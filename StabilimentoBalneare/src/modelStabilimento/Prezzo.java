package modelStabilimento;

public class Prezzo {
	
	private float prezzo;
	private Stagione stagione;
	
	public Prezzo(float prezzo, Stagione stagione) {
		super();
		this.prezzo = prezzo;
		this.stagione = stagione;
	}

	public float getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}

	public Stagione getStagione() {
		return stagione;
	}

	public void setStagione(Stagione stagione) {
		this.stagione = stagione;
	}
}
