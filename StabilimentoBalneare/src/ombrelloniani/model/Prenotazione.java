package ombrelloniani.model;

import java.util.Date;
import java.util.List;

public class Prenotazione {
	
	private int idPrenotazione;
	private Date dataInizio;
	private Date dataFine;
	private int numeroLettini;
	private List<Ombrellone> ombrelloni;
	private Cliente cliente;
	private List<Servizio> servizi;
	
	public Prenotazione() {}
	
	public Prenotazione(int idPrenotazione, Date dataInizio, Date dataFine, int numeroLettini,
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
	
	public Date getDataInizio() {
		return dataInizio;
	}
	
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	
	public Date getDataFine() {
		return dataFine;
	}
	
	public void setDataFine(Date dataFine) {
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
