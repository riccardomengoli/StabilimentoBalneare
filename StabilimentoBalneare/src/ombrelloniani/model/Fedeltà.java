package ombrelloniani.model;

public class Fedeltà {
	
	private String nome;
	private int giorni;
	private double sconto;

	public Fedeltà(String nome, int giorni, double sconto) {
		super();
		this.nome = nome;
		this.giorni = giorni;
		this.sconto = sconto;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public int getGiorni() {
		return giorni;
	}
	
	public void setGiorni(int giorni) {
		this.giorni = giorni;
	}
	
	public double getSconto() {
		return sconto;
	}
	
	public void setSconto(double sconto) {
		this.sconto = sconto;
	}

}
