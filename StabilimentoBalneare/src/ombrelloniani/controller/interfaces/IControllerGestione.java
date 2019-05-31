package ombrelloniani.controller.interfaces;

import java.text.ParseException;
import java.util.List;

import ombrelloniani.model.Prenotazione;

public interface IControllerGestione {
	
	public List<Prenotazione> cercaPrenotazioni(String nome, String cognome) throws ParseException;

	public Prenotazione cercaPrenotazione(int idPrenotazione) throws ParseException;
	
	public void aggiornaListaFedelta();
	
	public void aggiornaListaConvenzioni() throws ParseException;
	
	public void aggiornaListaPrezzi() throws ParseException;
	
	public void aggiornaListaStagioni() throws ParseException;
	
	
}
