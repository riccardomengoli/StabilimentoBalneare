package ombrelloniani.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ombrelloniani.controller.interfaces.IController;
import ombrelloniani.controller.interfaces.IControllerDisponibilita;
import ombrelloniani.view.interfaces.IViewDisponibilita;

public class DisponibilitaController implements IController, IControllerDisponibilita {

	private GestionePrenotazioneController controllerGestione = new GestionePrenotazioneController();
	
	//View
	private IViewDisponibilita viewDisponibilita;

	//Controller
	//private ControlloDisponibilita controlloDisponibilita = new ControlloDisponibilita();
	
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
			"SELECT P.*, C.*, O.* " + 
			"FROM PRENOTAZIONI P JOIN OMBRELLONIPRENOTAZIONE OP JOIN OMBRELLONI O JOIN CLIENTI C " +
			"ON P.idPrenotazione = OP.idPrenotazione AND OP.idOmbrellone = O.idOmbrellone AND C.documento = P.idCliente " +
			"WHERE O.numeroRiga = ? AND O.numeroColonna = ? " +
			"AND (? BETWEEN P.dataInizio AND P.dataFine OR " +
			"? BETWEEN P.dataInizio AND P.dataFine OR " +
			"(P.dataInizio >= ? AND P.dataFine <= ?)) "
			;
	
	// Costruttore deve prendere in ingresso la view sulla quale richiamare i metodi
	public DisponibilitaController (IViewDisponibilita viewDisponibilita) {
			this.viewDisponibilita = viewDisponibilita;
		}

	// metodo restituisce per ogni ombrellone un array con (in ordine): numeroRiga,
	// numeroColonna
	@Override
	public List<int[]> getOmbrelloni() {

		List<int[]> result = new ArrayList<int[]>();
		Connection connection = controllerGestione.getConnection();

		try {

			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(get_ombrelloni);

			while (rs.next()) {

				int[] infOmbrellone = new int[2];
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

	// restituisce numeroRiga, numeroColonna ombrelloni occupati(caso singola Data
	// mettere la data inserita nei due campi)
	@Override
	public List<int[]> mostraStatoSpiaggia() {

		LocalDate dataInizio = viewDisponibilita.getDataInizio();
		LocalDate dataFine = viewDisponibilita.getDataFine();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		List<int[]> result = new ArrayList<int[]>();
		Connection connection = controllerGestione.getConnection();
		int fila, colonna;
		boolean trovato;

		try {

			PreparedStatement stm = connection.prepareStatement(check_ombrellone);
			stm.setString(1, formatter.format(dataFine));
			stm.setString(2, formatter.format(dataInizio));
			stm.setString(3, formatter.format(dataInizio));
			stm.setString(4, formatter.format(dataFine));
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				trovato = false;
				int[] infOmbrellone = new int[3];
				fila = rs.getInt("numeroRiga");
				colonna = rs.getInt("numeroColonna");

				for (int[] arrInt : result) {

					if (arrInt[0] == fila && arrInt[1] == colonna) {
						trovato = true;
						break;
					}
				}

				if (trovato == false) {
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

	@Override
	public List<String[]> mostraStatoOmbrellone() {
		
		LocalDate dataInizio = viewDisponibilita.getDataInizio();
		LocalDate dataFine = viewDisponibilita.getDataFine();
		int numeroRiga = (viewDisponibilita.getOmbrelloneSelezionato())[0];
		int numeroColonna = (viewDisponibilita.getOmbrelloneSelezionato())[1];
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		List<String[]> list = new ArrayList<String[]>();
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

			String[] infoPrenotazione = new String[6];
			while (rs.next()) {

				infoPrenotazione[0] = rs.getString("idOmbrellone");
				infoPrenotazione[1] = rs.getString("idPrenotazione");
				infoPrenotazione[2] = rs.getString("dataInizio");
				infoPrenotazione[3] = rs.getString("dataFine");
				infoPrenotazione[4] = rs.getString("nome");
				infoPrenotazione[5] = rs.getString("cognome");

				list.add(infoPrenotazione);
			}
			
			stm.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//se la lista é vuota non ha trovato nessun risultato => null
		if(list.size() == 0) {
			return null;
		}
		
		return list;
	}
}
