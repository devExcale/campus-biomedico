package it.edu.faraday.campus_biomedico.repositories;

import it.edu.faraday.campus_biomedico.models.Paziente;
import it.edu.faraday.campus_biomedico.models.Prenotazione;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PrenotazioneRepository extends CrudRepository<Prenotazione, Integer> {

	@Query("SELECT p FROM Prenotazione p WHERE p.paziente = ?1")
	Iterable<Prenotazione> findAllByPaziente(Paziente paziente);

	@Query("SELECT p FROM Prenotazione p WHERE p.paziente.id = ?1")
	Iterable<Prenotazione> findAllByPazienteId(String codiceFiscale);

	@Query("SELECT p FROM Prenotazione p WHERE year(dataOra) = year(?1) AND month(dataOra) = month(?1) AND day(dataOra) = day(?1)")
	Iterable<Prenotazione> findAllByData(Date date);

	@Query("SELECT p FROM Prenotazione p WHERE prenotante IS NOT NULL")
	Iterable<Prenotazione> findAllByMedicoPrenotante();

	@Query("SELECT p FROM Prenotazione p WHERE year(dataOra) = year(?1) " +
			"AND month(dataOra) = month(?1) AND day(dataOra) =  day(?1) AND prenotante IS NOT NULL")
	Iterable<Prenotazione> findAllByDataAndMedicoPrenotante(Date data);

}
