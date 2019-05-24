package modelStabilimento;

import java.util.List;

public class ListaFedelt� {
	
	private static ListaFedelt� myInstance = null;
	private List<Fedelt�> listaFedelt�;
	
	private ListaFedelt�() {};
	
	public static synchronized ListaFedelt� getListaFedelt�() {
		if(myInstance == null) {
			myInstance = new ListaFedelt�();
		}
		return myInstance;
	}
	
	public void aggiornaFedelt�(List<Fedelt�> list) {
		this.listaFedelt� = list;
	}
	
	public List<Fedelt�> getFedelt�() {
		return this.listaFedelt�;
	}
}
