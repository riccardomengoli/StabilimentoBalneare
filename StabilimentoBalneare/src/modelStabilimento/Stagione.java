package modelStabilimento;

import java.time.LocalDate;

public class Stagione {

	private String nomeStagione;
	private LocalDate dataInizio;
	private LocalDate dataFine;
	
	public Stagione(String nomeStagione, LocalDate dataInizio, LocalDate dataFine) {
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

}
