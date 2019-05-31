package ombrelloniani.model;

import java.util.List;

public class Ricevuta {

	private double percentualeSconto;
	private double percentualeConvenzione;
	private float prezzoTotale;
	private List<EntryRicevuta> entries;
	
	public Ricevuta() {
		super();
	}
	
	public Ricevuta(double percentualeSconto, double percentualeConvenzione, float prezzoTotale,
			List<EntryRicevuta> entries) {
		super();
		this.percentualeSconto = percentualeSconto;
		this.percentualeConvenzione = percentualeConvenzione;
		this.prezzoTotale = prezzoTotale;
		this.entries = entries;
	}
	
	public double getPercentualeSconto() {
		return percentualeSconto;
	}

	public void setPercentualeSconto(double percentualeSconto) {
		this.percentualeSconto = percentualeSconto;
	}

	public double getPercentualeConvenzione() {
		return percentualeConvenzione;
	}

	public void setPercentualeConvenzione(double percentualeConvenzione) {
		this.percentualeConvenzione = percentualeConvenzione;
	}

	public float getPrezzoTotale() {
		return prezzoTotale;
	}

	public void setPrezzoTotale(float prezzoTotale) {
		this.prezzoTotale = prezzoTotale;
	}
	
	public void setPrezzoTotale() {
		float tot = 0;
		for(EntryRicevuta e : this.entries) {
			tot += e.getTotale();
		}
		this.prezzoTotale = tot;
	}

	public List<EntryRicevuta> getEntries() {
		return entries;
	}

	public void setEntries(List<EntryRicevuta> entries) {
		this.entries = entries;
	}


}
