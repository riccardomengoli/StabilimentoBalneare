package ombrelloniani.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ombrelloniani.controller.interfaces.IController;
import ombrelloniani.controller.interfaces.IControllerCrea;
import ombrelloniani.model.Cliente;
import ombrelloniani.model.Ombrellone;
import ombrelloniani.view.interfaces.IViewCreazione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CreaPrenotazioneController extends Controller implements IController,IControllerCrea{
	
	private Cliente cliente;
	private List<Ombrellone> ombrelloni = new ArrayList<Ombrellone>();
	private IViewCreazione viewCreazione;
	
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
			"WHERE lower(documento) = ?"
			;
	
	static String find_ombrellone = 
			"SELECT * FROM OMBRELLONI " +
			"WHERE lower(idOmbrellone) = ?"
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
	
	static String updateCliente = 
			"UPDATE CLIENTI " +
			"SET nome = ?, " +
			"cognome = ?, " +
			"email = ?, " +
			"telefono = ? " +
			"WHERE documento = ? "
	;
		
	public CreaPrenotazioneController(IViewCreazione viewCreazione) {
		this.viewCreazione = viewCreazione;
	}
	
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
	
	//se inserisco carta di identit� registrata e provo a salvare un nuovo cliente
	private boolean verificaCliente() {
		
		String idDocumento = viewCreazione.getIdDocumento();
		Connection connection = this.getConnection();
		boolean result = false;
		try {
			
			PreparedStatement pstm = connection.prepareStatement(find_client);
			pstm.setString(1, idDocumento.toLowerCase());
			ResultSet rs = pstm.executeQuery();
			
			if(rs.next()) result = true;
			
			pstm.close();
			//connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void cercaCliente() {
		
		String idDocumento = viewCreazione.getIdDocumento();
		Connection connection = this.getConnection();
		Cliente result = null;
		try {
			
			PreparedStatement pstm = connection.prepareStatement(find_client);
			pstm.setString(1, idDocumento.toLowerCase());
			ResultSet rs = pstm.executeQuery();
			
			if(rs.next()) {
				
				result = new Cliente();
				
				result.setNome(rs.getString("nome"));
				result.setCognome(rs.getString("cognome"));
				result.setEmail(rs.getString("email"));
				result.setTelefono(rs.getString("telefono"));
				result.setIdDocumento(rs.getString("documento"));
				
				this.cliente = result; //salvo il cliente temporaneamente
				
				//vado a riempire i campi della view 
				this.viewCreazione.setNome(cliente.getNome());
				this.viewCreazione.setCognome(cliente.getCognome());
				this.viewCreazione.setTelefono(cliente.getTelefono());
				this.viewCreazione.setEmail(cliente.getEmail());
			}
			
			else this.viewCreazione.showError("Cliente non trovato",
					"Non � stato trovato alcun cliente associato al documento: " + idDocumento.toUpperCase());
			
			pstm.close();
			//connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void creaPrenotazione() {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dataInizio = Date.from(viewCreazione.getDataInizio().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date dataFine = Date.from(viewCreazione.getDataFine().atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		if(dataInizio.compareTo(dataFine) > 0) {
			viewCreazione.showError("ErroreDate", "La data di fine deve essere successiva alla data di inizio");
			return;
		}
		
		Connection connection = this.getConnection();
		List<String> ombrelloniOccupati = new ArrayList<String>();
		
		try {
			
			if(viewCreazione.getNumeroLettini() > 4*this.ombrelloni.size()) {
				viewCreazione.showError("Troppi lettini", "Per ogni ombrellone si possono avere al pi� 4 lettini");
				return;
			}
			
			PreparedStatement pstm = connection.prepareStatement(check_ombrellone);
			pstm.setString(1, formatter.format(dataFine));
			pstm.setString(2, formatter.format(dataInizio));
			pstm.setString(3, formatter.format(dataInizio));
			pstm.setString(4, formatter.format(dataFine));
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {
				
				for(Ombrellone o: this.ombrelloni) {
					if(rs.getString("idOmbrellone").equals(o.getIdOmbrellone())) {
						ombrelloniOccupati.add(o.getIdOmbrellone());
					}
				}
			}
			
			if(ombrelloniOccupati.size() > 0 ) {
				this.viewCreazione.showError("Ombrellone occupato", 
						"L'ombrellone (ID = " + ombrelloniOccupati.get(0) + ") non � disponibile nei giorni selezionati");
				return;
			}
			
			if(this.cliente == null || !this.cliente.getIdDocumento().equals(viewCreazione.getIdDocumento().toUpperCase()) ) {
				
				if(verificaCliente()) {
					viewCreazione.showError("Errore Inserimento", "Documento di identit� associato ad un ospite gi� registrato");
					return;
				}
				
				pstm = connection.prepareStatement(createCliente);
				pstm.setString(1, viewCreazione.getIdDocumento().toUpperCase());
				pstm.setString(2, this.capitalizeFirstLetter(viewCreazione.getNome().toLowerCase()));
				pstm.setString(3, this.capitalizeFirstLetter(viewCreazione.getCognome().toLowerCase()));
				pstm.setString(4, viewCreazione.getEmail());
				pstm.setString(5, viewCreazione.getTelefono());
				pstm.executeUpdate();
			}
			
			//in questo modo ho la possibilit� di modificare i parametri del cliente sul db
			else if(!this.cliente.getNome().equalsIgnoreCase(viewCreazione.getNome()) ||
					!this.cliente.getCognome().equalsIgnoreCase(viewCreazione.getCognome()) ||
					(this.cliente.getEmail() == null && viewCreazione.getEmail() != null) ||
					(this.cliente.getTelefono() == null && viewCreazione.getTelefono() != null) ||
					!this.cliente.getEmail().equalsIgnoreCase(viewCreazione.getEmail()) ||
					!this.cliente.getTelefono().equalsIgnoreCase(viewCreazione.getTelefono()) ) {
				
				pstm = connection.prepareStatement(updateCliente);
				pstm.setString(1, this.capitalizeFirstLetter(viewCreazione.getNome()));
				pstm.setString(2, this.capitalizeFirstLetter(viewCreazione.getCognome()));
				pstm.setString(3, viewCreazione.getEmail());
				pstm.setString(4, viewCreazione.getTelefono());
				pstm.setString(5, viewCreazione.getIdDocumento().toUpperCase());
				pstm.executeUpdate();
			}
				
			
			pstm = connection.prepareStatement(createPrenotazione);
			pstm.setString(1, formatter.format(dataInizio));
			pstm.setString(2, formatter.format(dataFine));
			pstm.setInt(3, viewCreazione.getNumeroLettini());
			if(this.cliente == null) pstm.setString(4, viewCreazione.getIdDocumento().toUpperCase());
			else pstm.setString(4, this.cliente.getIdDocumento());
			
			pstm.executeUpdate();
				
			for(Ombrellone o: this.ombrelloni) {
					
				pstm = connection.prepareStatement(aggiungiOmbrellonePrenotazione);
				pstm.setInt(1,this.getLastIdPrenotazione());
				pstm.setString(2, o.getIdOmbrellone().toUpperCase());
				pstm.executeUpdate();
			}
			
			
			this.viewCreazione.confermaCreazione(this.getLastIdPrenotazione());
			this.cliente = null;
			this.ombrelloni = new ArrayList<Ombrellone>();
			
			//Scrittura log
			this.writeLog(LocalDateTime.now(), Controller.username, "creazionePrenotazione", "creazione avvenuta con successo");
			
			pstm.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void aggiungiOmbrellone() {
		
		String idOmbrellone = viewCreazione.getInputOmbrellone();
		Connection connection = this.getConnection();
		boolean trovato = false;
		
		for(Ombrellone ombrellone: this.ombrelloni) {
			
			if(ombrellone.getIdOmbrellone().equalsIgnoreCase(idOmbrellone)) {
				trovato = true;
				break;
			}
		}
		
		if(trovato == false) {
	
			try {
				
				PreparedStatement pstm = connection.prepareStatement(find_ombrellone);
				pstm.setString(1, idOmbrellone.toLowerCase());
				ResultSet rs = pstm.executeQuery();
				
				if(rs.next()) {
					
					Ombrellone o = new Ombrellone();
					o.setIdOmbrellone(rs.getString("idOmbrellone"));
					o.setNumeroColonna(rs.getInt("numeroColonna"));
					o.setNumeroRiga(rs.getInt("numeroRiga"));
					
					this.ombrelloni.add(o);
					this.viewCreazione.addOmbrelloneToList(rs.getString("idOmbrellone"));
				}
				
				else this.viewCreazione.showError("Ombrellone inesistente",
						"Non � stato trovato nessun ombrellone con ID: " + idOmbrellone.toUpperCase());
				
				pstm.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		else this.viewCreazione.showError("Ombrellone gi� inserito",
				"L'ombrellone con ID " + idOmbrellone.toUpperCase() + " � gia stato inserito nella prenotazione");
	}
	
	public void rimuoviOmbrellone() {
		
		String idOmbrellone;
		boolean trovato = false;
		
		idOmbrellone = viewCreazione.getOmbrelloneSelezionato();
		if (idOmbrellone == null)
			idOmbrellone = viewCreazione.getInputOmbrellone();
		
		for(Ombrellone o: this.ombrelloni) {
			
			if(o.getIdOmbrellone().equalsIgnoreCase(idOmbrellone)) {
				
				trovato = true;
				this.ombrelloni.remove(o);
				this.viewCreazione.removeOmbrelloneFromList(idOmbrellone.toUpperCase());
				break;
			}
		}
		
		if(trovato == false) this.viewCreazione.showError("Ombrellone inesistente",
				"Non � stato trovato nessun ombrellone con ID: " + idOmbrellone.toUpperCase());
		
	}
	
	private String capitalizeFirstLetter(String original) {
	    if (original == null || original.length() == 0) {
	        return original;
	    }
	    return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
}