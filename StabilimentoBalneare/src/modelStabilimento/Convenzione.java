package modelStabilimento;

import java.time.LocalDate;

public class Convenzione {

	private String nome;
	private LocalDate dataInizio;
	private LocalDate dataFine;
	private double sconto;
	private EnteTerzo ente;
	
	public Convenzione(String nome, LocalDate dataInizio, LocalDate dataFine, double sconto) {
		super();
		this.nome = nome;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.sconto = sconto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public double getSconto() {
		return sconto;
	}

	public void setSconto(double sconto) {
		this.sconto = sconto;
	}

	public EnteTerzo getEnte() {
		return ente;
	}

	public void setEnte(EnteTerzo ente) {
		this.ente = ente;
	}
	
	
}
