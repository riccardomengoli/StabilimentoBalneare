package ombrelloniani.view.interfaces;

import java.time.LocalDate;
import java.util.List;

public interface IViewTermina {

	/**
	 * 'prenotazioni' formata da array di stringhe di lunghezza 3.
	 * I campi sono i seguenti:
	 * [0] = idPrenotazione
	 * [1] = dataInizio
	 * [2] = dataFine
	 * 
	 * @param prenotazioni
	 */
	public void aggiornaListaPrenotazioniDisponibili(List<String[]> prenotazioni);
	
	/**
	 * 'convenzioni' formata da array di stringhe di lunghezza 3.
	 * I campi sono i seguenti:
	 * string[0] = nomeEnte
	 * string[1] = nomeConvenzione
	 * string[2] = sconto
	 * 
	 * @param convenzioni
	 */
	public void aggiornaListaConvenzioni(List<String[]> convenzioni);
	
	/**
	 * 'ricevuta' formata da array di stringhe di lunghezza 3.
	 * I campi sono i seguenti:
	 * [0] = descrizione
	 * [1] = giorni
	 * [2] = euro
	 * Utilizzare come in interfaccia jpeg di progettazione.
	 * 
	 * @param ricevuta
	 */
	public void aggiornaTabellaRicevuta(List<String[]> ricevuta);
	
	public void addOmbrelloneToList(String idOmbrellone);
	public void addServizioToList(String nomeServizio);
	
	public void setDataInizio(LocalDate dataInizio);
	public void setDataFine(LocalDate dataFine);
	public void setNumeroLettini(int numeroLettini);
}
