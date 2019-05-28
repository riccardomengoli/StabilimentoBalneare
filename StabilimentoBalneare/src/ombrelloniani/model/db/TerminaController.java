package ombrelloniani.model.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import ombrelloniani.controller.interfaces.IControllerTermina;
import ombrelloniani.model.Convenzione;
import ombrelloniani.model.EnteTerzo;
import ombrelloniani.model.EntryRicevuta;
import ombrelloniani.model.Fedelta;
import ombrelloniani.model.ListaConvenzioni;
import ombrelloniani.model.ListaFedelta;
import ombrelloniani.model.ListaPrezziOmbrelloni;
import ombrelloniani.model.ListaPrezziServizi;
import ombrelloniani.model.ListaStagioni;
import ombrelloniani.model.Ombrellone;
import ombrelloniani.model.Prenotazione;
import ombrelloniani.model.PrenotazioneTerminata;
import ombrelloniani.model.PrezzoOmbrellone;
import ombrelloniani.model.PrezzoServizio;
import ombrelloniani.model.Ricevuta;
import ombrelloniani.model.Servizio;
import ombrelloniani.model.Stagione;
import ombrelloniani.view.interfaces.IViewTermina;

public class TerminaController implements IControllerTermina{
	
	private ListaPrezziOmbrelloni prezziOmbrelloni;
	private ListaPrezziServizi prezziServizi;
	private ListaConvenzioni convenzioni;
	private Convenzione convenzione;
	private ListaFedelta fedelta;
	private Ricevuta ricevuta;
	private PrenotazioneTerminata prenotazioneTerminata;
	private Prenotazione prenotazione;
	private List<Prenotazione> prenotazioni;
	//Controller
	private GestionePrenotazioneController controller = new GestionePrenotazioneController();
	private ModificaPrenotazioneController modController = new ModificaPrenotazioneController();
	//View
	private IViewTermina viewTermina;
	
	//Query
	private static String cercaConvenzioni = 
			"SELECT * FROM CONVENZIONI"
			+ "INNER JOIN ENTI_TERZI ON CONVENZIONI.nomeEnte = ENTI_TERZI.nomeEnte";
	
	private static String terminaPrenotazione = 
			"DELETE FROM PRENOTAZIONI "
			+ "WHERE idPrenotazione = ?";
	
	private static String aggiornaPrenotazioniTerminate = 
			"INSERT INTO PRENOTAZIONI_TERMINATE(idPrenotazione, dataInizio, dataFine, numeroLettini, idCliente, saldo, giorni)"
			+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private static String giorniTotaliSoggiorno =
			"SELECT SUM(giorni)"
			+ "FROM PRENOTAZIONI_TERMINATE"
			+ "WHERE idCliente = ?";

	private  static String informazioniServizio = 
			"SELECT dataOra FROM SERVIZIPRENOTAZIONE"
			+ "WHERE idPrenotazione = ? AND nomeServizio = ?"; 
	
	//Costruttore
	public TerminaController(IViewTermina viewTermina) {
		this.viewTermina = viewTermina;
	}
	
	//Metodi
	public void terminaPrenotazione() {
		this.prenotazioneTerminata = new PrenotazioneTerminata(prenotazione);
		
		if(prenotazione.getDataFine() != Date.from(Instant.now())) {
			modController.modificaDataFine(prenotazione, Date.from(Instant.now()));
		}
		
		prenotazioneTerminata.setSaldo(calcolaSaldo());
		
		aggiornaDataBase();
		controller.writeLog("prenotazione " + prenotazione.getIdPrenotazione()
				+ "terminata alle " + Date.from(Instant.now()));
	}

	public void aggiornaDataBase() {
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Connection connection = controller.getConnection();
		PreparedStatement delete;
		PreparedStatement insert;
		
		try {
			delete = connection.prepareStatement(terminaPrenotazione);
			delete.setInt(1, prenotazione.getIdPrenotazione());
			delete.executeUpdate();
			
			insert = connection.prepareStatement(aggiornaPrenotazioniTerminate);
			insert.setInt(1, prenotazioneTerminata.getIdPrenotazione());
			insert.setString(2, formatter.format(prenotazioneTerminata.getDataInizio()));
			insert.setString(3, formatter.format(prenotazioneTerminata.getDataFine()));
			insert.setInt(4, prenotazioneTerminata.getNumeroLettini());
			insert.setString(5, prenotazioneTerminata.getCliente().getIdDocumento());
			insert.setFloat(6, prenotazioneTerminata.getSaldo());
			insert.setDouble(7, prenotazioneTerminata.getGiorni());
			insert.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void cercaPrenotazione(int idPren) throws ParseException {
		prenotazione = controller.cercaPrenotazione(idPren);
		mostraPrenotazione();
	}

	public void prenotazioneSelezionata(int index) {
		prenotazione = prenotazioni.get(index);
		mostraPrenotazioni();
	}
	
	private void mostraPrenotazione() {
		DateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
		List<String> ombrelloni = new ArrayList<String>();
		List<String> servizi = new ArrayList<String>();
		
		viewTermina.setId(Integer.toString(prenotazione.getIdPrenotazione()));
		viewTermina.setDataInizio(formatter.format(this.prenotazione.getDataInizio()));
		viewTermina.setDataFine(formatter.format(this.prenotazione.getDataFine()));
		viewTermina.setNumeroLettini(Integer.toString(prenotazione.getNumeroLettini()));
		for(Ombrellone o : prenotazione.getOmbrelloni()) {
			ombrelloni.add(o.getIdOmbrellone());
		}
		viewTermina.setOmbrelloni(prenotazione.getOmbrelloni().size(), ombrelloni);
		for(Servizio s : prenotazione.getServizi()) {
			servizi.add(s.getNome());
		}
		viewTermina.setServizi(prenotazione.getServizi().size(), servizi);
		
		mostraRicevuta(); // ancora non contiene la convenzione
	}
	
	private void mostraRicevuta() {
		creaRicevuta();
		int numeroEntry = (this.ricevuta.getEntries().size());
		List<String> ricevuta = new ArrayList<String>();
		
		if(this.ricevuta.getPercentualeSconto() == 0) {
			ricevuta.add(null);
		} else
			ricevuta.add(Float.toString(this.ricevuta.getPercentualeSconto()));
		if(this.ricevuta.getPercentualeConvenzione() == 0) {
			ricevuta.add(null);
		} else
			ricevuta.add(Float.toString(this.ricevuta.getPercentualeConvenzione()));
		ricevuta.add(Float.toString(this.ricevuta.getPrezzoTotale()));
		for(EntryRicevuta e : this.ricevuta.getEntries()) {
			ricevuta.add(e.getTipoPrezzo());
			ricevuta.add(e.getDescrizione());
			ricevuta.add(Integer.toString(e.getGiorni()));
			ricevuta.add(Float.toString(e.getTotale()));
		}
		
		viewTermina.aggiornaTabellaRicevuta(ricevuta, numeroEntry);
	}

	public void cercaPrenotazioni(String nome, String cognome) throws ParseException{
		prenotazioni =  controller.cercaPrenotazioni(nome, cognome);
		mostraPrenotazioni();
	}
	
	private void mostraPrenotazioni() {
		DateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
		String[] prenotazione = new String[3];
		List<String[]> prenotazioni = new ArrayList<String[]>();
		
		for(Prenotazione p : this.prenotazioni) {
			prenotazione[1] = Integer.toString(p.getIdPrenotazione());
			prenotazione[2] = formatter.format(this.prenotazione.getDataInizio());
			prenotazione[3] = formatter.format(this.prenotazione.getDataFine());
			prenotazioni.add(prenotazione);
		}
		
		viewTermina.aggiornaListaPrenotazioniDisponibili(prenotazioni);
	}
	
	public void cercaConvenzioni() {
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Connection connection = controller.getConnection();
		PreparedStatement pstm;
		List<Convenzione> convenzioni = new ArrayList<Convenzione>();
		Convenzione convenzione = new Convenzione();
		
		try {
			pstm = connection.prepareStatement(cercaConvenzioni);
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {	
				convenzione.setNome(rs.getString("nome"));
				convenzione.setDataInizio(formatter.parse(rs.getString("dataInizio")));
				convenzione.setDataFine(formatter.parse(rs.getString("dataFine")));
				//se non funziona da mettere getDouble con cast
				convenzione.setSconto(rs.getFloat("sconto"));
				convenzione.setEnte(new EnteTerzo(rs.getString("nomeEnte"), rs.getString(rs.getString("tipologia"))));
				
				convenzioni.add(convenzione);
			
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		this.convenzioni.aggiornaConvenzioni(convenzioni);
		
		mostraConvenzioni();
	}
	
	private void mostraConvenzioni() {
		DateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
		List<String[]> convenzioni = new ArrayList<String[]>();
		String[] convenzione = new String[6];
		
		for(Convenzione c : this.convenzioni.getConvenzioni()) {
			convenzione[1] = c.getNome();
			convenzione[2] = formatter.format(c.getDataInizio());
			convenzione[3] = formatter.format(c.getDataFine());
			convenzione[4] = Float.toString(c.getSconto());
			convenzione[5] = c.getEnte().getNomeEnte();
			convenzione[6] = c.getEnte().getTipologia();
			
			convenzioni.add(convenzione);
		}
		
		viewTermina.aggiornaListaConvenzioni(convenzioni);
	}

	public void convenzioneSelezionata(int index) {
		convenzione = convenzioni.getConvenzioni().get(index);
		ricevuta.setPercentualeConvenzione(convenzione.getSconto());
	}
	
	private float calcolaSaldo() {
		return ricevuta.getPrezzoTotale() 
				- (ricevuta.getPrezzoTotale()*ricevuta.getPercentualeSconto()) 
				- (ricevuta.getPrezzoTotale()*ricevuta.getPercentualeConvenzione());
	}
	
	private void creaRicevuta() {
		ricevuta = new Ricevuta();
		
		ricevuta.setEntries(calcolaEntryRicevuta());
		ricevuta.setPercentualeSconto(calcolaSconto());
		ricevuta.setPercentualeConvenzione(0);
		ricevuta.setPrezzoTotale();
	}

	private float calcolaSconto() {
		float sconto = 0;
		double giorni = 0;
		Connection connection = controller.getConnection();
		PreparedStatement pstm;
		
		try {
			pstm = connection.prepareStatement(giorniTotaliSoggiorno);
			pstm.setString(1, prenotazioneTerminata.getCliente().getIdDocumento());
			ResultSet rs = pstm.executeQuery();
			giorni = rs.getDouble(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(giorni == 0)
			return sconto;
		else {
			for(Fedelta f : fedelta.getFedelta()) {
				if(giorni >= f.getGiorni()) {
					if(f.getSconto() > sconto) {
						sconto = f.getSconto();
					}
				}
			}
		}
		
		return sconto;
	}
	
	private List<EntryRicevuta> calcolaEntryRicevuta() {
		List<EntryRicevuta> entries = new ArrayList<EntryRicevuta>();
		EntryRicevuta entry = new EntryRicevuta();
		for(Ombrellone o : prenotazioneTerminata.getOmbrelloni()) {
			entry.setTipoPrezzo("Ombrellone");
			entry.setDescrizione(o.getIdOmbrellone());
			entry.setGiorni(prenotazioneTerminata.getGiorni());
			entry.setTotale(calcolaTotaleOmbrellone(o, prenotazioneTerminata.getDataFine()));
			entries.add(entry);
		}
		
		for(Servizio s : prenotazioneTerminata.getServizi()) {
			entry.setTipoPrezzo("Servizio");
			entry.setDescrizione(s.getDescrizione());
			entry.setGiorni(0);
			entry.setTotale(calcolaTotaleServizio(s));
			entries.add(entry);
		}
		
		return entries;
	}

	//funziona solo finche nella prenotazione c'é solo un servizio per nome
	private float calcolaTotaleServizio(Servizio servizio) {
		Stagione stagione = null;
		
		Date data = null;
		
		Connection connection = controller.getConnection();
		PreparedStatement pstm;
		
		try {
			pstm = connection.prepareStatement(informazioniServizio);
			pstm.setInt(1, prenotazioneTerminata.getIdPrenotazione());
			pstm.setString(2, servizio.getNome());
			ResultSet rs = pstm.executeQuery();
			data = rs.getDate(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(Stagione s : ListaStagioni.getListaStagioni().getListStagioni()) {
			if(data.compareTo(s.getDataInizio()) >= 0 || data.compareTo(s.getDataFine()) <= 0) {
				stagione = s;			}	
		}
		
		for(PrezzoServizio p : prezziServizi.getPrezziServizi()) {
			if(p.getStagione().equals(stagione) && servizio.equals(p.getServizio())) {
				return p.getPrezzo();
			}
		}
		
		return 0;
	}

	//versione semplificata: prende la stagione nella quale si trova la data di fine
	private float calcolaTotaleOmbrellone(Ombrellone o, Date dataFine) {
		Stagione stagione = null;
		for(Stagione s : ListaStagioni.getListaStagioni().getListStagioni()) {
			if(dataFine.compareTo(s.getDataInizio()) >= 0 || dataFine.compareTo(s.getDataFine()) <= 0) {
				stagione = s;			}	
		}
		
		for(PrezzoOmbrellone p : prezziOmbrelloni.getPrezziOmbrelloni()) {
			if(p.getStagione().equals(stagione) && o.getNumeroRiga() >= p.getFilaInizio() && o.getNumeroRiga() <= p.getFilaFine()) {
				return p.getPrezzo();
			}
		}
		
		return 0;
	}
	
	
}
