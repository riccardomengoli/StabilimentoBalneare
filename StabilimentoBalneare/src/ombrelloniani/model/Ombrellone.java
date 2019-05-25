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
	
	public String getIdOmbrellone() {
		return idOmbrellone;
	}
	
	public int getNumeroRiga() {
		return numeroRiga;
	}

	public int getNumeroColonna() {
		return numeroColonna;
	}

}
