package ombrelloniani.model;

import java.util.List;

public class ListaConvenzioni {
	
	private static ListaConvenzioni myInstance = null;
	private List<Convenzione> listConvenzioni;
	
	private ListaConvenzioni() {};
	
	public static synchronized ListaConvenzioni getListaConvenzioni() {
		if(myInstance == null) {
			myInstance = new ListaConvenzioni();
		}
		return myInstance;
	}
	
	public void aggiornaConvenzioni(List<Convenzione> list) {
		this.listConvenzioni = list;
	}
	
	public List<Convenzione> getConvenzioni() {
		return this.listConvenzioni;
	}
}
