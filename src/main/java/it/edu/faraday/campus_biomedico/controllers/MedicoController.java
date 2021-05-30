package it.edu.faraday.campus_biomedico.controllers;

import it.edu.faraday.campus_biomedico.models.*;
import it.edu.faraday.campus_biomedico.repositories.*;
import it.edu.faraday.campus_biomedico.utils.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

import static it.edu.faraday.campus_biomedico.CampusBiomedicoApplication.*;

@Controller
@RequestMapping("/medico")
public class MedicoController {

	private static final String COOKIE_MEDICO = "medico.cod";

	@Autowired
	private MedicoRepository medicoRepo;

	@Autowired
	private PazienteRepository pazienteRepo;

	@Autowired
	private PrenotazioneRepository prenotazioneRepo;

	@Autowired
	private PrestazioneRepository prestazioneRepo;

	@Autowired
	private AnamnesiRepository anamnesiRepo;

	@GetMapping("/accedi")
	private String accedi_view(Alert alert, Model model,
			@CookieValue(name = COOKIE_MEDICO, required = false) String codUtente) {

		if(codUtente != null)
			return "redirect:/medico/dashboard";

		if(alert.getMessage() == null)
			alert = null;
		else if(alert.getType() == null)
			alert.setType("primary");

		model.addAttribute("alert", alert);

		return "medico/accesso";
	}

	@PostMapping("/accedi/submit")
	private String accedi_submit(Medico formMedico, HttpServletResponse response, Model model) {

		String action;
		Optional<Medico> medicoOpt = medicoRepo.findById(formMedico.getCodiceFiscale());

		if(!medicoOpt.isPresent())
			action = "redirect:/medico/accedi?message=" + encode("Codice medico inesistente") + "&type=danger";
		else {

			Medico dbMedico = medicoOpt.get();
			String password = dbMedico.getPassword();

			if(password.equals(formMedico.getPassword())) {

				Cookie cookie = new Cookie(COOKIE_MEDICO, dbMedico.getCodiceFiscale());
				cookie.setPath("/medico");
				response.addCookie(cookie);

				model.addAttribute("title", "Login Area Medici");
				model.addAttribute("message", "Login effettuato");
				model.addAttribute("nextUrl", "/medico/dashboard");
				action = "action_success";

			} else
				action = "redirect:/medico/accedi?message=" + encode("Password errata") + "&type=danger";
		}

		return action;
	}

	@GetMapping("/logout")
	private String logout(HttpServletResponse response, Model model,
			@CookieValue(value = COOKIE_MEDICO, required = false) String codUtente) {

		// ELIMINAZIONE COOKIE SESSIONE
		Cookie cookie = new Cookie(COOKIE_MEDICO, "");
		cookie.setMaxAge(1);
		response.addCookie(cookie);

		Optional.ofNullable(codUtente)
				.flatMap(medicoRepo::findById)
				.ifPresent(medico -> {
					medico.setSessione(null);
					medicoRepo.save(medico);
				});

		model.addAttribute("title", "Logout medici");
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
		model.addAttribute("pazienti", pazienteRepo.findAll());

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
		Paziente paziente = pazienteRepo.findById(codPaziente)
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
		Paziente paziente = pazienteRepo.findById(codPaziente)
				.get();

		anamnesi.setPaziente(paziente);
		anamnesiRepo.save(anamnesi);

		//noinspection SpringMVCViewInspection
		return "redirect:/medico/pazienti/" + codPaziente + "?type=success&message=" +
				encode("Anamnesi aggiunta con successo");
	}

	@GetMapping("prenotazioni")
	private String lista_prenotazioni(Model model, @RequestParam(required = false) Date data,
			@RequestParam(required = false, defaultValue = "false") Boolean medicoPrenotante,
			@CookieValue(value = COOKIE_MEDICO, required = false) String codUtente) {

		if(codUtente == null)
			return "redirect:/medico/accedi";

		//noinspection OptionalGetWithoutIsPresent
		Medico medico = medicoRepo.findById(codUtente)
				.get();

		Iterable<Prenotazione> prenotazioni;
		if(data != null) {
			Date domani = new Date(data.getTime() + 86339999L);
			if(medicoPrenotante)
				prenotazioni = prenotazioneRepo.findAllByDataAndMedicoPrenotante(data, domani);
			else
				prenotazioni = prenotazioneRepo.findAllByData(data, domani);
		} else if(medicoPrenotante)
			prenotazioni = prenotazioneRepo.findAllByMedicoPrenotante();
		else
			prenotazioni = prenotazioneRepo.findAll();

		Iterator<Prenotazione> i = prenotazioni.iterator();
		if(!i.hasNext())
			model.addAttribute("alert", new Alert().error("Nessuna prenotazione trovata"));

		model.addAttribute("prenotazioni", prenotazioni);
		model.addAttribute("medico", medico);
		model.addAttribute("data", data);
		model.addAttribute("medicoPrenotante", medicoPrenotante);

		return "medico/lista_prenotazioni";
	}

	@GetMapping("/prenota")
	private String prenota_view(Model model, @CookieValue(value = COOKIE_MEDICO, required = false) String codUtente) {

		if(codUtente == null)
			return "redirect:/medico/accedi";

		//noinspection OptionalGetWithoutIsPresent
		Medico medico = medicoRepo.findById(codUtente)
				.get();

		model.addAttribute("medico", medico);
		model.addAttribute("prestazioni", prestazioneRepo.findAll());
		model.addAttribute("domani",
				LocalDate.now()
						.plusDays(1)
						.format(DATE_FORMATTER));

		return "medico/prenotazione";
	}

	@PostMapping("/prenota/submit")
	private String prenota_submit(Integer idPrestazione, String data, String ora, String codiceFiscale,
			@CookieValue(value = COOKIE_MEDICO, required = false) String codUtente) {

		if(codUtente == null)
			return "redirect:/medico/accedi";

		//noinspection OptionalGetWithoutIsPresent
		Medico medico = medicoRepo.findById(codUtente)
				.get();
		//noinspection OptionalGetWithoutIsPresent
		Paziente paziente = pazienteRepo.findById(codiceFiscale)
				.get();
		//noinspection OptionalGetWithoutIsPresent
		Prestazione prestazione = prestazioneRepo.findById(idPrestazione)
				.get();

		Timestamp timestamp;

		try {
			timestamp = new Timestamp(TIMESTAMP_FORMATTER.parse(data + " " + ora)
					.getTime());
		} catch(ParseException e) {
			//noinspection SpringMVCViewInspection
			return "redirect:/medico/prenota?message=" + encode("Formato data errato") + "&type=danger";
		}

		Prenotazione prenotazione = new Prenotazione().setMedicoPrenotante(medico)
				.setPaziente(paziente)
				.setDataOra(timestamp)
				.setPrestazione(prestazione);
		prenotazioneRepo.save(prenotazione);

		// TODO: INVIO EMAIL

		//noinspection SpringMVCViewInspection
		return "redirect:/medico/dashboard?message=" + encode("Prenotazione effettuata") + "&type=success";
	}

}
