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

import java.io.UnsupportedEncodingException;

import static it.edu.faraday.campus_biomedico.CampusBiomedicoApplication.encode;

@Controller
@RequestMapping("/paziente")
public class UtenteController {

	@Autowired
	private PazienteRepository pazienteRepo;

	@GetMapping("/registrati")
	private ModelAndView registrati_view(Alert alert) {

		ModelAndView mav = new ModelAndView("paziente/registrazione");

		if(alert.getMessage() != null)
			mav.addObject("alert", alert);

		return mav;
	}

	@SuppressWarnings("SpringMVCViewInspection")
	@PostMapping("/registrati/submit")
	private String registrati_submit(Paziente paziente) throws UnsupportedEncodingException {

		boolean esiste = pazienteRepo.findById(paziente.getCodiceFiscale())
				.isPresent();

		if(esiste)
			return "redirect:/paziente/registrati?message=" + encode("Utente già registrato") + "&type=danger";

		pazienteRepo.save(paziente);

		return "redirect:/paziente/login?message=" + encode("Utente registrato con successo") + "&type=success";
	}

	@GetMapping("/login")
	private ModelAndView login_view() {
		// TODO: check already logged in
		return new ModelAndView("paziente/login");
	}

	@PostMapping("/login/submit")
	private String login_submit() {
		// TODO
		return "redirect:/paziente/home";
	}

}
