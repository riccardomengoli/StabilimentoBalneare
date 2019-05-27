package ombrelloniani.model;

import java.util.Date;

public class Stagione {

	private String nomeStagione;
	private Date dataInizio;
	private Date dataFine;
	
	public Stagione(String nomeStagione, Date dataInizio, Date dataFine) {
		super();
		this.nomeStagione = nomeStagione;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
	}

	public String getNomeStagione() {
		return nomeStagione;
	}

	public void setNomeStagione(String nomeStagione) {
		this.nomeStagione = nomeStagione;
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

}
