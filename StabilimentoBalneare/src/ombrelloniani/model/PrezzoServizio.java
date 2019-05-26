package ombrelloniani.model;

public class PrezzoServizio extends Prezzo {
	
	private Servizio servizio;

	public PrezzoServizio(float prezzo, Stagione stagione, Servizio servizio) {
		super(prezzo, stagione);
		this.servizio = servizio;
	}

	public Servizio getServizio() {
		return servizio;
	}

	public void setServizio(Servizio servizio) {
		this.servizio = servizio;
	}
}
