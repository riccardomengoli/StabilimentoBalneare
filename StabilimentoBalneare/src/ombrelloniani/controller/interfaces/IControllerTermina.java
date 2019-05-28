package ombrelloniani.controller.interfaces;

import java.text.ParseException;

public interface IControllerTermina {

	public void terminaPrenotazione();
	
	public void aggiornaDataBase();
	
	public void cercaPrenotazione(int idPren) throws ParseException;
	
	public void prenotazioneSelezionata(int index);
	
	public void cercaPrenotazioni(String nome, String cognome) throws ParseException;
	
	public void cercaConvenzioni();
	
	public void convenzioneSelezionata(int index);
}
