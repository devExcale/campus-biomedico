package it.edu.faraday.campus_biomedico.controllers;

import it.edu.faraday.campus_biomedico.models.Paziente;
import it.edu.faraday.campus_biomedico.repositories.PazienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.thymeleaf.spring5.view.ThymeleafView;

@Controller
@RequestMapping("/paziente")
public class UtenteController {

	@Autowired
	private PazienteRepository pazienteRepo;

	@GetMapping("/registrati")
	private View registrati_view() {
		return new ThymeleafView("utente/registrazione");
	}

	@PostMapping("/registrati/submit")
	private String registrati_submit(Paziente paziente) {
		// TODO
		return "redirect:/home";
	}

	@GetMapping("/login")
	private View login_view() {
		// TODO: check already logged in
		return new ThymeleafView("utente/login");
	}

	@PostMapping("/login/submit")
	private String login_submit() {
		// TODO
		return "redirect:/utente/home";
	}

}
