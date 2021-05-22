package it.edu.faraday.campus_biomedico.repositories;

import it.edu.faraday.campus_biomedico.models.Paziente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PazienteRepository extends CrudRepository<Paziente, String> {

}
