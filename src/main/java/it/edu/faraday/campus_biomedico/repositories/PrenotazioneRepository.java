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

	@Query("SELECT p FROM Prenotazione p WHERE p.paziente.codiceFiscale = ?1")
	Iterable<Prenotazione> findAllByPazienteId(String codiceFiscale);

	@Query("SELECT p FROM Prenotazione p WHERE p.dataOra BETWEEN ?1 AND ?2")
	Iterable<Prenotazione> findAllByData(Date dataInizio, Date dataFine);

	@Query("SELECT p FROM Prenotazione p WHERE p.prenotante IS NOT NULL")
	Iterable<Prenotazione> findAllByMedicoPrenotante();

	@Query("SELECT p FROM Prenotazione p WHERE p.dataOra BETWEEN ?1 AND ?2 AND p.prenotante IS NOT NULL")
	Iterable<Prenotazione> findAllByDataAndMedicoPrenotante(Date dataInizio, Date dataFine);

}
