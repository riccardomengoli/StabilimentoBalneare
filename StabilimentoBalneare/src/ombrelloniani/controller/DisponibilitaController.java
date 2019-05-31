package ombrelloniani.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ombrelloniani.controller.interfaces.IController;
import ombrelloniani.controller.interfaces.IControllerDisponibilita;

public class DisponibilitaController implements IController,IControllerDisponibilita{

	private GestionePrenotazioneController controllerGestione = new GestionePrenotazioneController();
	
	static String get_ombrelloni = 
			"SELECT numeroRiga,numeroColonna FROM OMBRELLONI";
	
	static String check_ombrellone = 
			"SELECT O.numeroRiga, O.numeroColonna " + 
			"FROM PRENOTAZIONI P JOIN OMBRELLONIPRENOTAZIONE OP JOIN OMBRELLONI O " +
			"ON P.idPrenotazione = OP.idPrenotazione AND OP.idOmbrellone = O.idOmbrellone " +
			"WHERE ? BETWEEN P.dataInizio AND P.dataFine  OR " +
			"? BETWEEN P.dataInizio AND P.dataFine OR " +
			"(P.dataInizio >= ? AND P.dataFine <= ?) "
			;
	
	static String stato_ombrellone = 
			"SELECT P.*, C.* " + 
			"FROM PRENOTAZIONI P JOIN OMBRELLONIPRENOTAZIONE OP JOIN OMBRELLONI O JOIN CLIENTI C " +
			"ON P.idPrenotazione = OP.idPrenotazione AND OP.idOmbrellone = O.idOmbrellone AND C.documento = P.idCliente " +
			"WHERE O.numeroRiga = ? AND O.numeroColonna = ? " +
			"AND (? BETWEEN P.dataInizio AND P.dataFine OR " +
			"? BETWEEN P.dataInizio AND P.dataFine OR " +
			"(P.dataInizio >= ? AND P.dataFine <= ?)) "
			;
	
	//metodo restituisce per ogni ombrellone un array con (in ordine): numeroRiga, numeroColonna
	public List<Integer[]> getOmbrelloni() {
		
		List<Integer[]> result = new ArrayList<Integer[]>();
		Connection connection = controllerGestione.getConnection();
		
		try {
			
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(get_ombrelloni);
			
			while(rs.next()) {
				
				Integer[] infOmbrellone = new Integer[3];
				infOmbrellone[0] = rs.getInt("numeroRiga");
				infOmbrellone[1] = rs.getInt("numeroColonna");
				result.add(infOmbrellone);
			}
			
			stm.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	//restituisce numeroRiga, numeroColonna ombrelloni occupati(caso singola Data mettere la data inserita nei due campi)
	public List<Integer[]> mostraStatoSpiaggia(Date dataInizio, Date dataFine) {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		List<Integer[]> result = new ArrayList<Integer[]>();
		Connection connection = controllerGestione.getConnection();
		int fila,colonna;
		boolean trovato;
		
		try {
			
			PreparedStatement stm = connection.prepareStatement(check_ombrellone);
			stm.setString(1, formatter.format(dataFine));
			stm.setString(2, formatter.format(dataInizio));
			stm.setString(3, formatter.format(dataInizio));
			stm.setString(4, formatter.format(dataFine));
			ResultSet rs = stm.executeQuery();
			
			while(rs.next()) {
				
				trovato = false;
				Integer[] infOmbrellone = new Integer[3];
				fila = rs.getInt("numeroRiga");
				colonna = rs.getInt("numeroColonna");
				
				for(Integer[] arrInt: result) {
					
					if(arrInt[0] == fila && arrInt[1] == colonna) {
						trovato = true;
						break;
					}
				}
				
				if(trovato == false) {
					infOmbrellone[0] = fila;
					infOmbrellone[1] = colonna;
					result.add(infOmbrellone);
				}
			}
			
			stm.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public List<String[]> mostraStatoOmbrellone(Date dataInizio, Date dataFine, int numeroRiga, int numeroColonna) {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		List<String[]> result = new ArrayList<String[]>();
		Connection connection = controllerGestione.getConnection();
		
		try {
			
			PreparedStatement stm = connection.prepareStatement(stato_ombrellone);
			stm.setInt(1, numeroRiga);
			stm.setInt(2, numeroColonna);
			stm.setString(3, formatter.format(dataFine));
			stm.setString(4, formatter.format(dataInizio));
			stm.setString(5, formatter.format(dataInizio));
			stm.setString(6, formatter.format(dataFine));
			ResultSet rs = stm.executeQuery();
			
			while(rs.next()) {
				
				String[] infoPrenotazione = new String[5];
				infoPrenotazione[0] = rs.getString("idPrenotazione");
				infoPrenotazione[1] = rs.getString("dataInizio");
				infoPrenotazione[2] = rs.getString("dataFine");
				infoPrenotazione[3] = rs.getString("nome");
				infoPrenotazione[4] = rs.getString("cognome");
				
				result.add(infoPrenotazione);
			}
			
			stm.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
}
