package it.edu.faraday.campus_biomedico.controllers;

import it.edu.faraday.campus_biomedico.models.Paziente;
import it.edu.faraday.campus_biomedico.repositories.PazienteRepository;
import it.edu.faraday.campus_biomedico.utils.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import static it.edu.faraday.campus_biomedico.CampusBiomedicoApplication.encode;

@Controller
@RequestMapping("/paziente")
public class UtenteController {

	@Autowired
	private PazienteRepository pazienteRepo;

	@GetMapping("/registrati")
	private ModelAndView registrati_view(Alert alert) {

		ModelAndView mav = new ModelAndView("paziente/registrazione");

		if(alert.getMessage() == null)
			alert = null;
		else if(alert.getType() == null)
			alert.setType("primary");

		mav.addObject("alert", alert);

		return mav;
	}

	@SuppressWarnings("SpringMVCViewInspection")
	@PostMapping("/registrati/submit")
	private String registrati_submit(Paziente paziente) {

		boolean esiste = pazienteRepo.findById(paziente.getCodiceFiscale())
				.isPresent();

		if(esiste)
			return "redirect:/paziente/registrati?message=" + encode("Utente già registrato") + "&type=danger";

		pazienteRepo.save(paziente);

		return "redirect:/paziente/accedi?message=" + encode("Utente registrato con successo") + "&type=success";
	}

	@GetMapping("/accedi")
	private ModelAndView accedi_view(Alert alert) {

		ModelAndView mav = new ModelAndView("paziente/accesso");

		if(alert.getMessage() == null)
			alert = null;
		else if(alert.getType() == null)
			alert.setType("primary");

		mav.addObject("alert", alert);

		return mav;
	}

	@PostMapping("/accedi/submit")
	private String login_submit(Paziente paziente) {

		String action;
		Optional<Paziente> pazienteOpt = pazienteRepo.findById(paziente.getCodiceFiscale());

		if(!pazienteOpt.isPresent())
			action = "redirect:/paziente/accedi?message=" + encode("Utente non registrato") + "&type=danger";
		else {
			String password = pazienteOpt.get()
					.getPassword();

			if(password.equals(paziente.getPassword())) {

				// TODO: id_session
				action = "redirect:/paziente/home";

			} else
				action = "redirect:/paziente/accedi?message=" + encode("Password errata") + "&type=danger";
		}


		return action;
	}

}
