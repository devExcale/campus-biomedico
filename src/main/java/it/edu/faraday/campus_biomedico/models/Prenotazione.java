package it.edu.faraday.campus_biomedico.models;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "prenotazione")
public class Prenotazione {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PREN_SEQ_GEN")
	@SequenceGenerator(name = "PREN_SEQ_GEN", sequenceName = "prenotazione_id_prenotazione_seq", allocationSize = 1)
	@Column(name = "id_prenotazione")
	private Integer id;

	@Basic
	@Column(name = "data")
	private Date dataOra;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cod_p")
	private Paziente paziente;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_prestazione")
	private Prestazione prestazione;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cod_m")
	private Medico prenotante;

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

	public Date getDataOra() {
		return dataOra;
	}

	public Prenotazione setDataOra(Date dataOra) {
		this.dataOra = dataOra;
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

	public Medico getMedicoPrenotante() {
		return prenotante;
	}

	public Prenotazione setMedicoPrenotante(Medico prenotante) {
		this.prenotante = prenotante;
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
