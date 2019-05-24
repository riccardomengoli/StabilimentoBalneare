package modelStabilimento;

public class EnteTerzo {
	
	private String nomeEnte;
	private String tipologia;
	
	public EnteTerzo(String nomeEnte, String tipologia) {
		super();
		this.nomeEnte = nomeEnte;
		this.tipologia = tipologia;
	}

	public String getNomeEnte() {
		return nomeEnte;
	}

	public void setNomeEnte(String nomeEnte) {
		this.nomeEnte = nomeEnte;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

}