package modelStabilimento;

import java.util.List;

public class Ricevuta {

	private float percentualeSconto;
	private float percentualeConvenzione;
	private float prezzoTotale;
	private List<EntryRicevuta> entries;
	
	public Ricevuta(float percentualeSconto, float percentualeConvenzione, float prezzoTotale,
			List<EntryRicevuta> entries) {
		super();
		this.percentualeSconto = percentualeSconto;
		this.percentualeConvenzione = percentualeConvenzione;
		this.prezzoTotale = prezzoTotale;
		this.entries = entries;
	}
	
	public float getPercentualeSconto() {
		return percentualeSconto;
	}

	public void setPercentualeSconto(float percentualeSconto) {
		this.percentualeSconto = percentualeSconto;
	}

	public float getPercentualeConvenzione() {
		return percentualeConvenzione;
	}

	public void setPercentualeConvenzione(float percentualeConvenzione) {
		this.percentualeConvenzione = percentualeConvenzione;
	}

	public float getPrezzoTotale() {
		return prezzoTotale;
	}

	public void setPrezzoTotale(float prezzoTotale) {
		this.prezzoTotale = prezzoTotale;
	}

	public List<EntryRicevuta> getEntries() {
		return entries;
	}

	public void setEntries(List<EntryRicevuta> entries) {
		this.entries = entries;
	}

}
