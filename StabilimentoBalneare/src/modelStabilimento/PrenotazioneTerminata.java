package modelStabilimento;

import java.time.LocalDate;
import java.util.List;

public class PrenotazioneTerminata extends Prenotazione {
	
	private float saldo;

	public PrenotazioneTerminata() {
		super();
	}

	public PrenotazioneTerminata(int idPrenotazione, LocalDate dataInizio, LocalDate dataFine, int numeroLettini,
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
