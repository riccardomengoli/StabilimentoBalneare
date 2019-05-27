package ombrelloniani.model;

import java.util.List;

public class ListaPrezziServizi {
	private static ListaPrezziServizi myInstance = null;
	private List<PrezzoServizio> listPrezziServizi;
	
	private ListaPrezziServizi() {};
	
	public static synchronized ListaPrezziServizi getListaPrezziServizi() {
		if(myInstance == null) {
			myInstance = new ListaPrezziServizi();
		}
		return myInstance;
	}
	
	public void aggiornaPrezziServizi(List<PrezzoServizio> list) {
		this.listPrezziServizi = list;
	}
	
	public List<PrezzoServizio> getPrezziServizi() {
		return this.listPrezziServizi;
	}
}
