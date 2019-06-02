package ombrelloniani.view.interfaces;

import java.time.LocalDate;

public interface IViewDisponibilita {

	/**
	 * Restituisce la data inizio di ricerca inserita.
	 * Se vuota o non valida restituisce null.
	 */
	public LocalDate getDataInizio();

	/**
	 * Restituisce la data fine di ricerca inserita.
	 * Se vuota o non valida restituisce null.
	 */
	public LocalDate getDataFine();

	/**
	 * Restituisce l'ombrellone selezionato sulla mappa.
	 * Valore di ritorno formato da array di int di lunghezza 2. I campi sono i
	 * seguenti: 
	 * [0] = numeroRiga 
	 * [1] = numeroColonna
	 * 
	 */
	public int[] getOmbrelloneSelezionato();

	/**
	 * Mostra un popup di errore con titolo e descrizione.
	 */
	public void showError(String titolo, String descrizione);

}
