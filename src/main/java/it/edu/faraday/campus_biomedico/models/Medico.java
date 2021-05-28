package it.edu.faraday.campus_biomedico.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "medico")
public class Medico {

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

	@Basic
	private String password;

	@Basic
	@Column(name = "id_sessione")
	private Long sessione;

	@ManyToMany(mappedBy = "medici", fetch = FetchType.LAZY)
	private Set<Prenotazione> prenotazioni;

	@ManyToMany(mappedBy = "mediciPartecipanti", fetch = FetchType.LAZY)
	private Set<Corso> partecipazioniCorsi;

	public Medico() {
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public Medico setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public String getNome() {
		return nome;
	}

	public Medico setNome(String nome) {
		this.nome = nome;
		return this;
	}

	public String getCognome() {
		return cognome;
	}

	public Medico setCognome(String cognome) {
		this.cognome = cognome;
		return this;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public Medico setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Medico setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getCellulare() {
		return cellulare;
	}

	public Medico setCellulare(String cellulare) {
		this.cellulare = cellulare;
		return this;
	}

	public String getViaResidenza() {
		return viaResidenza;
	}

	public Medico setViaResidenza(String viaResidenza) {
		this.viaResidenza = viaResidenza;
		return this;
	}

	public String getCitta() {
		return citta;
	}

	public Medico setCitta(String citta) {
		this.citta = citta;
		return this;
	}

	public String getCap() {
		return cap;
	}

	public Medico setCap(String cap) {
		this.cap = cap;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public Medico setPassword(String password) {
		this.password = password;
		return this;
	}

	public Long getSessione() {
		return sessione;
	}

	public Medico setSessione(Long sessione) {
		this.sessione = sessione;
		return this;
	}

	public Set<Prenotazione> getPrenotazioni() {
		return prenotazioni;
	}

	public Medico setPrenotazioni(Set<Prenotazione> prenotazioni) {
		this.prenotazioni = prenotazioni;
		return this;
	}

	public Set<Corso> getPartecipazioniCorsi() {
		return partecipazioniCorsi;
	}

	public Medico setPartecipazioniCorsi(Set<Corso> partecipazioniCorsi) {
		this.partecipazioniCorsi = partecipazioniCorsi;
		return this;
	}

}
