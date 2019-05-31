package ombrelloniani.model;

import java.util.Date;

public class Convenzione {

	private String nome;
	private Date dataInizio;
	private Date dataFine;
	private double sconto;
	private EnteTerzo ente;
	
	public Convenzione(String nome, Date dataInizio, Date dataFine, float sconto) {
		super();
		this.nome = nome;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.sconto = sconto;
	}

	public Convenzione() {
		super();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public double getSconto() {
		return sconto;
	}

	public void setSconto(double d) {
		this.sconto = d;
	}

	public EnteTerzo getEnte() {
		return ente;
	}

	public void setEnte(EnteTerzo ente) {
		this.ente = ente;
	}
	
	
}
