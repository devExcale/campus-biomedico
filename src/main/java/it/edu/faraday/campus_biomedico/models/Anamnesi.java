package it.edu.faraday.campus_biomedico.models;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "anamnesi")
public class Anamnesi {

	@Id
	@Column(name = "id_anamnesi")
	private Integer id;

	@Basic
	private String descrizione;

	@Basic
	private Date data;

	@ManyToOne
	@JoinColumn(name = "cod_p")
	private Paziente paziente;

	public Anamnesi() {
	}

	public Integer getId() {
		return id;
	}

	public Anamnesi setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public Anamnesi setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}

	public Date getData() {
		return data;
	}

	public Anamnesi setData(Date data) {
		this.data = data;
		return this;
	}

	public Paziente getPaziente() {
		return paziente;
	}

	public Anamnesi setPaziente(Paziente paziente) {
		this.paziente = paziente;
		return this;
	}

}
