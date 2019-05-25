package ombrelloniani.model;

import java.time.LocalDate;
import java.util.List;

public class Prenotazione {
	
	private int idPrenotazione;
	private LocalDate dataInizio;
	private LocalDate dataFine;
	private int numeroLettini;
	private List<Ombrellone> ombrelloni;
	private Cliente cliente;
	private List<Servizio> servizi;
	
	public Prenotazione() {}
	
	public Prenotazione(int idPrenotazione, LocalDate dataInizio, LocalDate dataFine, int numeroLettini,
			List<Ombrellone> ombrelloni, Cliente cliente, List<Servizio> servizi) {
		super();
		this.idPrenotazione = idPrenotazione;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.numeroLettini = numeroLettini;
		this.ombrelloni = ombrelloni;
		this.cliente = cliente;
		this.servizi = servizi;
	}
	
	public int getIdPrenotazione() {
		return idPrenotazione;
	}
	
	public void setIdPrenotazione(int idPrenotazione) {
		this.idPrenotazione = idPrenotazione;
	}
	
	public LocalDate getDataInizio() {
		return dataInizio;
	}
	
	public void setDataInizio(LocalDate dataInizio) {
		this.dataInizio = dataInizio;
	}
	
	public LocalDate getDataFine() {
		return dataFine;
	}
	
	public void setDataFine(LocalDate dataFine) {
		this.dataFine = dataFine;
	}
	
	public int getNumeroLettini() {
		return numeroLettini;
	}
	
	public void setNumeroLettini(int numeroLettini) {
		this.numeroLettini = numeroLettini;
	}
	
	public List<Ombrellone> getOmbrelloni() {
		return ombrelloni;
	}
	
	public void setOmbrelloni(List<Ombrellone> ombrelloni) {
		this.ombrelloni = ombrelloni;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public List<Servizio> getServizi() {
		return servizi;
	}
	
	public void setServizi(List<Servizio> servizi) {
		this.servizi = servizi;
	}
}
