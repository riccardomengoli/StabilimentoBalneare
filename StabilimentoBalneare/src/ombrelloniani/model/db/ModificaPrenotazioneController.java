package ombrelloniani.model.db;

import java.sql.Date;

import ombrelloniani.model.Prenotazione;

public class ModificaPrenotazioneController extends Controller {

	public Prenotazione modificaDataFine(Prenotazione prenotazione, Date valueOf) {
		
		Prenotazione newPrenotazione = prenotazione;
		newPrenotazione.setDataFine(valueOf);
		return newPrenotazione;
	}
}
