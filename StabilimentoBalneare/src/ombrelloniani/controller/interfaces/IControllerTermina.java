package ombrelloniani.controller.interfaces;

public interface IControllerTermina {

	
	public void cercaPrenotazione(int idPren);
	public void cercaPrenotazioni(String nome, String cognome);
	public void prenotazioneSelezionata(int index);
	
	public void terminaPrenotazione();
	
	public void mostraConvenzioni();
	public void convenzioneSelezionata(int index);
}
