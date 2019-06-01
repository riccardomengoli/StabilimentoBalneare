package ombrelloniani.controller.interfaces;

public interface IControllerTermina {

	public void terminaPrenotazione();
		
	public void cercaPrenotazione(int idPren);
	
	public void prenotazioneSelezionata(int index);
	
	public void cercaPrenotazioni(String nome, String cognome);
	
	public void mostraConvenzioni();
	
	public void convenzioneSelezionata(int index);
}
