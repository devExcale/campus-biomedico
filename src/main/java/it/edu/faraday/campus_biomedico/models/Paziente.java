package it.edu.faraday.campus_biomedico.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "paziente")
public class Paziente {

	@Id
	@Column(name = "cod_f")
	private String codiceFiscale;

	@Basic(optional = false)
	private String nome;

	@Basic(optional = false)
	private String cognome;

	@Basic(optional = false)
	@Column(name = "data_n")
	private Date dataNascita;

	@Basic(optional = false)
	private String email;

	@Basic(optional = false)
	private String cellulare;

	@Basic(optional = true)
	@Column(name = "via_residenza")
	private String viaResidenza;

	@Basic(optional = true)
	private String citta;

	@Basic(optional = true)
	private String cap;

	@Basic(optional = false)
	private String password;

	@Basic(optional = true)
	@Column(name = "id_sessione")
	private Long sessione;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "paziente")
	private List<Anamnesi> anamnesi;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "paziente")
	private Set<Prenotazione> prenotazioni;

	public Paziente() {
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public Paziente setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public String getNome() {
		return nome;
	}

	public Paziente setNome(String nome) {
		this.nome = nome;
		return this;
	}

	public String getCognome() {
		return cognome;
	}

	public Paziente setCognome(String cognome) {
		this.cognome = cognome;
		return this;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public Paziente setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Paziente setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getCellulare() {
		return cellulare;
	}

	public Paziente setCellulare(String cellulare) {
		this.cellulare = cellulare;
		return this;
	}

	public String getViaResidenza() {
		return viaResidenza;
	}

	public Paziente setViaResidenza(String viaResidenza) {
		this.viaResidenza = viaResidenza;
		return this;
	}

	public String getCitta() {
		return citta;
	}

	public Paziente setCitta(String citta) {
		this.citta = citta;
		return this;
	}

	public String getCap() {
		return cap;
	}

	public Paziente setCap(String cap) {
		this.cap = cap;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public Paziente setPassword(String password) {
		this.password = password;
		return this;
	}

	public Long getSessione() {
		return sessione;
	}

	public Paziente setSessione(Long sessione) {
		this.sessione = sessione;
		return this;
	}

	public List<Anamnesi> getAnamnesi() {
		return anamnesi;
	}

	public Paziente setAnamnesi(List<Anamnesi> anamnesi) {
		this.anamnesi = anamnesi;
		return this;
	}

	public Set<Prenotazione> getPrenotazioni() {
		return prenotazioni;
	}

	public Paziente setPrenotazioni(Set<Prenotazione> prenotazioni) {
		this.prenotazioni = prenotazioni;
		return this;
	}

}
