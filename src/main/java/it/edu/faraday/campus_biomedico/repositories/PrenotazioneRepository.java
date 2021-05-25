package it.edu.faraday.campus_biomedico.repositories;

import it.edu.faraday.campus_biomedico.models.Prenotazione;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrenotazioneRepository extends CrudRepository<Prenotazione, Integer> {

}
