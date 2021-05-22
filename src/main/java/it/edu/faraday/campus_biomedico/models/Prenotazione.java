package it.edu.faraday.campus_biomedico.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "prenotazione")
public class Prenotazione {

	@Id
	@Column(name = "id_prenotazione")
	private Integer id;

	@Basic
	private Date data;

	@ManyToOne
	@JoinColumn(name = "cod_p")
	private Paziente paziente;

	@ManyToOne
	@JoinColumn(name = "id_prestazione")
	private Prestazione prestazione;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "presidio",
			joinColumns = @JoinColumn(name = "id_prenotazione"),
			inverseJoinColumns = @JoinColumn(name = "cod_m"))
	private Set<Medico> medici;

	public Prenotazione() {
	}

	public Integer getId() {
		return id;
	}

	public Prenotazione setId(Integer id) {
		this.id = id;
		return this;
	}

	public Date getData() {
		return data;
	}

	public Prenotazione setData(Date data) {
		this.data = data;
		return this;
	}

	public Paziente getPaziente() {
		return paziente;
	}

	public Prenotazione setPaziente(Paziente paziente) {
		this.paziente = paziente;
		return this;
	}

	public Prestazione getPrestazione() {
		return prestazione;
	}

	public Prenotazione setPrestazione(Prestazione prestazione) {
		this.prestazione = prestazione;
		return this;
	}

	public Set<Medico> getMedici() {
		return medici;
	}

	public Prenotazione setMedici(Set<Medico> medici) {
		this.medici = medici;
		return this;
	}

}
