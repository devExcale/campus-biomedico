package it.edu.faraday.campus_biomedico.models;

import javax.persistence.*;

@Entity
@Table(name = "prestazione")
public class Prestazione {

	@Id
	@Column(name = "id_prestazione")
	private Integer id;

	@Basic
	private String denominazione;

	@Basic(fetch = FetchType.LAZY)
	private String descrizione;

	public Prestazione() {
	}

	@Basic
	private Float costo;

	public Integer getId() {
		return id;
	}

	public Prestazione setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public Prestazione setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public Prestazione setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}

	public Float getCosto() {
		return costo;
	}

	public Prestazione setCosto(Float costo) {
		this.costo = costo;
		return this;
	}

}
