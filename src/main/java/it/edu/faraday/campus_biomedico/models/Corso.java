package it.edu.faraday.campus_biomedico.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "corso")
public class Corso {

	@Id
	@Column(name = "id_corso")
	private Integer id;

	@Basic
	private String titolo;

	@Basic
	private String descrizione;

	@Column(name = "ora_inizio")
	private Timestamp inizio;

	@Column(name = "ora_fine")
	private Timestamp fine;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "partecipazione_corso",
			joinColumns = @JoinColumn(name = "id_corso"),
			inverseJoinColumns = @JoinColumn(name = "cod_m"))
	private Set<Medico> mediciPartecipanti;

	public Corso() {
	}

	public Integer getId() {
		return id;
	}

	public Corso setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getTitolo() {
		return titolo;
	}

	public Corso setTitolo(String titolo) {
		this.titolo = titolo;
		return this;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public Corso setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}

	public Timestamp getInizio() {
		return inizio;
	}

	public Corso setInizio(Timestamp inizio) {
		this.inizio = inizio;
		return this;
	}

	public Timestamp getFine() {
		return fine;
	}

	public Corso setFine(Timestamp fine) {
		this.fine = fine;
		return this;
	}

	public Set<Medico> getMediciPartecipanti() {
		return mediciPartecipanti;
	}

	public Corso setMediciPartecipanti(Set<Medico> mediciPartecipanti) {
		this.mediciPartecipanti = mediciPartecipanti;
		return this;
	}

}
