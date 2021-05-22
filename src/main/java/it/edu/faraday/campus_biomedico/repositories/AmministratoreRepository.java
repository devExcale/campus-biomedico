package it.edu.faraday.campus_biomedico.repositories;

import it.edu.faraday.campus_biomedico.models.Amministratore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmministratoreRepository extends CrudRepository<Amministratore, String> {

}
