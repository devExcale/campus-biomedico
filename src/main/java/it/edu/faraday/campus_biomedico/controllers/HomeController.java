package it.edu.faraday.campus_biomedico.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@GetMapping("/")
	private String index() {
		return "redirect:/home";
	}

	@GetMapping("/home")
	private ModelAndView home() {
		return new ModelAndView("homepage");
	}

}
