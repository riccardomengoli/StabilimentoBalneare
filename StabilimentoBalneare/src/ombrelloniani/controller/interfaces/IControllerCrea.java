package ombrelloniani.controller.interfaces;

import java.util.Date;
import java.util.List;

import ombrelloniani.model.Cliente;

public interface IControllerCrea {
	
	public Cliente cercaCliente(String idDocumento);
	
	public List<String> creaPrenotazioneNuovoCliente(String nome, String cognome, String email, String telefono,
			String documento, Date dataInizio, Date dataFine, int numeroLettini);
	
	public List<String> creaPrenotazioneClienteEsistente(Date dataInizio, Date dataFine, int numeroLettini);
	
	public int aggiungiOmbrellone(String idOmbrellone);
	
	public void rimuoviOmbrellone(String idOmbrellone);
}
