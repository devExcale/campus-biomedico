package it.edu.faraday.campus_biomedico.models;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "amministratore")
public class Amministratore {

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

	public Amministratore() {
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public Amministratore setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public String getNome() {
		return nome;
	}

	public Amministratore setNome(String nome) {
		this.nome = nome;
		return this;
	}

	public String getCognome() {
		return cognome;
	}

	public Amministratore setCognome(String cognome) {
		this.cognome = cognome;
		return this;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public Amministratore setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Amministratore setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getCellulare() {
		return cellulare;
	}

	public Amministratore setCellulare(String cellulare) {
		this.cellulare = cellulare;
		return this;
	}

	public String getViaResidenza() {
		return viaResidenza;
	}

	public Amministratore setViaResidenza(String viaResidenza) {
		this.viaResidenza = viaResidenza;
		return this;
	}

	public String getCitta() {
		return citta;
	}

	public Amministratore setCitta(String citta) {
		this.citta = citta;
		return this;
	}

	public String getCap() {
		return cap;
	}

	public Amministratore setCap(String cap) {
		this.cap = cap;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public Amministratore setPassword(String password) {
		this.password = password;
		return this;
	}

}
