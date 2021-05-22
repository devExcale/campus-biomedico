package it.edu.faraday.campus_biomedico.repositories;

import it.edu.faraday.campus_biomedico.models.Medico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends CrudRepository<Medico, String> {

}
