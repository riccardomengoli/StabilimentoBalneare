package ombrelloniani.controller;

import java.util.ArrayList;
import java.util.List;

import ombrelloniani.controller.interfaces.IController;
import ombrelloniani.controller.interfaces.IControllerGestione;
import ombrelloniani.model.Cliente;
import ombrelloniani.model.Convenzione;
import ombrelloniani.model.EnteTerzo;
import ombrelloniani.model.Fedelta;
import ombrelloniani.model.ListaConvenzioni;
import ombrelloniani.model.ListaFedelta;
import ombrelloniani.model.ListaPrezzi;
import ombrelloniani.model.ListaStagioni;
import ombrelloniani.model.Ombrellone;
import ombrelloniani.model.Prenotazione;
import ombrelloniani.model.Prezzo;
import ombrelloniani.model.PrezzoOmbrellone;
import ombrelloniani.model.PrezzoServizio;
import ombrelloniani.model.Servizio;
import ombrelloniani.model.Stagione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GestionePrenotazioneController extends Controller implements IController,IControllerGestione{
		

	static String prenotazioni_idCliente = 
			"SELECT * " + 
			"FROM PRENOTAZIONI P JOIN CLIENTI C " +
			"ON P.idCliente = C.documento " +
			"WHERE idPrenotazione = ?  "
			;
	
	static String prenotazioni_nomeCognome = 
			"SELECT * "+ 
			"FROM PRENOTAZIONI P JOIN CLIENTI C "+
			"ON P.idCliente = C.documento " +
			"WHERE lower(C.nome) = ? AND lower(C.cognome) = ? "
			;
	
	static String find_serviziByIdPrenotazione =
			"SELECT S.* " +
			"FROM SERVIZIPRENOTAZIONE SP JOIN SERVIZI S " +
			"ON SP.nomeServizio = S.nome " +
			"WHERE SP.idPrenotazione = ? "
			;
	
	static String find_ombrelloniByIdPrenotazione =
			"SELECT O.* " +
			"FROM OMBRELLONIPRENOTAZIONE OP JOIN OMBRELLONI O " +
			"ON OP.idOmbrellone = O.idOmbrellone " +
			"WHERE OP.idPrenotazione = ? "
			;
	
	static String aggiorna_fedelta = 
			"SELECT * " +
			"FROM FEDELTA "
			;
	
	static String aggiorna_convenzioni = 
			"SELECT * " +
			"FROM CONVENZIONI C JOIN ENTI_TERZI E " +
			"ON C.nomeEnte = E.nomeEnte "
			;
	
	static String aggiorna_prezziOmbrelloni = 
			"SELECT * " +
			"FROM PREZZI_OMBRELLONI P JOIN STAGIONI S " +
			"ON P.nomeStagione = S.nomeStagione "
			;
	
	static String aggiorna_prezziServizi = 
			"SELECT * " +
			"FROM PREZZI_SERVIZI P JOIN STAGIONI S JOIN SERVIZI S " +
			"ON P.nomeStagione = S.nomeStagione AND P.nomeServizio = S.nome "
			;
		
	static String aggiorna_stagioni = 
			"SELECT * " +
			"FROM STAGIONI "
			;

	public GestionePrenotazioneController() { super();};
		
	// La lista dei servizi è calcolata con duplicati in modo da mostrare anche i servizi inseriti molteplici volte
	// cerca prenotazione modifica automaticamente le stringhe in ingresso nel formato Nome Cognome
	public List<Prenotazione> cercaPrenotazioni(String nome, String cognome) {
		
		nome = this.capitalizeFirstLetter(nome);
		cognome = this.capitalizeFirstLetter(cognome);
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Connection connection = this.getConnection();
		List<Prenotazione> result = new ArrayList<Prenotazione>();
		
		try {
			
			PreparedStatement pstm = connection.prepareStatement(prenotazioni_nomeCognome);
			pstm.setString(1, nome.toLowerCase());
			pstm.setString(2, cognome.toLowerCase());
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
			
			//ricerca dei servizi associati ad una data prenotazione
			pstm = connection.prepareStatement(find_serviziByIdPrenotazione);
			
			for(Prenotazione p : result) {
				
				pstm.setInt(1, p.getIdPrenotazione());
				rs = pstm.executeQuery();
				List<Servizio> servizi = new ArrayList<Servizio>();
				
				while(rs.next()) {
					
					Servizio s = new Servizio();
					s.setNome(rs.getString("nome"));
					s.setDescrizione(rs.getString("descrizione"));
					servizi.add(s);
				}
				
				p.setServizi(servizi);
			}
			
			pstm = connection.prepareStatement(find_ombrelloniByIdPrenotazione);
			
			for(Prenotazione p : result) {
				
				pstm.setInt(1, p.getIdPrenotazione());
				rs = pstm.executeQuery();
				List<Ombrellone> ombrelloni = new ArrayList<Ombrellone>();
				
				while(rs.next()) {
					
					Ombrellone o = new Ombrellone();
					o.setIdOmbrellone(rs.getString("idOmbrellone"));
					o.setNumeroRiga(rs.getInt("numeroRiga"));
					o.setNumeroColonna(rs.getInt("numeroColonna"));
					ombrelloni.add(o);
				}
				
				p.setOmbrelloni(ombrelloni);
			}
			
			pstm.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	//fare controllo != null sulla data per vedere se ï¿½ stata ritrovata oppure no	
	public Prenotazione cercaPrenotazione(int idPrenotazione) {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Connection connection = this.getConnection();
		Prenotazione p = new Prenotazione();
		
		try {
			
			PreparedStatement pstm = connection.prepareStatement(prenotazioni_idCliente);
			pstm.setInt(1, idPrenotazione);
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {
				
				Cliente c = new Cliente();
				
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
			
			//ricerca dei servizi associati ad una data prenotazione
			pstm = connection.prepareStatement(find_serviziByIdPrenotazione);
			
			pstm.setInt(1, p.getIdPrenotazione());
			rs = pstm.executeQuery();
			List<Servizio> servizi = new ArrayList<Servizio>();
				
			while(rs.next()) {
					
				Servizio s = new Servizio();
				s.setNome(rs.getString("nome"));
				s.setDescrizione(rs.getString("descrizione"));
				servizi.add(s);
			}
				
			p.setServizi(servizi);
			
			
			pstm = connection.prepareStatement(find_ombrelloniByIdPrenotazione);
			
			pstm.setInt(1, p.getIdPrenotazione());
			rs = pstm.executeQuery();
			List<Ombrellone> ombrelloni = new ArrayList<Ombrellone>();
				
			while(rs.next()) {
				
				Ombrellone o = new Ombrellone();
				o.setIdOmbrellone(rs.getString("idOmbrellone"));
				o.setNumeroRiga(rs.getInt("numeroRiga"));
				o.setNumeroColonna(rs.getInt("numeroColonna"));
				ombrelloni.add(o);
			}
			
			p.setOmbrelloni(ombrelloni);
			
			pstm.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return p;
	}
	
	public void aggiornaListaFedelta() {
		
		List<Fedelta> listaFedelta = new ArrayList<Fedelta>();
		Connection c = this.getConnection();
		
		try {
			
			Statement stm = c.createStatement();
			ResultSet rs = stm.executeQuery(aggiorna_fedelta);
		
			while(rs.next()) {
				Fedelta f = new Fedelta();
				f.setNome(rs.getString("nome"));
				f.setGiorni(rs.getInt("giorni"));
				f.setSconto(rs.getDouble("sconto"));
				listaFedelta.add(f);
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ListaFedelta.getListaFedelta().aggiornaFedelta(listaFedelta);
	}
	
	public void aggiornaListaConvenzioni() {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		List<Convenzione> listaConvenzioni = new ArrayList<Convenzione>();
		Connection c = this.getConnection();
		
		try {
			
			Statement stm = c.createStatement();
			ResultSet rs = stm.executeQuery(aggiorna_convenzioni);
		
			while(rs.next()) {
				Convenzione convenzione = new Convenzione();
				EnteTerzo e = new EnteTerzo();
				convenzione.setNome(rs.getString("nome"));
				convenzione.setDataInizio(formatter.parse(rs.getString("dataInizio")));
				convenzione.setDataFine(formatter.parse(rs.getString("dataFine")));
				convenzione.setSconto(rs.getDouble("sconto"));
				
				e.setNomeEnte(rs.getString("nomeEnte"));
				e.setTipologia(rs.getString("tipologia"));
				convenzione.setEnte(e);
				
				listaConvenzioni.add(convenzione);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		ListaConvenzioni.getListaConvenzioni().aggiornaConvenzioni(listaConvenzioni);
		
		
	}
	
	public void aggiornaListaPrezzi() {
				
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<Prezzo> listaPrezzi = new ArrayList<Prezzo>();
		Connection c = this.getConnection();
		
		try {
			
			Statement stm = c.createStatement();
			ResultSet rs = stm.executeQuery(aggiorna_prezziServizi);
		
			while(rs.next()) {
				PrezzoServizio pServ = new PrezzoServizio();
				Stagione s = new Stagione();
				Servizio servizio = new Servizio();
				pServ.setPrezzo(rs.getFloat("prezzo"));

				s.setNomeStagione(rs.getString("nomeStagione"));
				s.setDataInizio(formatter.parse(rs.getString("dataInizio")));
				s.setDataFine(formatter.parse(rs.getString("dataFine")));
				pServ.setStagione(s);
				
				servizio.setNome(rs.getString("nomeServizio"));
				servizio.setDescrizione(rs.getString("descrizione"));
				pServ.setServizio(servizio);
		
				listaPrezzi.add(pServ);
			}
			
			stm = c.createStatement();
			rs = stm.executeQuery(aggiorna_prezziOmbrelloni);
			
			while(rs.next()) {
				PrezzoOmbrellone pOmb = new PrezzoOmbrellone();
				Stagione s = new Stagione();
				pOmb.setPrezzo(rs.getFloat("prezzo"));
				pOmb.setFilaInizio(rs.getInt("filaInizio"));
				pOmb.setFilaFine(rs.getInt("filaFine"));
				
				s.setNomeStagione(rs.getString("nomeStagione"));
				s.setDataInizio(formatter.parse(rs.getString("dataInizio")));
				s.setDataFine(formatter.parse(rs.getString("dataFine")));
				
				pOmb.setStagione(s);
				listaPrezzi.add(pOmb);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ListaPrezzi.getListaPrezzi().aggiornaPrezzi(listaPrezzi);
	}
	
	public void aggiornaListaStagioni() {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<Stagione> listaStagioni = new ArrayList<Stagione>();
		Connection c = this.getConnection();
		
		try {
			
			Statement stm = c.createStatement();
			ResultSet rs = stm.executeQuery(aggiorna_stagioni);
		
			while(rs.next()) {
				Stagione s = new Stagione();
			
				s.setNomeStagione(rs.getString("nomeStagione"));
				s.setDataInizio(formatter.parse(rs.getString("dataInizio")));
				s.setDataFine(formatter.parse(rs.getString("dataFine")));
	
				listaStagioni.add(s);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ListaStagioni.getListaStagioni().aggiornaStagioni(listaStagioni);
	}
	

	private String capitalizeFirstLetter(String original) {
	    if (original == null || original.length() == 0) {
	        return original;
	    }
	    return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
}
