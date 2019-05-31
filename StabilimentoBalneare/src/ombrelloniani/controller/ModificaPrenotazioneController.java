package ombrelloniani.controller;

import java.util.Date;

import ombrelloniani.controller.interfaces.IController;
import ombrelloniani.controller.interfaces.IControllerModifica;
import ombrelloniani.model.Prenotazione;

public class ModificaPrenotazioneController implements IController,IControllerModifica {

	public Prenotazione modificaDataFine(Prenotazione prenotazione, Date date) {
		
		Prenotazione newPrenotazione = prenotazione;
		newPrenotazione.setDataFine(date);
		return newPrenotazione;
	}
}
