package it.edu.faraday.campus_biomedico.controllers;

import it.edu.faraday.campus_biomedico.models.Anamnesi;
import it.edu.faraday.campus_biomedico.models.Medico;
import it.edu.faraday.campus_biomedico.models.Paziente;
import it.edu.faraday.campus_biomedico.models.Prenotazione;
import it.edu.faraday.campus_biomedico.repositories.AnamnesiRepository;
import it.edu.faraday.campus_biomedico.repositories.MedicoRepository;
import it.edu.faraday.campus_biomedico.repositories.PazienteRepository;
import it.edu.faraday.campus_biomedico.repositories.PrenotazioneRepository;
import it.edu.faraday.campus_biomedico.utils.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

import static it.edu.faraday.campus_biomedico.CampusBiomedicoApplication.encode;

@Controller
@RequestMapping("/medico")
public class MedicoController {

	private static final String COOKIE_MEDICO = "medico.cod";
	private static final String COOKIE_SESSIONE = "medico.id_sessione";

	@Autowired
	private MedicoRepository medicoRepo;

	@Autowired
	private PazienteRepository pazientiRepo;

	@Autowired
	private AnamnesiRepository anamnesiRepo;

	@Autowired
	private PrenotazioneRepository prenotazioneRepo;

	private final DateFormat timestampFormatter;
	private final DateFormat dateFormatter;

	public MedicoController() {
		timestampFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	}

	@GetMapping("/accedi")
	private String accedi_view(Alert alert, Model model, HttpServletResponse response,
			@CookieValue(name = COOKIE_MEDICO, required = false) String codUtente,
			@CookieValue(name = COOKIE_SESSIONE, required = false) Long sessione) {

		if(codUtente != null) {

			Optional<Medico> medicoOpt = medicoRepo.findById(codUtente);

			Cookie c1 = new Cookie(COOKIE_MEDICO, "");
			Cookie c2 = new Cookie(COOKIE_SESSIONE, "");
			c1.setMaxAge(1);
			c2.setMaxAge(1);

			if(!medicoOpt.isPresent()) {

				response.addCookie(c1);
				response.addCookie(c2);

			} else {

				Medico medico = medicoOpt.get();
				if(!sessione.equals(medico.getSessione())) {

					response.addCookie(c1);
					response.addCookie(c2);
					alert.setType("danger");
					alert.setMessage("Sessione invalida");

				} else
					return "redirect:/medico/dashboard";

			}

		}

		if(alert.getMessage() == null)
			alert = null;
		else if(alert.getType() == null)
			alert.setType("primary");

		model.addAttribute("alert", alert);

		return "medico/accesso";
	}

	@PostMapping("/accedi/submit")
	private String accedi_submit(Medico medico, HttpServletResponse response, Model model) {

		String action;
		Optional<Medico> medicoOpt = medicoRepo.findById(medico.getCodiceFiscale());

		if(!medicoOpt.isPresent())
			action = "redirect:/medico/accedi?message=" + encode("Codice medico inesistente") + "&type=danger";
		else {

			Medico dbMedico = medicoOpt.get();
			String password = dbMedico.getPassword();

			if(password.equals(medico.getPassword())) {

				long sessione = System.currentTimeMillis();

				dbMedico.setSessione(sessione);
				medicoRepo.save(dbMedico);

				Cookie c1 = new Cookie(COOKIE_MEDICO, dbMedico.getCodiceFiscale());
				Cookie c2 = new Cookie(COOKIE_SESSIONE, String.valueOf(sessione));
				c1.setPath("/medico");
				c2.setPath("/medico");

				response.addCookie(c1);
				response.addCookie(c2);

				model.addAttribute("title", "Login Area Medici");
				model.addAttribute("message", "Login effettuato");
				model.addAttribute("nextUrl", "/medico/dashboard");
				return "action_success";

			} else
				action = "redirect:/medico/accedi?message=" + encode("Password errata") + "&type=danger";
		}

		return action;
	}

	@GetMapping("/logout")
	private String logout(HttpServletResponse response, Model model,
			@CookieValue(value = COOKIE_MEDICO, required = false) String codUtente) {

		// ELIMINAZIONE COOKIE SESSIONE
		response.addCookie(new Cookie(COOKIE_MEDICO, ""));
		response.addCookie(new Cookie(COOKIE_SESSIONE, ""));

		Optional.ofNullable(codUtente)
				.flatMap(medicoRepo::findById)
				.ifPresent(medico -> {
					medico.setSessione(null);
					medicoRepo.save(medico);
				});

		model.addAttribute("title", "Logout pazienti");
		model.addAttribute("message", "Logout effettuato correttamente");
		model.addAttribute("nextUrl", "/home");

		return "action_success";
	}

	@GetMapping("/dashboard")
	private String dashboard(Model model, Alert alert,
			@CookieValue(value = COOKIE_MEDICO, required = false) String codUtente) {

		if(codUtente == null)
			return "redirect:/medico/accedi";

		//noinspection OptionalGetWithoutIsPresent
		Medico medico = medicoRepo.findById(codUtente)
				.get();

		if(alert.getMessage() == null)
			alert = null;
		else if(alert.getType() == null)
			alert.setType("primary");

		model.addAttribute("medico", medico);
		model.addAttribute("alert", alert);

		return "medico/dashboard";
	}

	@GetMapping("/pazienti")
	private String lista_pazienti(Model model, @CookieValue(value = COOKIE_MEDICO, required = false) String codUtente) {

		if(codUtente == null)
			return "redirect:/medico/accedi";

		//noinspection OptionalGetWithoutIsPresent
		Medico medico = medicoRepo.findById(codUtente)
				.get();

		model.addAttribute("medico", medico);
		model.addAttribute("pazienti", pazientiRepo.findAll());

		return "medico/lista_pazienti";
	}

	@GetMapping("/pazienti/{codPaziente}")
	private String info_paziente(Model model, Alert alert, @PathVariable String codPaziente,
			@CookieValue(value = COOKIE_MEDICO, required = false) String codUtente) {

		if(codUtente == null)
			return "redirect:/medico/accedi";

		if(alert.getMessage() == null)
			alert = null;
		else if(alert.getType() == null)
			alert.setType("primary");

		//noinspection OptionalGetWithoutIsPresent
		Medico medico = medicoRepo.findById(codUtente)
				.get();

		//noinspection OptionalGetWithoutIsPresent
		Paziente paziente = pazientiRepo.findById(codPaziente)
				.get();

		paziente.getAnamnesi()
				.sort(Comparator.comparing(Anamnesi::getData));

		model.addAttribute("medico", medico);
		model.addAttribute("paziente", paziente);
		model.addAttribute("alert", alert);

		return "medico/info_paziente";
	}

	@PostMapping("/pazienti/{codPaziente}/anamnesi")
	private String info_paziente(Anamnesi anamnesi, @PathVariable String codPaziente,
			@CookieValue(value = COOKIE_MEDICO, required = false) String codUtente) {

		if(codUtente == null)
			return "redirect:/medico/accedi";

		//noinspection OptionalGetWithoutIsPresent
		Paziente paziente = pazientiRepo.findById(codPaziente)
				.get();

		anamnesi.setPaziente(paziente);
		anamnesiRepo.save(anamnesi);

		//noinspection SpringMVCViewInspection
		return "redirect:/medico/pazienti/" + codPaziente + "?type=success&message=" +
				encode("Anamnesi aggiunta con successo");
	}

	@GetMapping("prenotazioni")
	private String lista_prenotazioni(String data, Boolean medicoPrenotante, Model model,
			@CookieValue(value = COOKIE_MEDICO, required = false) String codUtente) {

		System.out.println("Data: " + data);
		System.out.println("Medico: " + medicoPrenotante);

		Date date = null;
		medicoPrenotante = medicoPrenotante != null && medicoPrenotante;

		if(data != null && !data.isEmpty())
			try {
				date = dateFormatter.parse(data);
			} catch(ParseException e) {
				model.addAttribute("alert", new Alert().error("Data invalida"));
				model.addAttribute("prenotazioni",
						medicoPrenotante ? prenotazioneRepo.findAllByMedicoPrenotante() : prenotazioneRepo.findAll());
			}

		//noinspection OptionalGetWithoutIsPresent
		Medico medico = medicoRepo.findById(codUtente)
				.get();

		Iterable<Prenotazione> prenotazioni;
		if(date != null) {
			if(medicoPrenotante) {
				prenotazioni = prenotazioneRepo.findAllByDataAndMedicoPrenotante(date);
				System.out.println("findAllByDataAndMedicoPrenotante");
			} else {
				prenotazioni = prenotazioneRepo.findAllByData(date);
				System.out.println("findAllByData");
			}
		} else if(medicoPrenotante) {
			prenotazioni = prenotazioneRepo.findAllByMedicoPrenotante();
			System.out.println("findAllByMedicoPrenotante");
		} else {
			prenotazioni = prenotazioneRepo.findAll();
			System.out.println("findAll");
		}

		model.addAttribute("prenotazioni", prenotazioni);
		model.addAttribute("medico", medico);

		return "medico/lista_prenotazioni";
	}

}
