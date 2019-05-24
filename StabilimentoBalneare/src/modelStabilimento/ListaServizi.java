package modelStabilimento;

import java.util.List;

public class ListaServizi {
	
	private static ListaServizi myInstance = null;
	private List<Servizio> listServizi;
	
	private ListaServizi() {};
	
	public static synchronized ListaServizi getListaServizi() {
		if(myInstance == null) {
			myInstance = new ListaServizi();
		}
		return myInstance;
	}
	
	public void aggiornaServizi(List<Servizio> list) {
		this.listServizi = list;
	}
	
	public List<Servizio> getServizi() {
		return this.listServizi;
	}
}
