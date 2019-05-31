package ombrelloniani.controller;

import java.util.ArrayList;
import java.util.List;

import ombrelloniani.controller.exceptions.ClientNotFoundException;
import ombrelloniani.controller.exceptions.OmbrelloneNotFoundException;
import ombrelloniani.controller.exceptions.OmbrelloneOccupatoException;
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
import java.time.LocalDate;

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
	
	public void cercaCliente() throws ClientNotFoundException {
		
		String idDocumento = viewCreazione.getIdDocumento();
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
				
				this.cliente = result; //salvo il cliente temporaneamente
				
				//vado a riempire i campi della view 
				this.viewCreazione.setNome(cliente.getNome());
				this.viewCreazione.setCognome(cliente.getCognome());
				this.viewCreazione.setTelefono(cliente.getTelefono());
				this.viewCreazione.setEmail(cliente.getEmail());
			}
			
			else throw new ClientNotFoundException(idDocumento);
			
			pstm.close();
			//connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void creaPrenotazione() throws OmbrelloneOccupatoException {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		LocalDate dataInizio = viewCreazione.getDataInizio();
		LocalDate dataFine = viewCreazione.getDataFine();
		int numeroLettini = Integer.parseInt(viewCreazione.getNumeroLettini());
		
		Connection connection = this.getConnection();
		List<String> ombrelloniOccupati = new ArrayList<String>();
		
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
						ombrelloniOccupati.add(o.getIdOmbrellone());
					}
				}
			}
			
			if(ombrelloniOccupati.size() > 0 ) {
				throw new OmbrelloneOccupatoException(ombrelloniOccupati.get(0));
			}
			
			if(this.cliente == null) {
			
				pstm = connection.prepareStatement(createCliente);
				pstm.setString(1, viewCreazione.getIdDocumento());
				pstm.setString(2, viewCreazione.getNome());
				pstm.setString(3, viewCreazione.getCognome());
				pstm.setString(4, viewCreazione.getEmail());
				pstm.setString(5, viewCreazione.getTelefono());
				pstm.executeUpdate();
			}
			
			pstm = connection.prepareStatement(createPrenotazione);
			pstm.setString(1, formatter.format(dataInizio));
			pstm.setString(2, formatter.format(dataFine));
			pstm.setInt(3, numeroLettini);
			if(this.cliente == null) pstm.setString(4, viewCreazione.getIdDocumento());
			else pstm.setString(4, this.cliente.getIdDocumento());
			
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
	}
	
	public void aggiungiOmbrellone() throws OmbrelloneNotFoundException {
		
		String idOmbrellone = viewCreazione.getInputOmbrellone();
		Connection connection = this.getConnection();
		try {
			
			PreparedStatement pstm = connection.prepareStatement(find_ombrellone);
			pstm.setString(1, idOmbrellone);
			ResultSet rs = pstm.executeQuery();
			
			if(rs.next()) {
				
				Ombrellone o = new Ombrellone();
				o.setIdOmbrellone(idOmbrellone);
				o.setNumeroColonna(rs.getInt("numeroColonna"));
				o.setNumeroRiga(rs.getInt("numeroRiga"));
				
				this.ombrelloni.add(o);
			}
			
			else throw new OmbrelloneNotFoundException(idOmbrellone);
			
			pstm.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void rimuoviOmbrellone() throws OmbrelloneNotFoundException {
		
		String idOmbrellone = viewCreazione.getInputOmbrellone();
		boolean trovato = false;
		
		for(Ombrellone o: this.ombrelloni) {
			
			if(o.getIdOmbrellone().equals(idOmbrellone)) {
				
				trovato = true;
				this.ombrelloni.remove(o);
				break;
			}
		}
		
		if(trovato == false) throw new OmbrelloneNotFoundException(idOmbrellone);
		
	}

}