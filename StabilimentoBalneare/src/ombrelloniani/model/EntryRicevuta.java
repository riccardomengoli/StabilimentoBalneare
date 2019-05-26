package ombrelloniani.model;

public class EntryRicevuta {
	
	private String tipoPrezzo;
	private String descrizione;
	private int giorni;
	private float totale;
	
	public EntryRicevuta(String tipoPrezzo, String descrizione, int giorni, float totale) {
		super();
		this.tipoPrezzo = tipoPrezzo;
		this.descrizione = descrizione;
		this.giorni = giorni;
		this.totale = totale;
	}

	public EntryRicevuta() {
		super();
	}

	public String getTipoPrezzo() {
		return tipoPrezzo;
	}

	public void setTipoPrezzo(String tipoPrezzo) {
		this.tipoPrezzo = tipoPrezzo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getGiorni() {
		return giorni;
	}

	public void setGiorni(int giorni) {
		this.giorni = giorni;
	}

	public float getTotale() {
		return totale;
	}

	public void setTotale(float totale) {
		this.totale = totale;
	}
}
