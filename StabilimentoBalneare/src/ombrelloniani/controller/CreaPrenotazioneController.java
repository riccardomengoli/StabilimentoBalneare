package ombrelloniani.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ombrelloniani.controller.interfaces.IController;
import ombrelloniani.controller.interfaces.IControllerCrea;
import ombrelloniani.model.Cliente;
import ombrelloniani.model.Ombrellone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CreaPrenotazioneController extends Controller implements IController,IControllerCrea{
	
	private Cliente cliente;
	private List<Ombrellone> ombrelloni = new ArrayList<Ombrellone>();
	
	static String createCliente = 
			"INSERT INTO CLIENTI " +
			"values (?,?,?,?,?)"
			;
	
	static String createPrenotazione = 
			"INSERT INTO PRENOTAZIONI(dataInizio,dataFine,numeroLettini,idCliente) " +
			"values (?,?,?,?)"
			;
	
	static String find_client = 
			"SELECT * FROM CLIENTI " +
			"WHERE documento = ?"
			;
	
	static String find_ombrellone = 
			"SELECT * FROM OMBRELLONI " +
			"WHERE idOmbrellone = ?"
			;
	
	static String aggiungiOmbrellonePrenotazione = 
			"INSERT INTO OMBRELLONIPRENOTAZIONE " +
				"values (?,?)"
			;
	
	static String get_idPrenotazione = 
			"SELECT idPrenotazione FROM PRENOTAZIONI ";
	
	static String check_ombrellone = 
			"SELECT OP.idOmbrellone " + 
			"FROM PRENOTAZIONI P JOIN OMBRELLONIPRENOTAZIONE OP " +
			"ON P.idPrenotazione = OP.idPrenotazione " +
			"WHERE ? BETWEEN P.dataInizio AND P.dataFine OR " +
			"? BETWEEN P.dataInizio AND P.dataFine OR " +
			"(P.dataInizio >= ? AND P.dataFine <= ?) "
			;
	
	static String eliminate_prenotazione = 
			"DELETE FROM PRENOTAZIONI " +
			"WHERE idPrenotazione = ? "
	;
		
	
	public CreaPrenotazioneController() { super();};
	
	private int getLastIdPrenotazione() {
		
		int result = 0;
		Connection connection = this.getConnection();
		try {
			
			PreparedStatement pstm = connection.prepareStatement(get_idPrenotazione);
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {
				result = rs.getInt("idPrenotazione");
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public Cliente cercaCliente(String idDocumento) {
		
		Connection connection = this.getConnection();
		Cliente result = null;
		try {
			
			PreparedStatement pstm = connection.prepareStatement(find_client);
			pstm.setString(1, idDocumento);
			ResultSet rs = pstm.executeQuery();
			
			if(rs.next()) {
				
				result = new Cliente();
				
				result.setNome(rs.getString("nome"));
				result.setCognome(rs.getString("cognome"));
				result.setEmail(rs.getString("email"));
				result.setTelefono(rs.getString("telefono"));
				result.setIdDocumento(rs.getString("documento"));
				
				this.cliente = result;
			}
			
			pstm.close();
			//connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<String> creaPrenotazioneNuovoCliente(String nome, String cognome, String email, String telefono,
			String documento, Date dataInizio, Date dataFine, int numeroLettini) {
		
		if(nome == null || cognome == null || (email == null && telefono == null) || documento == null || 
				dataInizio == null || dataFine == null || numeroLettini == 0 ) {
			
			System.out.print("Per creare la prenotazione � necessario riempire tutti i campi");
			return null; // null value solo a scopo di test
		}
		
		if(this.ombrelloni.size() == 0) {
			
			System.out.println("Per creare la prenotazione � necessario aggiungere almeno un ombrellone!");
			return null; // null value solo a scopo di test
		}
		
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Connection connection = this.getConnection();
		List<String> result = new ArrayList<String>();
		try {
			
			PreparedStatement pstm = connection.prepareStatement(check_ombrellone);
			pstm.setString(1, formatter.format(dataFine));
			pstm.setString(2, formatter.format(dataInizio));
			pstm.setString(3, formatter.format(dataInizio));
			pstm.setString(4, formatter.format(dataFine));
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {
				
				for(Ombrellone o: this.ombrelloni) {
					if(rs.getString("idOmbrellone").equals(o.getIdOmbrellone())) {
						result.add(o.getIdOmbrellone());
					}
				}
			}
			
			if(result.size() > 0) return result;
			
			pstm = connection.prepareStatement(createCliente);
			pstm.setString(1, documento);
			pstm.setString(2, nome);
			pstm.setString(3, cognome);
			if(email == null) email = ""; //per evitare eccezioni Null Pointer
			pstm.setString(4, email);
			if(telefono == null) telefono = "";
			pstm.setString(5, telefono);
			pstm.executeUpdate();
			
			pstm = connection.prepareStatement(createPrenotazione);
			pstm.setString(1, formatter.format(dataInizio));
			pstm.setString(2, formatter.format(dataFine));
			pstm.setInt(3, numeroLettini);
			pstm.setString(4, documento);
			pstm.executeUpdate();
			
			for(Ombrellone o: this.ombrelloni) {
				
				pstm = connection.prepareStatement(aggiungiOmbrellonePrenotazione);
				pstm.setInt(1,this.getLastIdPrenotazione());
				pstm.setString(2, o.getIdOmbrellone());
				pstm.executeUpdate();
			}
			
			pstm.close();
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	public List<String> creaPrenotazioneClienteEsistente(Date dataInizio, Date dataFine, int numeroLettini) {
		
		if(this.cliente == null) { System.out.println("Cliente non inserito");
			return null; } //in questo caso mandare una eccezzione inserire i dati del cliente nella view
		
		if( dataInizio == null || dataFine == null || numeroLettini == 0 ) {
			
			System.out.print("Per creare la prenotazione � necessario riempire tutti i campi");
			return null; // null value solo a scopo di test
		}
		
		if(this.ombrelloni.size() == 0) {
			
			System.out.println("Per creare la prenotazione � necessario aggiungere almeno un ombrellone!");
			return null; // null value solo a scopo di test
		}
		
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Connection connection = this.getConnection();
		List<String> result = new ArrayList<String>();
		
		try {
			
			PreparedStatement pstm = connection.prepareStatement(check_ombrellone);
			pstm.setString(1, formatter.format(dataFine));
			pstm.setString(2, formatter.format(dataInizio));
			pstm.setString(3, formatter.format(dataInizio));
			pstm.setString(4, formatter.format(dataFine));
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {
				
				for(Ombrellone o: this.ombrelloni) {
					if(rs.getString("idOmbrellone").equals(o.getIdOmbrellone())) {
						result.add(o.getIdOmbrellone());
					}
				}
			}
			
			if(result.size() > 0) return result;
			
			pstm = connection.prepareStatement(createPrenotazione);
			pstm.setString(1, formatter.format(dataInizio));
			pstm.setString(2, formatter.format(dataFine));
			pstm.setInt(3, numeroLettini);
			pstm.setString(4, this.cliente.getIdDocumento());
			pstm.executeUpdate();
			
			for(Ombrellone o: this.ombrelloni) {
				
				pstm = connection.prepareStatement(aggiungiOmbrellonePrenotazione);
				pstm.setInt(1,this.getLastIdPrenotazione());
				pstm.setString(2, o.getIdOmbrellone());
				pstm.executeUpdate();
			}
		
			pstm.close();
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	// il metodo restituisce -1 se l'ombrellone cercato non esiste
	public int aggiungiOmbrellone(String idOmbrellone) {
		
		int result = -1;
		Connection connection = this.getConnection();
		try {
			
			PreparedStatement pstm = connection.prepareStatement(find_ombrellone);
			pstm.setString(1, idOmbrellone);
			ResultSet rs = pstm.executeQuery();
			
			if(rs.next()) {
				
				result = 0;
				Ombrellone o = new Ombrellone();
				o.setIdOmbrellone(idOmbrellone);
				o.setNumeroColonna(rs.getInt("numeroColonna"));
				o.setNumeroRiga(rs.getInt("numeroRiga"));
				
				this.ombrelloni.add(o);
			}
			
			pstm.close();
			//connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	public void rimuoviOmbrellone(String idOmbrellone) {
		
		
		for(Ombrellone o: this.ombrelloni) {
			
			if(o.getIdOmbrellone().equals(idOmbrellone)) {
				
				this.ombrelloni.remove(o);
				break;
			}
		}
	}

}