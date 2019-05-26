package ombrelloniani.model.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ombrelloniani.model.EntryRicevuta;
import ombrelloniani.model.Fedelta;
import ombrelloniani.model.ListaConvenzioni;
import ombrelloniani.model.ListaFedelta;
import ombrelloniani.model.ListaPrezzi;
import ombrelloniani.model.ListaServizi;
import ombrelloniani.model.Ombrellone;
import ombrelloniani.model.Prenotazione;
import ombrelloniani.model.PrenotazioneTerminata;
import ombrelloniani.model.Ricevuta;
import ombrelloniani.model.Servizio;

public class TerminaController {
	
	private ListaPrezzi prezzi;
	private ListaServizi servizi;
	private ListaConvenzioni convenzioni;
	private ListaFedelta fedelta;
	private Ricevuta ricevuta;
	private PrenotazioneTerminata prenotazioneTerminata;
	private Prenotazione prenotazione;
	private List<Prenotazione> prenotazioni;
	//Controller
	private GestionePrenotazioneController controller = new GestionePrenotazioneController();
	private ModificaPrenotazioneController modController = new ModificaPrenotazioneController();
	
	//Query
	private static String convenzione = 
			"SELECT sconto FROM CONVENZIONI"
			+ "WHERE nome = ?";
	
	private static String terminaPrenotazione = 
			"DELETE FROM PRENOTAZIONI "
			+ "WHERE idPrenotazione = ?";
	
	private static String aggiornaPrenotazioniTerminate = 
			"INSERT INTO PRENOTAZIONI_TERMINATE(idPrenotazione, dataInizio, dataFine, numeroLettini, idCliente, saldo, giorni)"
			+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private static String giorniTotaliSoggiorno =
			"SELECT SUM(giorni)"
			+ "FROM PRENOTAZIONI_TERMINATE"
			+ "WHERE idCliente = ?";

	public void terminaPrenotazione() {
		this.prenotazioneTerminata = new PrenotazioneTerminata(prenotazione);
		
		if(prenotazione.getDataFine() != Date.valueOf(LocalDate.now())) {
			modController.modificaDataFine(prenotazione, Date.valueOf(LocalDate.now()));
		}
		
		ricevuta = creaRicevuta();
		prenotazioneTerminata.setSaldo(calcolaSaldo());
		
		mostraRicevuta();
		
		aggiornaDataBase();
		controller.writeLog("prenotazione " + prenotazione.getIdPrenotazione()
				+ "terminata alle " + Date.valueOf(LocalDate.now()));
	}
	
	public void aggiornaDataBase() {
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Connection connection = controller.getConnection();
		PreparedStatement delete;
		PreparedStatement insert;
		
		try {
			delete = connection.prepareStatement(terminaPrenotazione);
			delete.setInt(1, prenotazione.getIdPrenotazione());
			delete.executeUpdate();
			
			insert = connection.prepareStatement(aggiornaPrenotazioniTerminate);
			insert.setInt(1, prenotazioneTerminata.getIdPrenotazione());
			insert.setString(2, formatter.format(prenotazioneTerminata.getDataInizio()));
			insert.setString(3, formatter.format(prenotazioneTerminata.getDataFine()));
			insert.setInt(4, prenotazioneTerminata.getNumeroLettini());
			insert.setString(5, prenotazioneTerminata.getCliente().getIdDocumento());
			insert.setFloat(6, prenotazioneTerminata.getSaldo());
			insert.setDouble(7, prenotazioneTerminata.getGiorni());
			insert.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public Prenotazione cercaPrenotazione(int idPren) throws ParseException {
		return controller.cercaPrenotazione(idPren);
	}
	
	public List<Prenotazione> cercaPrenotazione(String nome, String cognome) throws ParseException{
		return controller.cercaPrenotazioni(nome, cognome);
	}
	
	public Prenotazione prenotazioneSelezionata(int index) {
		return prenotazioni.get(index);
	}
	
	public float impostaConvenzione(String nome) {
		Connection connection = controller.getConnection();
		PreparedStatement pstm;
		float sconto = 0;
		try {
			pstm = connection.prepareStatement(convenzione);
			pstm.setString(1, nome);
			ResultSet rs = pstm.executeQuery();
			
			sconto = rs.getFloat("sconto");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return sconto;
	}
	
	private float calcolaSaldo() {
		float saldo;
		
		saldo = ricevuta.getPrezzoTotale() 
				- (ricevuta.getPrezzoTotale()*ricevuta.getPercentualeSconto()) 
				- (ricevuta.getPrezzoTotale()*ricevuta.getPercentualeConvenzione());
		
		return saldo;
		
	}


	private void mostraRicevuta() {
		//mostrare la ricevuta sulla view
	}
	
	private Ricevuta creaRicevuta() {
		ricevuta = new Ricevuta();
		
		ricevuta.setEntries(calcolaEntryRicevuta());
		ricevuta.setPercentualeSconto(calcolaSconto());
		//String conv = ViewTermina.scegliConvenzione(convenzioni);
		ricevuta.setPercentualeConvenzione(impostaConvenzione("nomeConvenzione"));
		ricevuta.setPrezzoTotale();
		return ricevuta;
	}

	private float calcolaSconto() {
		float sconto = 0;
		double giorni = 0;
		Connection connection = controller.getConnection();
		PreparedStatement pstm;
		
		try {
			//da chiedere conferma se va bene cosi per una SUM
			pstm = connection.prepareStatement(giorniTotaliSoggiorno);
			pstm.setString(1, prenotazioneTerminata.getCliente().getIdDocumento());
			ResultSet rs = pstm.executeQuery();
			giorni = rs.getDouble(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(giorni == 0)
			return sconto;
		else {
			for(Fedelta f : fedelta.getFedelta()) {
				if(giorni >= f.getGiorni()) {
					if(f.getSconto() > sconto) {
						sconto = f.getSconto();
					}
				}
			}
		}
		
		return sconto;
	}

	/*
	* private String tipoPrezzo;
	* private String descrizione;
	* private int giorni;
	* private float totale;
	*/
	
	private List<EntryRicevuta> calcolaEntryRicevuta() {
		List<EntryRicevuta> entries = new ArrayList();
		EntryRicevuta entry = new EntryRicevuta();
		for(Ombrellone o : prenotazioneTerminata.getOmbrelloni()) {
			
		}
		
		for(Servizio s : prenotazioneTerminata.getServizi()) {
			
		}
		
		return entries;
	}
	
	
}
