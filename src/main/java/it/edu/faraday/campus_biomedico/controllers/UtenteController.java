package it.edu.faraday.campus_biomedico.controllers;

import it.edu.faraday.campus_biomedico.models.Paziente;
import it.edu.faraday.campus_biomedico.repositories.PazienteRepository;
import it.edu.faraday.campus_biomedico.utils.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static it.edu.faraday.campus_biomedico.CampusBiomedicoApplication.encode;

@Controller
@RequestMapping("/paziente")
public class UtenteController {

	@Autowired private PazienteRepository pazienteRepo;

	@GetMapping("/registrati") private ModelAndView registrati_view(Alert alert) {

		ModelAndView mav = new ModelAndView("paziente/registrazione");

		if(alert.getMessage() == null)
			alert = null;
		else if(alert.getType() == null)
			alert.setType("primary");

		mav.addObject("alert", alert);

		return mav;
	}

	@SuppressWarnings("SpringMVCViewInspection") @PostMapping("/registrati/submit")
	private String registrati_submit(Paziente paziente) {

		boolean esiste = pazienteRepo.findById(paziente.getCodiceFiscale())
				.isPresent();

		if(esiste)
			return "redirect:/paziente/registrati?message=" + encode("Utente già registrato") + "&type=danger";

		pazienteRepo.save(paziente);

		return "redirect:/paziente/accedi?message=" + encode("Utente registrato con successo") + "&type=success";
	}

	@GetMapping("/accedi")
	private String accedi_view(Alert alert, Model model, HttpServletResponse response,
			@CookieValue(name = "paziente.cod_f", required = false) String codUtente,
			@CookieValue(name = "paziente.id_sessione", required = false) Long sessione) {

		if(codUtente != null) {

			Optional<Paziente> pazienteOpt = pazienteRepo.findById(codUtente);

			Cookie c1 = new Cookie("paziente.cod_f", "");
			Cookie c2 = new Cookie("paziente.id_sessione", "");
			c1.setMaxAge(1);
			c2.setMaxAge(1);

			if(!pazienteOpt.isPresent()) {

				response.addCookie(c1);
				response.addCookie(c2);

			} else {

				Paziente paziente = pazienteOpt.get();
				if(!sessione.equals(paziente.getSessione())) {

					response.addCookie(c1);
					response.addCookie(c2);
					alert.setType("danger");
					alert.setMessage("Sessione invalida");

				} else
					return "redirect:dashboard";

			}

		}

		if(alert.getMessage() == null)
			alert = null;
		else if(alert.getType() == null)
			alert.setType("primary");

		model.addAttribute("alert", alert);

		return "paziente/accesso";
	}

	@PostMapping("/accedi/submit")
	private String login_submit(Paziente paziente, HttpServletResponse response) {

		String action;
		Optional<Paziente> pazienteOpt = pazienteRepo.findById(paziente.getCodiceFiscale());

		if(!pazienteOpt.isPresent())
			action = "redirect:/paziente/accedi?message=" + encode("Utente non registrato") + "&type=danger";
		else {

			Paziente dbPaziente = pazienteOpt.get();
			String password = dbPaziente.getPassword();

			if(password.equals(paziente.getPassword())) {

				long sessione = System.currentTimeMillis();

				dbPaziente.setSessione(sessione);
				pazienteRepo.save(dbPaziente);

				// TODO: CODICE UTENTE, DAHSBOARD
				response.addCookie(new Cookie("paziente.id_sessione", String.valueOf(sessione)));
				action = "redirect:/paziente/home";

			} else
				action = "redirect:/paziente/accedi?message=" + encode("Password errata") + "&type=danger";
		}


		return action;
	}

}
