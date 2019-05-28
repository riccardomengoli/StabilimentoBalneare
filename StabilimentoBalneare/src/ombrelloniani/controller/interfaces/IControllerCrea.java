package ombrelloniani.controller.interfaces;

import java.util.Date;

import ombrelloniani.model.Cliente;

public interface IControllerCrea {
	
	public int getLastIdPrenotazione();
	
	public Cliente cercaCliente(String idDocumento);
	
	public String creaPrenotazioneNuovoCliente(String nome, String cognome, String email, String telefono,
			String documento, Date dataInizio, Date dataFine, int numeroLettini);
	
	public String creaPrenotazioneClienteEsistente(Date dataInizio, Date dataFine, int numeroLettini);
	
	public int aggiungiOmbrellone(String idOmbrellone);
	
	public void rimuoviOmbrellone(String idOmbrellone);
}
