package ombrelloniani.model;

import java.util.Date;
import java.util.List;

public class PrenotazioneTerminata extends Prenotazione {
	
	private float saldo;

	public PrenotazioneTerminata() {
		super();
	}

	public PrenotazioneTerminata(Prenotazione prenotazione) {
		super(prenotazione.getIdPrenotazione(), prenotazione.getDataInizio(), prenotazione.getDataFine(), 
				prenotazione.getNumeroLettini(), prenotazione.getOmbrelloni(), prenotazione.getCliente(), prenotazione.getServizi());
	}
	
	public PrenotazioneTerminata(int idPrenotazione, Date dataInizio, Date dataFine, int numeroLettini,
			List<Ombrellone> ombrelloni, Cliente cliente, List<Servizio> servizi, float saldo) {
		
		super(idPrenotazione, dataInizio, dataFine, numeroLettini, ombrelloni, cliente, servizi);
		this.saldo = saldo;
	
	}

	public float getSaldo() {
		return saldo;
	}

	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}
}
