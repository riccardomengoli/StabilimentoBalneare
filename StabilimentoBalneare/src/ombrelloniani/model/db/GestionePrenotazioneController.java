package ombrelloniani.model.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ombrelloniani.model.Cliente;
import ombrelloniani.model.Prenotazione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GestionePrenotazioneController extends Controller {
		
	static String read_all_clients = 
			"SELECT * " +
			"FROM CLIENTI ";
	
	static String read_all_prenotazioni = 
			"SELECT * " +
			"FROM PRENOTAZIONI ";
	
	static String find_doc_by_name = 
			read_all_clients +
				"WHERE nome" + " = ? " +
				"AND cognome = ?";
	
	static String find_prenotazioni_by_id = 
			read_all_prenotazioni +
				"WHERE idPrenotazione = ?";
	
	static String join_function = 
			"SELECT * "+ 
			"FROM PRENOTAZIONI P JOIN CLIENTI C "+
			"ON P.idCliente = C.documento " +
			"WHERE nome = ? AND cognome = ?";
	
	static String join_prenotazioni = 
			"SELECT * "+ 
			"FROM PRENOTAZIONI P JOIN CLIENTI C "+
			"ON P.idCliente = C.documento " +
			"WHERE idPrenotazione = ?";
	
	static String join_prenotazioni_date_date = 
			"SELECT * " +
			"FROM PRENOTAZIONI P JOIN CLIENTI C " +
			"ON P.idCliente = C.documento " +
			"WHERE P.dataInizio >= ? " +
			"AND P.dataFine <= ?";
	
	static String join_prenotazioni_date = 
			"SELECT * " +
			"FROM PRENOTAZIONI P JOIN CLIENTI C " +
			"ON P.idCliente = C.documento " +
			"WHERE P.dataInizio <= ? " + 
			"AND P.dataFine >= ?";
	
	public GestionePrenotazioneController() { super();};
		
	
	public List<Prenotazione> cercaPrenotazioni(String nome, String cognome) throws ParseException {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Connection connection = this.getConnection();
		List<Prenotazione> result = new ArrayList<Prenotazione>();
		try {
			
			PreparedStatement pstm = connection.prepareStatement(join_function);
			pstm.setString(1, nome);
			pstm.setString(2, cognome);
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {
				
				Cliente c = new Cliente();
				Prenotazione p = new Prenotazione();
				p.setIdPrenotazione(rs.getInt("idPrenotazione"));
				p.setDataInizio(formatter.parse(rs.getString("dataInizio")));
				p.setDataFine(formatter.parse(rs.getString("dataFine")));
				p.setNumeroLettini(rs.getInt("numeroLettini"));
				
				c.setNome(rs.getString("nome"));
				c.setCognome(rs.getString("cognome"));
				c.setEmail(rs.getString("email"));
				c.setTelefono(rs.getString("telefono"));
				c.setIdDocumento(rs.getString("idCliente"));
				
				p.setCliente(c);
				result.add(p);
			}
			
			this.writeLog("Ricerca prenotazioni nome, cognome cliente");
			pstm.close();
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public List<Prenotazione> cercaPrenotazioni(Date dataInizio, Date dataFine) {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Connection connection = this.getConnection();
		List<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();
		
		try {
			
			PreparedStatement pstm = connection.prepareStatement(join_prenotazioni_date_date);
			pstm.setString(1, formatter.format(dataInizio));
			pstm.setString(2, formatter.format(dataFine));
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {	
			
				Cliente c = new Cliente();
				Prenotazione p = new Prenotazione();
				p = new Prenotazione();
				p.setIdPrenotazione(rs.getInt("idPrenotazione"));
				
				p.setDataInizio(formatter.parse(rs.getString("dataInizio")));
				p.setDataFine(formatter.parse(rs.getString("dataFine")));
				p.setNumeroLettini(rs.getInt("numeroLettini"));
				
				c.setNome(rs.getString("nome"));
				c.setCognome(rs.getString("cognome"));
				c.setEmail(rs.getString("email"));
				c.setTelefono(rs.getString("telefono"));
				c.setIdDocumento(rs.getString("idCliente"));
				
				p.setCliente(c);
				prenotazioni.add(p);
			
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		
		return prenotazioni;
	}
	
	public List<Prenotazione> cercaPrenotazioni(Date data) {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Connection connection = this.getConnection();
		List<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();
		
		try {
			
			PreparedStatement pstm = connection.prepareStatement(join_prenotazioni_date);
			pstm.setString(1, formatter.format(data));
			pstm.setString(2, formatter.format(data));
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {	
			
				Cliente c = new Cliente();
				Prenotazione p = new Prenotazione();
				p = new Prenotazione();
				p.setIdPrenotazione(rs.getInt("idPrenotazione"));
				
				p.setDataInizio(formatter.parse(rs.getString("dataInizio")));
				p.setDataFine(formatter.parse(rs.getString("dataFine")));
				p.setNumeroLettini(rs.getInt("numeroLettini"));
				
				c.setNome(rs.getString("nome"));
				c.setCognome(rs.getString("cognome"));
				c.setEmail(rs.getString("email"));
				c.setTelefono(rs.getString("telefono"));
				c.setIdDocumento(rs.getString("idCliente"));
				
				p.setCliente(c);
				prenotazioni.add(p);
			
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		
		return prenotazioni;
	}
	
	public Prenotazione cercaPrenotazione(int idPrenotazione) throws ParseException {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Connection connection = this.getConnection();
		Prenotazione p = null;
		try {
			
			PreparedStatement pstm = connection.prepareStatement(join_prenotazioni);
			pstm.setInt(1, idPrenotazione);
			ResultSet rs = pstm.executeQuery();
			
			if(rs != null) {	
			
				Cliente c = new Cliente();
				p = new Prenotazione();
				p.setIdPrenotazione(rs.getInt("idPrenotazione"));
				p.setDataInizio(formatter.parse(rs.getString("dataInizio")));
				p.setDataFine(formatter.parse(rs.getString("dataFine")));
				p.setNumeroLettini(rs.getInt("numeroLettini"));
				
				c.setNome(rs.getString("nome"));
				c.setCognome(rs.getString("cognome"));
				c.setEmail(rs.getString("email"));
				c.setTelefono(rs.getString("telefono"));
				c.setIdDocumento(rs.getString("idCliente"));
				
				p.setCliente(c);
			
			}
			
			
			this.writeLog("Ricerca prenotazioni per id");
			pstm.close();
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return p;
	}
	
}
