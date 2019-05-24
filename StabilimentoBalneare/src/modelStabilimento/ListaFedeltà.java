package modelStabilimento;

import java.util.List;

public class ListaFedeltà {
	
	private static ListaFedeltà myInstance = null;
	private List<Fedeltà> listaFedeltà;
	
	private ListaFedeltà() {};
	
	public static synchronized ListaFedeltà getListaFedeltà() {
		if(myInstance == null) {
			myInstance = new ListaFedeltà();
		}
		return myInstance;
	}
	
	public void aggiornaFedeltà(List<Fedeltà> list) {
		this.listaFedeltà = list;
	}
	
	public List<Fedeltà> getFedeltà() {
		return this.listaFedeltà;
	}
}
