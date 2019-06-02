package ombrelloniani.controller.interfaces;

import java.time.LocalDate;
import java.util.List;

public interface IControllerDisponibilita {
	
	public List<int[]> getOmbrelloni();
	
	public List<int[]> mostraStatoSpiaggia(LocalDate dataInizio, LocalDate dataFine);
	
	public List<String[]> mostraStatoOmbrellone(LocalDate dataInizio, LocalDate dataFine, int numeroRiga, int numeroColonna);
	
}