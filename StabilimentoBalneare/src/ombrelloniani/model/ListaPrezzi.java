package ombrelloniani.model;

import java.util.List;

public class ListaPrezzi {
	
	private static ListaPrezzi myInstance = null;
	private List<Prezzo> listPrezzi;
	
	private ListaPrezzi() {};
	
	public static synchronized ListaPrezzi getListaPrezzi() {
		if(myInstance == null) {
			myInstance = new ListaPrezzi();
		}
		return myInstance;
	}
	
	public void aggiornaPrezzi(List<Prezzo> list) {
		this.listPrezzi = list;
	}
	
	public List<Prezzo> getPrezzi() {
		return this.listPrezzi;
	}
}
