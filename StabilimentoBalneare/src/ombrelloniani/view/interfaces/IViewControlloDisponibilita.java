package ombrelloniani.view.interfaces;

import java.time.LocalDate;

public interface IViewControlloDisponibilita {

	public LocalDate getDataInizio();

	public LocalDate getDataFine();

	/**
	 * Valore di ritorno formato da array di int di lunghezza 2. I campi sono i
	 * seguenti: 
	 * [0] = numeroRiga 
	 * [1] = numeroColonna
	 * 
	 */
	public int[] getOmbrelloneSelezionato();

	public void showError(String titolo, String descrizione);

}
