package ombrelloniani.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;

public class PrenotazioneTerminata extends Prenotazione {
	
	private float saldo;
	private double giorni;

	public PrenotazioneTerminata() {
		super();
		setGiorni();
	}

	public PrenotazioneTerminata(Prenotazione prenotazione) {
		super(prenotazione.getIdPrenotazione(), prenotazione.getDataInizio(), prenotazione.getDataFine(), 
				prenotazione.getNumeroLettini(), prenotazione.getOmbrelloni(), prenotazione.getCliente(), prenotazione.getServizi());
		setGiorni();
	}
	
	public PrenotazioneTerminata(int idPrenotazione, Date dataInizio, Date dataFine, int numeroLettini,
			List<Ombrellone> ombrelloni, Cliente cliente, List<Servizio> servizi, float saldo) {
		
		super(idPrenotazione, dataInizio, dataFine, numeroLettini, ombrelloni, cliente, servizi);
		this.saldo = saldo;
		setGiorni();
	}

	public float getSaldo() {
		return saldo;
	}

	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}

	private void setGiorni() {
		LocalDate dataInizio = 
				LocalDate.of(this.getDataInizio().getYear(), this.getDataInizio().getMonth(), this.getDataInizio().getDay());
		LocalDate dataFine = 
				LocalDate.of(this.getDataFine().getYear(), this.getDataFine().getMonth(), this.getDataFine().getDay());
		Period giorni = Period.between(dataInizio, dataFine);
		this.giorni = giorni.getDays();
		
	}
	
	public double getGiorni() {
		return this.giorni;
	}
}
