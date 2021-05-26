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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static it.edu.faraday.campus_biomedico.CampusBiomedicoApplication.encode;
import static it.edu.faraday.campus_biomedico.CampusBiomedicoApplication.getLogger;

@Controller
@RequestMapping("/paziente")
public class UtenteController {

	private static final String COOKIE_UTENTE = "paziente.cod";
	private static final String COOKIE_SESSIONE = "paziente.id_sessione";

	@Autowired
	private PazienteRepository pazienteRepo;

	@Autowired
	private PrenotazioneRepository prenotazioneRepo;

	@Autowired
	private PrestazioneRepository prestazioneRepo;

	private final DateFormat timestampFormatter;

	public UtenteController() {
		timestampFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}

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
	private String accedi_view(Alert alert, Model model, HttpServletResponse response,
			@CookieValue(name = COOKIE_UTENTE, required = false) String codUtente,
			@CookieValue(name = COOKIE_SESSIONE, required = false) Long sessione) {

		if(codUtente != null) {

			Optional<Paziente> pazienteOpt = pazienteRepo.findById(codUtente);

			Cookie c1 = new Cookie(COOKIE_UTENTE, "");
			Cookie c2 = new Cookie(COOKIE_SESSIONE, "");
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

				Cookie c1 = new Cookie(COOKIE_UTENTE, dbPaziente.getCodiceFiscale());
				Cookie c2 = new Cookie(COOKIE_SESSIONE, String.valueOf(sessione));
				c1.setPath("/paziente");
				c2.setPath("/paziente");

				response.addCookie(c1);
				response.addCookie(c2);

				model.addAttribute("title", "Login Area Pazienti");
				model.addAttribute("message", "Login effettuato");
				model.addAttribute("nextUrl", "/paziente/dashboard");
				return "action_success";

			} else
				action = "redirect:/paziente/accedi?message=" + encode("Password errata") + "&type=danger";
		}

		return action;
	}

	@GetMapping("/logout")
	private String logout(HttpServletResponse response, Model model,
			@CookieValue(value = COOKIE_UTENTE, required = false) String codUtente) {

		// ELIMINAZIONE COOKIE SESSIONE
		response.addCookie(new Cookie(COOKIE_UTENTE, ""));
		response.addCookie(new Cookie(COOKIE_SESSIONE, ""));

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
	private ModelAndView dashboard(@CookieValue(COOKIE_UTENTE) String codUtente, Alert alert) {

		//noinspection OptionalGetWithoutIsPresent
		Paziente paziente = pazienteRepo.findById(codUtente)
				.get();

		if(alert.getMessage() == null)
			alert = null;
		else if(alert.getType() == null)
			alert.setType("primary");

		ModelAndView mav = new ModelAndView("paziente/dashboard");
		mav.addObject("paziente", paziente);
		mav.addObject("alert", alert);

		return mav;
	}

	@PostMapping("/modifica/submit")
	private String modifica_paziente(Paziente pazienteForm) {

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
	private ModelAndView prenota_view(@CookieValue(COOKIE_UTENTE) String codUtente) {

		//noinspection OptionalGetWithoutIsPresent
		Paziente paziente = pazienteRepo.findById(codUtente)
				.get();

		ModelAndView mav = new ModelAndView("paziente/prenota");
		mav.addObject("paziente", paziente);
		mav.addObject("prestazioni", prestazioneRepo.findAll());
		mav.addObject("domani",
				LocalDate.now()
						.plusDays(1)
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));


		return mav;
	}

	@PostMapping("/prenota/submit")
	private String prenota_submit(Integer idPrestazione, String data, String ora,
			@CookieValue(COOKIE_UTENTE) String codUtente) {

		//noinspection OptionalGetWithoutIsPresent
		Paziente paziente = pazienteRepo.findById(codUtente)
				.get();
		//noinspection OptionalGetWithoutIsPresent
		Prestazione prestazione = prestazioneRepo.findById(idPrestazione)
				.get();

		Timestamp timestamp;

		try {
			timestamp = new Timestamp(timestampFormatter.parse(data + " " + ora)
					.getTime());
		} catch(ParseException e) {
			getLogger().error(e.getMessage(), e);
			//noinspection SpringMVCViewInspection
			return "redirect:/paziente/prenota?message=" + encode("Formato data errato") + "&type=danger";
		}

		Prenotazione prenotazione = new Prenotazione().setPaziente(paziente)
				.setDataOra(timestamp)
				.setPrestazione(prestazione);
		prenotazioneRepo.save(prenotazione);

		// TODO: INVIO EMAIL

		//noinspection SpringMVCViewInspection
		return "redirect:/paziente/dashboard?message=" + encode("Prenotazione effettuata") + "&type=success";
	}

}
