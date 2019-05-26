package ombrelloniani.model.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
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
	// da vedere se server effettivamente
	private GestionePrenotazioneController controller = new GestionePrenotazioneController();
	private ModificaPrenotazioneController modController = new ModificaPrenotazioneController();
	
	//Query
	private static String convenzione = 
			"SELECT sconto FROM CONVENZIONI"
			+ "WHERE nome = ?";
	
	public void terminaPrenotazione() {
		this.prenotazioneTerminata = new PrenotazioneTerminata(prenotazione);
		
		if(prenotazione.getDataFine() != Date.valueOf(LocalDate.now())) {
			modController.modificaDataFine(prenotazione, Date.valueOf(LocalDate.now()));
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
		double sconto = 0;
		PreparedStatement pstm;
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
		
	}
}
