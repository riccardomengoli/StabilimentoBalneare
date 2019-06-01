package ombrelloniani.controller.interfaces;

import java.util.List;

import ombrelloniani.model.Prenotazione;

public interface IControllerGestione {
	
	public List<Prenotazione> cercaPrenotazioni(String nome, String cognome);

	public Prenotazione cercaPrenotazione(int idPrenotazione);
	
	public void aggiornaListaFedelta();
	
	public void aggiornaListaConvenzioni();
	
	public void aggiornaListaPrezzi();
	
	public void aggiornaListaStagioni();
}
