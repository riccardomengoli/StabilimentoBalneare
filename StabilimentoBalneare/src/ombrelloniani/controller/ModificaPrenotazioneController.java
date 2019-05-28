package ombrelloniani.controller;

import java.util.Date;

import ombrelloniani.model.Prenotazione;

public class ModificaPrenotazioneController extends Controller {

	public Prenotazione modificaDataFine(Prenotazione prenotazione, Date date) {
		
		Prenotazione newPrenotazione = prenotazione;
		newPrenotazione.setDataFine(date);
		return newPrenotazione;
	}
}
