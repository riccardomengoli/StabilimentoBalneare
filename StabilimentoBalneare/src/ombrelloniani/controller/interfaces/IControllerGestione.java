package ombrelloniani.controller.interfaces;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import ombrelloniani.model.Prenotazione;

public interface IControllerGestione {
	
	public List<Prenotazione> cercaPrenotazioni(String nome, String cognome) throws ParseException;

	public List<Prenotazione> cercaPrenotazioni(Date dataInizio, Date dataFine);
	
	public List<Prenotazione> cercaPrenotazioni(Date data);
	
	public Prenotazione cercaPrenotazione(int idPrenotazione) throws ParseException;
	
}
