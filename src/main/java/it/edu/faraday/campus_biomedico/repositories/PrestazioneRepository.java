package it.edu.faraday.campus_biomedico.repositories;

import it.edu.faraday.campus_biomedico.models.Prestazione;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestazioneRepository extends CrudRepository<Prestazione, Integer> {

}
