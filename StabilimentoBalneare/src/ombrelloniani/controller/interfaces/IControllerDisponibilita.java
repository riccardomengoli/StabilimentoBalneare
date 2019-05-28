package ombrelloniani.controller.interfaces;

import java.util.Date;
import java.util.List;

import ombrelloniani.model.Ombrellone;
import ombrelloniani.model.Prenotazione;

public interface IControllerDisponibilita {
	
	public List<Ombrellone> getOmbrelloni();
	
	public List<String> mostraStatoSpiaggia(Date dataInizio, Date dataFine);
	
	public List<Prenotazione> mostraStatoOmbrellone(Date dataInizio, Date dataFine, String idOmbrellone);
	
}
