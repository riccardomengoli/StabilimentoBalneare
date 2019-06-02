package ombrelloniani.controller.interfaces;

import java.time.LocalDate;
import java.util.List;

public interface IControllerDisponibilita {

	/**
	 * Restituisce per ogni ombrellone un array con:
	 * [0] numeroRiga
	 * [1] numeroColonna
	 */
	public List<int[]> getOmbrelloni();

	/**
	 * Restituisce ombrelloni occupati indicando il loro:
	 * [0] numeroRiga
	 * [1] numeroColonna
	 */
	public List<int[]> mostraStatoSpiaggia(LocalDate dataInizio, LocalDate dataFine);

	
	/**
	 * Restituisce la prima prenotazione associata all'ombrellone ricercato per le date in ingresso.
	 * I valori di ritorno sono:
	 * [0] = idOmbrellone
	 * [1] = idPrenotazione
	 * [2] = dataInizio
	 * [3] = dataFine
	 * [4] = nome
	 * [5] = cognome
	 */
	public List<String[]> mostraStatoOmbrellone(LocalDate dataInizio, LocalDate dataFine, int numeroRiga,
			int numeroColonna);

}