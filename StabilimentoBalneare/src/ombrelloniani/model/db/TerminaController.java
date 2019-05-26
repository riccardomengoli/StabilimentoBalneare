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
import java.util.List;

import ombrelloniani.model.ListaConvenzioni;
import ombrelloniani.model.ListaFedelta;
import ombrelloniani.model.ListaPrezzi;
import ombrelloniani.model.ListaServizi;
import ombrelloniani.model.Prenotazione;
import ombrelloniani.model.PrenotazioneTerminata;
import ombrelloniani.model.Ricevuta;

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
			"INSERT INTO PRENOTAZIONI_TERMINATE(idPrenotazione, dataInizio, dataFine, numeroLettini, idCliente, saldo)"
			+ "VALUES (?, ?, ?, ?, ?, ?)";
	
	public Ricevuta terminaPrenotazione() {
		this.prenotazioneTerminata = new PrenotazioneTerminata(prenotazione);
		
		if(prenotazione.getDataFine() != Date.valueOf(LocalDate.now())) {
			modController.modificaDataFine(prenotazione, Date.valueOf(LocalDate.now()));
		}
		
		ricevuta = creaRicevuta();
		prenotazioneTerminata.setSaldo(calcolaSaldo());
		
		//se vogliamo prima la ricevuta e poi l'aggiornamento va chiamata dopo direttamente dalla view
		aggiornaDataBase();
		controller.writeLog("prenotazione " + prenotazione.getIdPrenotazione()
				+ "terminata alle " + Date.valueOf(LocalDate.now()));
		
		return ricevuta;
	}
	
	public void aggiornaDataBase() {
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		int queryResult;
		Connection connection = controller.getConnection();
		PreparedStatement delete;
		PreparedStatement insert;
		
		try {
			delete = connection.prepareStatement(terminaPrenotazione);
			delete.setInt(1, prenotazione.getIdPrenotazione());
			queryResult = delete.executeUpdate();
			
			insert = connection.prepareStatement(aggiornaPrenotazioniTerminate);
			insert.setInt(1, prenotazioneTerminata.getIdPrenotazione());
			insert.setString(2, formatter.format(prenotazioneTerminata.getDataInizio()));
			insert.setString(3, formatter.format(prenotazioneTerminata.getDataFine()));
			insert.setInt(4, prenotazioneTerminata.getNumeroLettini());
			insert.setString(5, prenotazioneTerminata.getCliente().getIdDocumento());
			insert.setFloat(6, prenotazioneTerminata.getSaldo());
			queryResult = insert.executeUpdate();
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
	
	public double impostaConvenzione(String nome) {
		Connection connection = controller.getConnection();
		PreparedStatement pstm;
		double sconto = 0;
		try {
			pstm = connection.prepareStatement(convenzione);
			pstm.setString(1, nome);
			ResultSet rs = pstm.executeQuery();
			
			sconto = rs.getDouble("sconto");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sconto;
	}
	
	private void riempiCampiView() {
		
	}
	
	private Ricevuta creaRicevuta() {
		
	}
	
	private float calcolaSaldo() {
		float saldo;
		
		saldo = ricevuta.getPrezzoTotale() 
				- (ricevuta.getPrezzoTotale()*ricevuta.getPercentualeSconto()) 
				- (ricevuta.getPrezzoTotale()*ricevuta.getPercentualeConvenzione());
		
		return saldo;
		
	}
}
