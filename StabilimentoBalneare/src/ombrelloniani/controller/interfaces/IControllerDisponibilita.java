package ombrelloniani.controller.interfaces;

import java.util.Date;
import java.util.List;

public interface IControllerDisponibilita {
	
	public List<Integer[]> getOmbrelloni();
	
	public List<Integer[]> mostraStatoSpiaggia(Date dataInizio, Date dataFine);
	
	public List<String[]> mostraStatoOmbrellone(Date dataInizio, Date dataFine, int numeroRiga, int numeroColonna);
	
}
