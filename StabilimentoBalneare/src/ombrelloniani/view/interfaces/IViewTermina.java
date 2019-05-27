package ombrelloniani.view.interfaces;

import java.util.List;

public interface IViewTermina {

	void aggiornaListaPrenotazioniDisponibili(List<String[]> prenotazioni);

	void setId(String string);

	void setDataInizio(String format);

	void setDataFine(String format);

	void aggiornaTabellaRicevuta(List<String> ricevuta, int numeroEntry);

}
