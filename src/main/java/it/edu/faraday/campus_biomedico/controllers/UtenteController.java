package it.edu.faraday.campus_biomedico.controllers;

import it.edu.faraday.campus_biomedico.models.Paziente;
import it.edu.faraday.campus_biomedico.models.Prenotazione;
import it.edu.faraday.campus_biomedico.models.Prestazione;
import it.edu.faraday.campus_biomedico.repositories.PazienteRepository;
import it.edu.faraday.campus_biomedico.repositories.PrenotazioneRepository;
import it.edu.faraday.campus_biomedico.repositories.PrestazioneRepository;
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
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static it.edu.faraday.campus_biomedico.CampusBiomedicoApplication.*;

@Controller
@RequestMapping("/paziente")
public class UtenteController {

	private static final String COOKIE_UTENTE = "paziente.cod";

	@Autowired
	private PazienteRepository pazienteRepo;

	@Autowired
	private PrenotazioneRepository prenotazioneRepo;

	@Autowired
	private PrestazioneRepository prestazioneRepo;

	@GetMapping("/registrati")
	private String registrati_view(Alert alert, Model model) {

		if(alert.getMessage() == null)
			alert = null;
		else if(alert.getType() == null)
			alert.setType("primary");

		model.addAttribute("alert", alert);

		return "paziente/registrazione";
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
	private String accedi_view(Alert alert, Model model,
			@CookieValue(name = COOKIE_UTENTE, required = false) String codUtente) {

		if(codUtente != null)
			return "redirect:dashboard";

		if(alert.getMessage() == null)
			alert = null;
		else if(alert.getType() == null)
			alert.setType("primary");

		model.addAttribute("alert", alert);

		return "paziente/accesso";
	}

	@PostMapping("/accedi/submit")
	private String accedi_submit(Paziente paziente, HttpServletResponse response, Model model) {

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

				Cookie cookie = new Cookie(COOKIE_UTENTE, dbPaziente.getCodiceFiscale());
				cookie.setPath("/paziente");
				response.addCookie(cookie);

				model.addAttribute("title", "Login Area Pazienti");
				model.addAttribute("message", "Login effettuato");
				model.addAttribute("nextUrl", "/paziente/dashboard");
				action = "action_success";

			} else
				action = "redirect:/paziente/accedi?message=" + encode("Password errata") + "&type=danger";
		}

		return action;
	}

	@GetMapping("/logout")
	private String logout(HttpServletResponse response, Model model,
			@CookieValue(value = COOKIE_UTENTE, required = false) String codUtente) {

		// ELIMINAZIONE COOKIE SESSIONE
		Cookie cookie = new Cookie(COOKIE_UTENTE, "");
		cookie.setMaxAge(1);
		response.addCookie(cookie);

		Optional.ofNullable(codUtente)
				.flatMap(pazienteRepo::findById)
				.ifPresent(paziente -> {
					paziente.setSessione(null);
					pazienteRepo.save(paziente);
				});

		model.addAttribute("title", "Logout pazienti");
		model.addAttribute("message", "Logout effettuato correttamente");
		model.addAttribute("nextUrl", "/home");

		return "action_success";
	}

	@GetMapping("/dashboard")
	private String dashboard(Model model, Alert alert,
			@CookieValue(value = COOKIE_UTENTE, required = false) String codUtente) {

		if(codUtente == null)
			return "redirect:/paziente/accedi";

		//noinspection OptionalGetWithoutIsPresent
		Paziente paziente = pazienteRepo.findById(codUtente)
				.get();

		if(alert.getMessage() == null)
			alert = null;
		else if(alert.getType() == null)
			alert.setType("primary");

		model.addAttribute("paziente", paziente);
		model.addAttribute("alert", alert);

		return "paziente/dashboard";
	}

	@PostMapping("/modifica/submit")
	private String modifica_paziente(Paziente pazienteForm,
			@CookieValue(value = COOKIE_UTENTE, required = false) String codUtente) {

		if(codUtente == null)
			return "redirect:/paziente/accedi";

		//noinspection OptionalGetWithoutIsPresent
		Paziente paziente = pazienteRepo.findById(pazienteForm.getCodiceFiscale())
				.map(p -> p.setEmail(pazienteForm.getEmail())
						.setCellulare(pazienteForm.getCellulare())
						.setViaResidenza(pazienteForm.getViaResidenza())
						.setCitta(pazienteForm.getCitta())
						.setCap(pazienteForm.getCap()))
				.get();

		pazienteRepo.save(paziente);

		//noinspection SpringMVCViewInspection
		return "redirect:/paziente/accedi?message=" + encode("Paziente modificato con successo") + "&type=success";
	}

	@GetMapping("/prenota")
	private String prenota_view(Model model, @CookieValue(value = COOKIE_UTENTE, required = false) String codUtente) {

		if(codUtente == null)
			return "redirect:/paziente/accedi";

		//noinspection OptionalGetWithoutIsPresent
		Paziente paziente = pazienteRepo.findById(codUtente)
				.get();

		model.addAttribute("paziente", paziente);
		model.addAttribute("prestazioni", prestazioneRepo.findAll());
		model.addAttribute("domani",
				LocalDate.now()
						.plusDays(1)
						.format(DATE_FORMATTER));

		return "paziente/prenotazione";
	}

	@PostMapping("/prenota/submit")
	private String prenota_submit(Integer idPrestazione, String data, String ora,
			@CookieValue(value = COOKIE_UTENTE, required = false) String codUtente) {

		if(codUtente == null)
			return "redirect:/paziente/accedi";

		//noinspection OptionalGetWithoutIsPresent
		Paziente paziente = pazienteRepo.findById(codUtente)
				.get();
		//noinspection OptionalGetWithoutIsPresent
		Prestazione prestazione = prestazioneRepo.findById(idPrestazione)
				.get();

		Date dataOra;

		try {
			dataOra = TIMESTAMP_FORMATTER.parse(data + " " + ora);
		} catch(ParseException e) {
			//noinspection SpringMVCViewInspection
			return "redirect:/paziente/prenota?message=" + encode("Formato data errato") + "&type=danger";
		}

		Prenotazione prenotazione = new Prenotazione().setPaziente(paziente)
				.setDataOra(dataOra)
				.setPrestazione(prestazione);
		prenotazioneRepo.save(prenotazione);

		// TODO: INVIO EMAIL

		//noinspection SpringMVCViewInspection
		return "redirect:/paziente/dashboard?message=" + encode("Prenotazione effettuata") + "&type=success";
	}

	@GetMapping("/prenotazioni")
	private String lista_prenotazioni(Model model,
			@CookieValue(value = COOKIE_UTENTE, required = false) String codUtente) {

		if(codUtente == null)
			return "redirect:/paziente/dashboard";

		//noinspection OptionalGetWithoutIsPresent
		Paziente paziente = pazienteRepo.findById(codUtente)
				.get();

		model.addAttribute("paziente", paziente);

		return "paziente/lista_prenotazioni";
	}

}
