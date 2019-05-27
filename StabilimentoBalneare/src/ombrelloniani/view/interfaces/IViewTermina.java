package ombrelloniani.view.interfaces;

import java.util.List;

public interface IViewTermina {

	void aggiornaListaPrenotazioniDisponibili(List<String[]> prenotazioni);

	void setId(String string);

	void setDataInizio(String format);

	void setDataFine(String format);

	void aggiornaTabellaRicevuta(List<String> ricevuta, int numeroEntry);

	void setUtente(String nome, String cognome, String idDocumento);

	void aggiornaListaConvenzioni(List<String[]> convenzioni);

	void setNumeroLettini(String string);

	void setOmbrelloni(int size, List<String> ombrelloni);

	void setServizi(int size, List<String> servizi);

}
