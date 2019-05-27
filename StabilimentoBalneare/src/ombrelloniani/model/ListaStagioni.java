package ombrelloniani.model;

import java.util.Date;
import java.util.List;

public class ListaStagioni {
	
	private static ListaStagioni myInstance = null;
	private List<Stagione> listStagioni;
	
	private ListaStagioni() {};
	
	public static synchronized ListaStagioni getListaStagioni() {
		if(myInstance == null) {
			myInstance = new ListaStagioni();
		}
		return myInstance;
	}
	
	public void aggiornaStagioni(List<Stagione> list) {
		this.listStagioni = list;
	}
	
	public List<Stagione> getListStagioni() {
		return this.listStagioni;
	}
}
