package ombrelloniani.model.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ombrelloniani.model.Ombrellone;
import ombrelloniani.model.Prenotazione;



public class DisponibilitaController {

	private GestionePrenotazioneController controllerGestione = new GestionePrenotazioneController();
	
	static String get_ombrelloni = 
			"SELECT * FROM OMBRELLONI";
	
	static String check_ombrellone = 
			"SELECT OP.idOmbrellone " + 
			"FROM PRENOTAZIONI P JOIN OMBRELLONIPRENOTAZIONE OP " +
			"ON P.idPrenotazione = OP.idPrenotazione " +
			"WHERE P.dataInizio <= ? " +
			"AND P.dataFine >= ? "
			;
	
	static String stato_ombrellone = 
			"SELECT * " + 
			"FROM PRENOTAZIONI P JOIN OMBRELLONIPRENOTAZIONE OP " +
			"ON P.idPrenotazione = OP.idPrenotazione " +
			"WHERE P.dataInizio <= ? " +
			"AND P.dataFine >= ? " +
			"AND OP.idOmbrellone = ? "
			;
	
	public List<Ombrellone> getOmbrelloni() {
		
		List<Ombrellone> result = new ArrayList<Ombrellone>();
		Connection connection = controllerGestione.getConnection();
		
		try {
			
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(get_ombrelloni);
			
			while(rs.next()) {
				
				Ombrellone o = new Ombrellone();
				o.setIdOmbrellone(rs.getString("idOmbrellone"));
				o.setNumeroColonna(rs.getInt("numeroColonna"));
				o.setNumeroRiga(rs.getInt("numeroRiga"));
				result.add(o);
			}
			
			stm.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public List<String> mostraStatoSpiaggia(Date dataInizio, Date dataFine) {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		List<String> result = new ArrayList<String>();
		Connection connection = controllerGestione.getConnection();
		
		try {
			
			PreparedStatement stm = connection.prepareStatement(check_ombrellone);
			stm.setString(1, formatter.format(dataInizio));
			stm.setString(2, formatter.format(dataFine));
			ResultSet rs = stm.executeQuery();
			
			while(rs.next()) {
				
				result.add(rs.getString("idOmbrellone"));
			}
			
			stm.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public List<Prenotazione> mostraStatoOmbrellone(Date dataInizio, Date dataFine, String idOmbrellone) {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		List<Prenotazione> result = new ArrayList<Prenotazione>();
		Connection connection = controllerGestione.getConnection();
		
		try {
			
			PreparedStatement stm = connection.prepareStatement(check_ombrellone);
			stm.setString(1, formatter.format(dataInizio));
			stm.setString(2, formatter.format(dataFine));
			stm.setString(3, idOmbrellone);
			ResultSet rs = stm.executeQuery();
			
			while(rs.next()) {
				
				Prenotazione p = new Prenotazione();
				p.setIdPrenotazione(rs.getInt("idPrenotazione"));
				p.setDataInizio(formatter.parse(rs.getString("dataInizio")));
				p.setDataFine(formatter.parse(rs.getString("dataFine")));
				p.setNumeroLettini(rs.getInt("numeroLettini"));
				result.add(p);
			}
			
			stm.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}
}