package ombrelloniani.controller;

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

import ombrelloniani.controller.interfaces.IController;
import ombrelloniani.controller.interfaces.IControllerTermina;
import ombrelloniani.model.Convenzione;
import ombrelloniani.model.EntryRicevuta;
import ombrelloniani.model.Fedelta;
import ombrelloniani.model.ListaConvenzioni;
import ombrelloniani.model.ListaFedelta;
import ombrelloniani.model.ListaPrezzi;
import ombrelloniani.model.Ombrellone;
import ombrelloniani.model.Prenotazione;
import ombrelloniani.model.PrenotazioneTerminata;
import ombrelloniani.model.Prezzo;
import ombrelloniani.model.PrezzoOmbrellone;
import ombrelloniani.model.PrezzoServizio;
import ombrelloniani.model.Ricevuta;
import ombrelloniani.model.Servizio;

import java.util.Date;

public class TerminaController implements IController, IControllerTermina{
	
	private ListaPrezzi prezzi = ListaPrezzi.getListaPrezzi();
	private ListaConvenzioni convenzioni = ListaConvenzioni.getListaConvenzioni();
	private List<String[]> convenzioniString = new ArrayList<String[]>();
	private ListaFedelta fedelta = ListaFedelta.getListaFedelta();
	private Ricevuta ricevuta;
	private PrenotazioneTerminata prenotazioneTerminata;
	private Prenotazione prenotazione;
	private List<Prenotazione> prenotazioni;
	
	//Controller
	private GestionePrenotazioneController controller = new GestionePrenotazioneController();
	private ModificaPrenotazioneController modController = new ModificaPrenotazioneController();
	
	private float prezzoBaseOmbrelloni = 10;
	private float prezzoBaseServizi = 10;
	//View

	//Query
	
	private static String terminaPrenotazione = 
			"DELETE FROM PRENOTAZIONI "
			+ "WHERE idPrenotazione = ?";
	
	private static String aggiornaPrenotazioniTerminate = 
			"INSERT INTO PRENOTAZIONI_TERMINATE(idPrenotazione, dataInizio, dataFine, numeroLettini, idCliente, saldo) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";
	
	private static String giorniTotaliSoggiorno =
			"SELECT SUM(((strftime('%s',dataFine)-strftime('%s',dataInizio))/86400)+1) "
			+ "FROM PRENOTAZIONI_TERMINATE "
			+ "WHERE idCliente = ? ";

	private  static String informazioniServizio = 
			"SELECT dataOra FROM SERVIZIPRENOTAZIONE "
			+ "WHERE idPrenotazione = ? AND nomeServizio = ?";
	
	private static String numGiorni_prenotazione = 
			"SELECT (strftime('%s',?) - strftime('%s',?))/86400"
			;
	
	private static String termina_ombrelloniPrenotazione =
			"DELETE FROM OMBRELLONIPRENOTAZIONE " +
			"WHERE idPrenotazione = ? "
			;
	
	private static String termina_serviziPrenotazione =
			"DELETE FROM SERVIZIPRENOTAZIONE " +
			"WHERE idPrenotazione = ? "
			;
	
	//Costruttore deve prendere in ingresso la view sulla quale richiamare i metodi
	public TerminaController() {
	}
	
	//Metodi della classe
	
	public void cercaPrenotazione(int idPren) throws ParseException {
		prenotazione = controller.cercaPrenotazione(idPren);
		
		if(prenotazione.getDataFine().compareTo(Date.from(Instant.now())) == 1) {
			prenotazione = modController.modificaDataFine(prenotazione, Date.from(Instant.now()));
		}
		
		mostraPrenotazione();
	}
	
	//cerco tutte le prenotazioni con nome e cognome dati
	
	public void cercaPrenotazioni(String nome, String cognome) throws ParseException{
		prenotazioni =  controller.cercaPrenotazioni(nome, cognome);
		mostraPrenotazioni();
	}
	
	//seleziono tra tutte la prenotazione che mi interessa
	
	private void mostraPrenotazioni() {
		System.out.println("Selezionare una prenotazione dalla lista: ");
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String[] prenotazione = new String[3];
		List<String[]> prenotazioni = new ArrayList<String[]>();
		
		for(Prenotazione p : this.prenotazioni) {
			prenotazione[0] = Integer.toString(p.getIdPrenotazione());
			prenotazione[1] = formatter.format(p.getDataInizio());
			prenotazione[2] = formatter.format(p.getDataFine());
			prenotazioni.add(prenotazione);
		}
		
		//viewTermina.aggiornaListaPrenotazioniDisponibili(prenotazioni);
	}

	public void prenotazioneSelezionata(int index) throws ParseException {
		prenotazione = prenotazioni.get(index);
		
		if(prenotazione.getDataFine().compareTo(Date.from(Instant.now())) == 1) {
			prenotazione = modController.modificaDataFine(prenotazione, Date.from(Instant.now()));
		}
		
		mostraPrenotazione();
	}
	
	private void mostraPrenotazione() throws ParseException {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		List<String> ombrelloni = new ArrayList<String>();
		List<String> servizi = new ArrayList<String>();
		
		//viewTermina.setId(Integer.toString(prenotazione.getIdPrenotazione()));
		//viewTermina.setDataInizio(formatter.format(this.prenotazione.getDataInizio()));
		//viewTermina.setDataFine(formatter.format(this.prenotazione.getDataFine()));
		//viewTermina.setNumeroLettini(Integer.toString(prenotazione.getNumeroLettini()));
		for(Ombrellone o : prenotazione.getOmbrelloni()) {
			ombrelloni.add(o.getIdOmbrellone());
		}
		//viewTermina.setOmbrelloni(prenotazione.getOmbrelloni().size(), ombrelloni);
		for(Servizio s : prenotazione.getServizi()) {
			servizi.add(s.getNome());
		}
		//viewTermina.setServizi(prenotazione.getServizi().size(), servizi);
		
		mostraRicevuta(); //ancora non contiene la convenzione
	}
	
	//Metodi richiamati al click del bottone
	public void terminaPrenotazione() {
		
		this.prenotazioneTerminata = new PrenotazioneTerminata(prenotazione);
		
		prenotazioneTerminata.setSaldo(this.calcolaSaldo());
		
		aggiornaDataBase();
		controller.writeLog("prenotazione " + prenotazione.getIdPrenotazione()
				+ "terminata alle " + Date.from(Instant.now()));
	}

	//aggiorna database cancella tutte le info su ombrelloni prenotazione e servizi della prenotazione terminata
	private void aggiornaDataBase() {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Connection connection = controller.getConnection();
		PreparedStatement delete;
		PreparedStatement insert;
		
		try {
			
			delete = connection.prepareStatement(terminaPrenotazione);
			delete.setInt(1, prenotazione.getIdPrenotazione());
			delete.executeUpdate();
			
			delete = connection.prepareStatement(termina_ombrelloniPrenotazione);
			delete.setInt(1, this.prenotazione.getIdPrenotazione());
			delete.executeUpdate();
			
			delete = connection.prepareStatement(termina_serviziPrenotazione);
			delete.setInt(1, this.prenotazione.getIdPrenotazione());
			delete.executeUpdate();
			
			insert = connection.prepareStatement(aggiornaPrenotazioniTerminate);
			insert.setInt(1, prenotazioneTerminata.getIdPrenotazione());
			insert.setString(2, formatter.format(prenotazioneTerminata.getDataInizio()));
			insert.setString(3, formatter.format(prenotazioneTerminata.getDataFine()));
			insert.setInt(4, prenotazioneTerminata.getNumeroLettini());
			insert.setString(5, prenotazioneTerminata.getCliente().getIdDocumento());
			insert.setFloat(6, prenotazioneTerminata.getSaldo());
			insert.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void creaRicevuta() throws ParseException {
		ricevuta = new Ricevuta();
		
		ricevuta.setEntries(calcolaEntryRicevuta());
		ricevuta.setPercentualeSconto(calcolaSconto());
		ricevuta.setPercentualeConvenzione(0); // viene aggiunta successivamente
		ricevuta.setPrezzoTotale();
	}

	//funzione manca al controllo
	private void mostraRicevuta() throws ParseException {
		creaRicevuta();
		List<String[]> entriesRicevuta = new ArrayList<String[]>();
		
		for(EntryRicevuta e: this.ricevuta.getEntries()) {
			
			String[] entry = new String[4];
			entry[0] = e.getTipoPrezzo();
			entry[1] = e.getDescrizione();
			entry[2] = Integer.toString(e.getGiorni());
			entry[3] = Float.toString(e.getTotale());
			
			entriesRicevuta.add(entry);
		}
		
		//viewTermina.aggiornaTabellaRicevuta(entriesRicevuta, this.ricevuta.getPercentualeConvenzione(),
		//		this.ricevuta.getPercentualeSconto(), this.ricevuta.getPrezzoTotale());
	}

	//mostraConvenzioni mostra solo le convenzioni attualmente disponibili confrontandole con la data odierna
	public void mostraConvenzioni() {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		convenzioniString = new ArrayList<String[]>();
		String[] convenzione;
		Date dNow = new Date();
		
		for(Convenzione c : this.convenzioni.getConvenzioni()) {
			if(c.getDataInizio().compareTo(dNow) <= 0 && c.getDataFine().compareTo(dNow) >= 0) {
				convenzione = new String[6];
				convenzione[0] = c.getNome();
				convenzione[1] = formatter.format(c.getDataInizio());
				convenzione[2] = formatter.format(c.getDataFine());
				convenzione[3] = Double.toString(c.getSconto());
				convenzione[4] = c.getEnte().getNomeEnte();
				convenzione[5] = c.getEnte().getTipologia();
					
				convenzioniString.add(convenzione);
			}
	
		}

		//viewTermina.aggiornaListaConvenzioni(convenzioni);
	}

	//index fa riferimento alla posizione nella lista di convenzioniString
	public void convenzioneSelezionata(int index) throws ParseException {
		
		ricevuta.setPercentualeConvenzione(Double.parseDouble((convenzioniString.get(index))[3]));
		mostraRicevuta(); //seleziono la ricevuta e riaggiorno la tabella
	}
	
	private float calcolaSaldo() {
		
		System.out.println("Sconto fedeltà: " + ricevuta.getPercentualeSconto());
		System.out.println("Sconto convenzione: " + ricevuta.getPercentualeConvenzione());
		
		if(ricevuta.getPercentualeSconto() >= ricevuta.getPercentualeConvenzione()) {
			System.out.println("Applico sconto fedeltà");
			return ricevuta.getPrezzoTotale() 
					- (float)(ricevuta.getPrezzoTotale()*ricevuta.getPercentualeSconto()); }
		
		else {
			System.out.println("Applico sconto convenzione");
			return ricevuta.getPrezzoTotale() 
					- (float)(ricevuta.getPrezzoTotale()*ricevuta.getPercentualeConvenzione()); }  
	
	}

	//il metodo legge in memoria su prenotazioniTerminate i giorni trascorsi dal cliente nello stabilimento
	private double calcolaSconto() {
		double sconto = 0;
		int giorni = 0;
		Connection connection = controller.getConnection();
		PreparedStatement pstm;
		
		try {
			pstm = connection.prepareStatement(giorniTotaliSoggiorno);
			pstm.setString(1, this.prenotazione.getCliente().getIdDocumento());
			ResultSet rs = pstm.executeQuery();
			giorni = rs.getInt(1);
						
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
	
	//attenzione nella entry relativa al prezzo del servizio il giorno è a 0!
	private List<EntryRicevuta> calcolaEntryRicevuta() throws ParseException {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		List<EntryRicevuta> entries = new ArrayList<EntryRicevuta>();
		EntryRicevuta entry;
		int numGiorni = 0;
		Connection connection = controller.getConnection();

		try {
			PreparedStatement pstm = connection.prepareStatement(numGiorni_prenotazione);
			pstm.setString(1, formatter.format(this.prenotazione.getDataFine()));
			pstm.setString(2, formatter.format(this.prenotazione.getDataInizio()));
			ResultSet rs = pstm.executeQuery();
			numGiorni = rs.getInt(1) + 1;
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		for(Ombrellone o : this.prenotazione.getOmbrelloni()) {
			
			entry = new EntryRicevuta();
			entry.setTipoPrezzo("Ombrellone");
			entry.setDescrizione(o.getIdOmbrellone());
			entry.setGiorni(numGiorni);
			entry.setTotale(calcolaTotaleOmbrellone(o, this.prenotazione.getDataFine(),numGiorni));
		
			entries.add(entry);
		}
		
		List<Servizio> serviziCalcolati = new ArrayList<Servizio>();
		boolean trovato;
		
		for(Servizio s : this.prenotazione.getServizi()) {
			
			trovato = false;
			
			for(Servizio serv: serviziCalcolati) {
				if(s.getNome().equals(serv.getNome())) {
					trovato = true;
					break;
				}
			}
			
			if(trovato == false) {
			
				serviziCalcolati.add(s);
				entry = new EntryRicevuta();
				entry.setTipoPrezzo("Servizio");
				entry.setDescrizione(s.getNome());
				entry.setGiorni(0);
				entry.setTotale(calcolaTotaleServizio(s));
				
				entries.add(entry);
			}
		}
		return entries;
	}
	
	private float calcolaTotaleServizio(Servizio servizio) throws ParseException {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Connection connection = controller.getConnection();
		PreparedStatement pstm;
		float totaleServizio = 0;
		int numeroServizi = 0;
		boolean trovato = false;
		
		try {
			pstm = connection.prepareStatement(informazioniServizio);
			pstm.setInt(1, prenotazione.getIdPrenotazione());
			pstm.setString(2, servizio.getNome());
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {
				
				trovato = false;
				
				for(Prezzo p: this.prezzi.getPrezzi()) {
					
					if(p.getClass() == PrezzoServizio.class) {
						
						Date dateTime = formatter.parse(rs.getString("dataOra"));
						PrezzoServizio pServ = (PrezzoServizio)p;
						
						if(pServ.getStagione().getDataInizio().compareTo(dateTime) <= 0 &&
								pServ.getStagione().getDataFine().compareTo(dateTime) >= 0 
								&& pServ.getServizio().getNome().equals(servizio.getNome())) {
		
							totaleServizio += pServ.getPrezzo();
							trovato = true;
							break;
						}
					}
				}
				
				if(trovato == false) totaleServizio += this.prezzoBaseServizi;
			}
			
		} catch (SQLException e) {
		e.printStackTrace();
		}
		
		if(totaleServizio == 0) return numeroServizi* this.prezzoBaseServizi;
		
		else return totaleServizio;
	}

	//versione semplificata: prende la stagione nella quale si trova la data di fine
	//il formatter della data passata in ingresso deve essere dd/MM/yyyy
	private float calcolaTotaleOmbrellone(Ombrellone o, Date dataFine, int numGiorni) {
		
		for(Prezzo p: prezzi.getPrezzi()) {
			
			if(p.getClass() == PrezzoOmbrellone.class) {
				
				PrezzoOmbrellone pOmb = (PrezzoOmbrellone)p;
					
				if(pOmb.getStagione().getDataInizio().compareTo(dataFine) <= 0 && 
						pOmb.getStagione().getDataFine().compareTo(dataFine) >= 0 && 
						pOmb.getFilaInizio() <= o.getNumeroRiga() && pOmb.getFilaFine() >= o.getNumeroRiga()) {
					return pOmb.getPrezzo()*numGiorni;
				}
			}
		}
		
		return prezzoBaseOmbrelloni*numGiorni;
	}

}
