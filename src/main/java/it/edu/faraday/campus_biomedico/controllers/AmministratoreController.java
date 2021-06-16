package it.edu.faraday.campus_biomedico.controllers;

import it.edu.faraday.campus_biomedico.models.Amministratore;
import it.edu.faraday.campus_biomedico.repositories.AmministratoreRepository;
import it.edu.faraday.campus_biomedico.utils.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static it.edu.faraday.campus_biomedico.CampusBiomedicoApplication.encode;

@Controller
@RequestMapping("/admin")
public class AmministratoreController {

	private static final String COOKIE_ADMIN = "admin.cod";

	@Autowired
	private AmministratoreRepository adminRepo;

	@GetMapping("/accedi")
	private String accedi_view(Alert alert, Model model,
			@CookieValue(name = COOKIE_ADMIN, required = false) String codUtente) {

		if(codUtente != null)
			return "redirect:/medico/dashboard";

		if(alert.getMessage() == null)
			alert = null;
		else if(alert.getType() == null)
			alert.setType("primary");

		model.addAttribute("alert", alert);

		return "amministratore/accesso";
	}

	@PostMapping("/accedi/submit")
	private String accedi_submit(Amministratore formAdmin, HttpServletResponse response, Model model) {

		String action;
		Optional<Amministratore> adminOpt = adminRepo.findById(formAdmin.getCodiceFiscale());

		if(!adminOpt.isPresent())
			action = "redirect:/medico/accedi?message=" + encode("Codice medico inesistente") + "&type=danger";
		else {

			Amministratore dbAdmin = adminOpt.get();
			String password = dbAdmin.getPassword();

			if(password.equals(formAdmin.getPassword())) {

				Cookie cookie = new Cookie(COOKIE_ADMIN, dbAdmin.getCodiceFiscale());
				cookie.setPath("/admin");
				response.addCookie(cookie);

				model.addAttribute("title", "Login Area Amministratori");
				model.addAttribute("message", "Login effettuato");
				model.addAttribute("nextUrl", "/admin/dashboard");
				action = "action_success";

			} else
				action = "redirect:/admin/accedi?message=" + encode("Password errata") + "&type=danger";
		}

		return action;
	}

}
