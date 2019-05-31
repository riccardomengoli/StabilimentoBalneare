package ombrelloniani.view.interfaces;

import java.time.LocalDate;
import java.util.List;

public interface IViewTermina {

	public void aggiornaListaPrenotazioniDisponibili(List<String[]> prenotazioni);
	public void addOmbrelloneToList(String idOmbrellone);
	public void addServizioToList(String nomeServizio);
	public void aggiornaTabellaRicevuta(List<String> ricevuta, int numeroEntry);
	public void aggiornaListaConvenzioni(List<String[]> convenzioni);

	public String getOmbrelloneSelezionato();
	public String getServizioSelezionato();
	
	public void setDataInizio(LocalDate dataInizio);
	public void setDataFine(LocalDate dataFine);
	public void setNumeroLettini(int numeroLettini);
	
	/*
	public void aggiornaListaPrenotazioniDisponibili(List<String[]> prenotazioni);
	
	void setId(String string);

	void setDataInizio(String format);

	void setDataFine(String format);

	void aggiornaTabellaRicevuta(List<String> ricevuta, int numeroEntry);

	void setUtente(String nome, String cognome, String idDocumento);

	void aggiornaListaConvenzioni(List<String[]> convenzioni);

	void setNumeroLettini(String string);

	void setOmbrelloni(int size, List<String> ombrelloni);

	void setServizi(int size, List<String> servizi);
	*/
}
