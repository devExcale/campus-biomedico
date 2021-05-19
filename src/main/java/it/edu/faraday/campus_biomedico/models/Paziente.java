package it.edu.faraday.campus_biomedico.models;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "paziente")
public class Paziente {

	@Id
	@Column(name = "cod_f")
	private String codiceFiscale;

	@Basic
	private String nome;

	@Basic
	private String cognome;

	@Column(name = "data_n")
	private Date dataNascita;

	@Basic
	private String email;

	@Basic
	private String cellulare;

	@Column(name = "via_residenza")
	private String viaResidenza;

	@Basic
	private String citta;

	@Basic
	private String cap;

}
