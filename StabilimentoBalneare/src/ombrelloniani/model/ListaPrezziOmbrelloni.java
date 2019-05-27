package ombrelloniani.model;

import java.util.List;

public class ListaPrezziOmbrelloni {
	private static ListaPrezziOmbrelloni myInstance = null;
	private List<PrezzoOmbrellone> listPrezziOmbrelloni;
	
	private ListaPrezziOmbrelloni() {};
	
	public static synchronized ListaPrezziOmbrelloni getListaPrezziOmbrelloni() {
		if(myInstance == null) {
			myInstance = new ListaPrezziOmbrelloni();
		}
		return myInstance;
	}
	
	public void aggiornaPrezziOmbrelloni(List<PrezzoOmbrellone> list) {
		this.listPrezziOmbrelloni = list;
	}
	
	public List<PrezzoOmbrellone> getPrezziOmbrelloni() {
		return this.listPrezziOmbrelloni;
	}
}
