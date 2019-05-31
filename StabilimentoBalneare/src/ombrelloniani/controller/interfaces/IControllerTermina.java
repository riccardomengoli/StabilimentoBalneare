package ombrelloniani.controller.interfaces;

import java.text.ParseException;

public interface IControllerTermina {

	public void terminaPrenotazione();
		
	public void cercaPrenotazione(int idPren) throws ParseException;
	
	public void prenotazioneSelezionata(int index) throws ParseException;
	
	public void cercaPrenotazioni(String nome, String cognome) throws ParseException;
	
	public void mostraConvenzioni();
	
	public void convenzioneSelezionata(int index) throws ParseException;
}
