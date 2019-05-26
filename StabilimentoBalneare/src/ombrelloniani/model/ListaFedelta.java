package ombrelloniani.model;

import java.util.List;

public class ListaFedelta {
	
	private static ListaFedelta myInstance = null;
	private List<Fedelta> listaFedelta;
	
	private ListaFedelta() {};
	
	public static synchronized ListaFedelta getListaFedelta() {
		if(myInstance == null) {
			myInstance = new ListaFedelta();
		}
		return myInstance;
	}
	
	public void aggiornaFedelta(List<Fedelta> list) {
		this.listaFedelta = list;
	}
	
	public List<Fedelta> getFedelta() {
		return this.listaFedelta;
	}
}
