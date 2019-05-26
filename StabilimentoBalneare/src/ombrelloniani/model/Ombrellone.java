package ombrelloniani.model;

public class Ombrellone {
	
	private String idOmbrellone;
	private int numeroRiga;
	private int numeroColonna;
	
	public Ombrellone(String idOmbrellone, int numeroRiga, int numeroColonna) {
		super();
		this.idOmbrellone = idOmbrellone;
		this.numeroRiga = numeroRiga;
		this.numeroColonna = numeroColonna;
	}
	
	public Ombrellone() {
	}

	public String getIdOmbrellone() {
		return idOmbrellone;
	}
	
	public int getNumeroRiga() {
		return numeroRiga;
	}

	public int getNumeroColonna() {
		return numeroColonna;
	}

	public void setIdOmbrellone(String idOmbrellone) {
		this.idOmbrellone = idOmbrellone;
	}
	
	public void setNumeroRiga(int numeroRiga) {
		this.numeroRiga = numeroRiga;
	}
	
	public void setNumeroColonna(int numeroColonna) {
		this.numeroColonna = numeroColonna;
	}

}
