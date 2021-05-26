package it.edu.faraday.campus_biomedico.repositories;

import it.edu.faraday.campus_biomedico.models.Paziente;
import it.edu.faraday.campus_biomedico.models.Prenotazione;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrenotazioneRepository extends CrudRepository<Prenotazione, Integer> {

	@Query("SELECT p FROM Prenotazione p WHERE p.paziente = ?1")
	Iterable<Prenotazione> findAllByPaziente(Paziente paziente);

	@Query("SELECT p FROM Prenotazione p WHERE p.paziente.id = ?1")
	Iterable<Prenotazione> findAllByPazienteId(String codiceFiscale);

}
