package ombrelloniani.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
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
import ombrelloniani.view.interfaces.IViewTermina;

import java.util.Date;

public class TerminaController implements IController, IControllerTermina{
	
	private ListaPrezzi prezzi = ListaPrezzi.getListaPrezzi();
	private ListaConvenzioni convenzioni = ListaConvenzioni.getListaConvenzioni();
	private List<String[]> convenzioniString = new ArrayList<String[]>();
	private ListaFedelta fedelta = ListaFedelta.getListaFedelta();
	private String nomeFedelta;
	private Ricevuta ricevuta;
	private Convenzione convenzione;
	private PrenotazioneTerminata prenotazioneTerminata;
	private Prenotazione prenotazione;
	private List<Prenotazione> prenotazioni;
	
	//Controller
	private GestionePrenotazioneController controller = new GestionePrenotazioneController();
	private ModificaPrenotazioneController modController = new ModificaPrenotazioneController();
	
	private float prezzoBaseOmbrelloni = 10;
	private float prezzoBaseServizi = 10;
	//View
	private IViewTermina viewTermina;
	
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
		
	private static String termina_ombrelloniPrenotazione =
			"DELETE FROM OMBRELLONIPRENOTAZIONE " +
			"WHERE idPrenotazione = ? "
			;
	
	private static String termina_serviziPrenotazione =
			"DELETE FROM SERVIZIPRENOTAZIONE " +
			"WHERE idPrenotazione = ? "
			;
	
	
	//Costruttore deve prendere in ingresso la view sulla quale richiamare i metodi
	public TerminaController(IViewTermina viewTermina) {
		this.viewTermina = viewTermina;
	}
	
	//Metodi della classe
	public void cercaPrenotazione(int idPren) {
		prenotazione = controller.cercaPrenotazione(idPren);
		
		//in caso di prenotazione non trovata invio una notifica al cliente
		if(prenotazione.getCliente() == null) {
			viewTermina.showError("idPrenotazione non esistente", "L'ID inserito non corrisponde a nessuna prenotazione");
			return;
		}
		
		if(prenotazione.getDataInizio().compareTo(Date.from(Instant.now())) > 0) {
			viewTermina.showError("Prenotazione non valida", "Prenotazione selezionata non è ancora iniziata");
			return;
		}
		
		if(prenotazione.getDataFine().compareTo(Date.from(Instant.now())) > 0) {
			prenotazione = modController.modificaDataFine(prenotazione, Date.from(Instant.now()));
		}
		
		//aggiornamento delle liste da spostare nella main view
		controller.aggiornaListaConvenzioni();
		controller.aggiornaListaFedelta();
		controller.aggiornaListaPrezzi();
		
		mostraPrenotazione();
	}
	
	//cerco tutte le prenotazioni con nome e cognome dati
	
	public void cercaPrenotazioni(String nome, String cognome) {
		List<Prenotazione> pren =  controller.cercaPrenotazioni(nome, cognome);
		prenotazioni = new ArrayList<Prenotazione>();
		
		for(Prenotazione p : pren) {
			//le prenotazioni non ancora iniziate non vengono mostrate nella terminazione
			if(p.getDataInizio().compareTo(Date.from(Instant.now())) < 0) {
				prenotazioni.add(p);
			}
		}
		
		if(prenotazioni.size() == 0) {
			viewTermina.showError("Prenotazioni non presenti", "Non sono presenti prenotazioni terminabili per il cliente : " + nome.toUpperCase() + " " +
						cognome.toUpperCase());
			return;
		}
		
		//aggiornamento delle liste da spostare nella main view
		controller.aggiornaListaConvenzioni();
		controller.aggiornaListaFedelta();
		controller.aggiornaListaPrezzi();
		
		//int numeroPrenotazioni = prenotazioni.size();
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
		
		viewTermina.aggiornaListaPrenotazioniDisponibili(prenotazioni);
	}

	public void prenotazioneSelezionata(int index) {
		prenotazione = prenotazioni.get(index);
		
		if(prenotazione.getDataFine().compareTo(Date.from(Instant.now())) > 0) {
			prenotazione = modController.modificaDataFine(prenotazione, Date.from(Instant.now()));
		}
		
		mostraPrenotazione();
	}
	
	private void mostraPrenotazione() {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		viewTermina.setDataInizio(formatter.format(this.prenotazione.getDataInizio()));
		viewTermina.setDataFine(formatter.format(this.prenotazione.getDataFine()));
		viewTermina.setNumeroLettini(prenotazione.getNumeroLettini());
		for(Ombrellone o : prenotazione.getOmbrelloni()) {
			viewTermina.addOmbrelloneToList(o.getIdOmbrellone());
		}
		
		for(Servizio s : prenotazione.getServizi()) {
			viewTermina.addServizioToList(s.getNome());
		}
		
		mostraRicevuta(); //ancora non contiene la convenzione
	}
	
	//Metodi richiamati al click del bottone
	public void terminaPrenotazione() {
		
		this.prenotazioneTerminata = new PrenotazioneTerminata(prenotazione);
		
		prenotazioneTerminata.setSaldo(this.calcolaSaldo());
		
		aggiornaDataBase();
		controller.writeLog("prenotazione " + prenotazione.getIdPrenotazione()
				+ "terminata alle " + Date.from(Instant.now()));
		
		viewTermina.confermaTerminazione();
		
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
	
	private void creaRicevuta() {
		ricevuta = new Ricevuta();
		
		ricevuta.setEntries(calcolaEntryRicevuta());
		ricevuta.setPercentualeSconto(calcolaSconto());
		ricevuta.setPercentualeConvenzione(0); // viene aggiunta successivamente
		ricevuta.setPrezzoTotale();
		
	}

	//funzione manca al controllo
	private void mostraRicevuta() {
		creaRicevuta();
		List<String[]> entriesRicevuta = new ArrayList<String[]>();
		String[] entry;
		
		for(EntryRicevuta e: this.ricevuta.getEntries()) {
			
			entry = new String[3];
			entry[0] = e.getTipoPrezzo() + " " + e.getDescrizione();
			if(e.getTipoPrezzo().equalsIgnoreCase("Servizio")) entry[1] = "";
			else entry[1] = Integer.toString(e.getGiorni());
			entry[2] = Float.toString(e.getTotale());
			
			entriesRicevuta.add(entry);
		}
		
		//Stringa vuota di intermezzo 
		entry = new String[3];
		entry[0] = "";
		entry[1] = "";
		entry[2] = "";
		
		entriesRicevuta.add(entry);
		
		entry = new String[3];
		entry[1] = "";
		if(this.ricevuta.getPercentualeSconto() == 0) {
			entry[0] = "Sconto - non presente";
			entry[2] = "";
		}
		else {
			entry[0] = "Sconto " + this.ricevuta.getPercentualeSconto()*100 + "% - " + this.nomeFedelta;
			entry[2] = Double.toString(this.ricevuta.getPercentualeSconto());
		}
		
		entriesRicevuta.add(entry);
		
		entry = new String[3];
		entry[1] = "";
		if(this.ricevuta.getPercentualeConvenzione() == 0) {
			entry[0] = "Convenzione - non presente";
			entry[2] = "";
		}
		else {
			entry[0] = "Convenzione - " + this.convenzione.getNome() + " - " + Double.toString(this.convenzione.getSconto()*100) + "%";
			entry[2] = "- " + Double.toString(this.ricevuta.getPrezzoTotale()*this.convenzione.getSconto());
		}
		
		entriesRicevuta.add(entry);
		
		entry = new String[3];
		
		entry[0] = "TOTALE";
		entry[1] = "";
		entry[2] = Float.toString(this.calcolaSaldo());
		
		entriesRicevuta.add(entry);
		
		viewTermina.aggiornaTabellaRicevuta(entriesRicevuta);
	}

	//mostraConvenzioni mostra solo le convenzioni attualmente disponibili confrontandole con la data odierna
	public void mostraConvenzioni() {
		
		convenzioniString = new ArrayList<String[]>();
		String[] convenzione;
		Date dNow = new Date();
		
		for(Convenzione c : this.convenzioni.getConvenzioni()) {
			if(c.getDataInizio().compareTo(dNow) <= 0 && c.getDataFine().compareTo(dNow) >= 0) {
				convenzione = new String[3];
				convenzione[0] = c.getEnte().getNomeEnte();
				convenzione[1] = c.getNome();
				convenzione[2] = Double.toString(c.getSconto());
					
				convenzioniString.add(convenzione);
			}
		}

		viewTermina.aggiornaListaConvenzioni(convenzioniString);
	}

	//index fa riferimento alla posizione nella lista di convenzioniString
	public void convenzioneSelezionata(int index) {
		
		String nomeConvenzione = (convenzioniString.get(index))[0];
		ricevuta.setPercentualeConvenzione(Double.parseDouble((convenzioniString.get(index))[3]));
		for(Convenzione c: this.convenzioni.getConvenzioni())
			if(c.getNome().equals(nomeConvenzione)) {
				this.convenzione = c;
				break;
			}
		
		mostraRicevuta(); //seleziono la convenzione e riaggiorno la tabella
	}
	
	private float calcolaSaldo() {
		
		System.out.println("Sconto fedeltà " + ricevuta.getPercentualeSconto());
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
						this.nomeFedelta = f.getNome();
					}
				}
			}
		}
		
		return sconto;
	}
	
	//attenzione nella entry relativa al prezzo del servizio il giorno ï¿½ a 0!
	private List<EntryRicevuta> calcolaEntryRicevuta() {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		List<EntryRicevuta> entries = new ArrayList<EntryRicevuta>();
		EntryRicevuta entry;
		/*int numGiorni = 0;
		Connection connection = controller.getConnection();

		try {
			PreparedStatement pstm = connection.prepareStatement(numGiorni_prenotazione);
			pstm.setString(1, formatter.format(this.prenotazione.getDataFine()));
			pstm.setString(2, formatter.format(this.prenotazione.getDataInizio()));
			ResultSet rs = pstm.executeQuery();
			numGiorni = rs.getInt(1) + 1;
				
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		String inizio = formatter.format(this.prenotazione.getDataInizio());
		String fine   = formatter.format(this.prenotazione.getDataFine());
		LocalDate localInizio = LocalDate.parse(inizio);
		LocalDate localFine   = LocalDate.parse(fine);
		int numGiorni = Period.between(localInizio, localFine).getDays() + 1;
	
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
	
	private float calcolaTotaleServizio(Servizio servizio) {
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Connection connection = controller.getConnection();
		PreparedStatement pstm;
		float totaleServizio = 0;
		int numeroServizi = 0;
		boolean trovato = false;
		prezzi = ListaPrezzi.getListaPrezzi();
		
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
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(totaleServizio == 0) return numeroServizi* this.prezzoBaseServizi;
		
		else return totaleServizio;
	}

	//versione semplificata: prende la stagione nella quale si trova la data di fine
	//il formatter della data passata in ingresso deve essere dd/MM/yyyy
	private float calcolaTotaleOmbrellone(Ombrellone o, Date dataFine, int numGiorni) {
		prezzi = ListaPrezzi.getListaPrezzi();
		List<Prezzo> lp = prezzi.getPrezzi();
		for(Prezzo p: lp) {
			
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
