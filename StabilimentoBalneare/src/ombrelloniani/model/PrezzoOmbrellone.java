package ombrelloniani.model;

import ombrelloniani.model.Prezzo;
import ombrelloniani.model.Stagione;

public class PrezzoOmbrellone extends Prezzo {
	
	private int filaInizio;
	private int filaFine;
	
	public PrezzoOmbrellone(float prezzo, Stagione stagione, int filaInizio, int filaFine) {
		super(prezzo, stagione);
		this.filaInizio = filaInizio;
		this.filaFine = filaFine;
	}

	public PrezzoOmbrellone() {
		super();
	}

	public int getFilaInizio() {
		return filaInizio;
	}

	public void setFilaInizio(int filaInizio) {
		this.filaInizio = filaInizio;
	}

	public int getFilaFine() {
		return filaFine;
	}

	public void setFilaFine(int filaFine) {
		this.filaFine = filaFine;
	}
	
	

}
