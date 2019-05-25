package ombrelloniani.model.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
 
public class CreateDB {
	
	static final String createCliente = 
    	"CREATE " +
    		"TABLE CLIENTI ( " +
   				"documento VARCHAR(50) NOT NULL PRIMARY KEY, " +
   				"nome VARCHAR(40) NOT NULL, " + 
   				"cognome VARCHAR(40) NOT NULL, " +
    			"email VARCHAR(100), " +
    			"telefono VARCHAR(20) ) "
   	;
	
	static final String createOmbrellone = 
	    "CREATE " +
	   		"TABLE OMBRELLONI ( " +
	 			"idOmbrellone VARCHAR(5) NOT NULL PRIMARY KEY, " +
	   			"numeroRiga INT NOT NULL, " + 
	   			"numeroColonna INT NOT NULL )"
	;
	
	static final String createPrenotazione = 
		    "CREATE " +
		   		"TABLE PRENOTAZIONI ( " +
		 			"idPrenotazione INT NOT NULL PRIMARY KEY, " +
		   			"dataInizio DATE NOT NULL, " + 
		   			"dataFine DATE NOT NULL, " +
		   			"numeroLettini INT NOT NULL," +
		   			"idCliente VARCHAR(50) NOT NULL," +
		   			"FOREIGN KEY (idCliente) REFERENCES CLIENTI(documento) )"
	;
	
	static final String createPrenotazioniTerminate1 = 
		    "CREATE TABLE PRENOTAZIONI_TERMINATE LIKE PRENOTAZIONI" 
	;
	
	static final String createPrenotazioniTerminate2 = 
		    "ALTER " +
		   		"TABLE PRENOTAZIONI_TERMINATE " +
		 			"ADD saldo DOUBLE NOT NULL"
	;
	
	static final String createServiziPrenotazione = 
		    "CREATE " +
		   		"TABLE SERVIZIPRENOTAZIONE( " +
		 			"idPrenotazione INT NOT NULL, " +
		   			"nomeServizio VARCHAR(50) NOT NULL, " + 
		   			"dataOra DATE NOT NULL, " +
		   			"FOREIGN KEY (idPrenotazione) REFERENCES PRENOTAZIONI(idPrenotazione), " +
		   			"FOREIGN KEY (nomeServizio) REFERENCES SERVIZI(nome), " +
		   			"PRIMARY KEY (idPrenotazione, nomeServizio, dataOra) )"
	;
	
	static final String createServizi = 
		    "CREATE " +
		   		"TABLE SERVIZI ( " +
		 			"nome VARCHAR(50) NOT NULL PRIMARY KEY, " +
		   			"descrizione VARCHAR(100) NOT NULL )"
	;
	
	static final String createFedeltà = 
		    "CREATE " +
		   		"TABLE FEDELTA ( " +
		 			"nome VARCHAR(40) NOT NULL PRIMARY KEY, " +
		   			"giorni INT NOT NULL, " + 
		   			"sconto DOUBLE NOT NULL )"
	;
	
	static final String createConvenzioni = 
		    "CREATE " +
		   		"TABLE CONVENZIONI ( " +
		 			"nome VARCHAR(40) NOT NULL PRIMARY KEY, " +
		   			"dataInizio DATE NOT NULL, " + 
		   			"dataFine DATE NOT NULL, " +
		   			"sconto DOUBLE NOT NULL, " +
		   			"nomeEnte VARCHAR(40) NOT NULL, " +
		   			"FOREIGN KEY (nomeEnte) REFERENCES ENTI_TERZI(nomeEnte) )"
	;
	
	static final String createEntiTerzi = 
		    "CREATE " +
		   		"TABLE ENTI_TERZI ( " +
		 			"nomeEnte VARCHAR(40) NOT NULL PRIMARY KEY, " +
		   			"tipologia VARCHAR(50) NOT NULL )"
	;
	
	static final String createStagioni = 
		    "CREATE " +
		   		"TABLE STAGIONI ( " +
		 			"nomeStagione VARCHAR(40) NOT NULL PRIMARY KEY, " +
		   			"dataInizio DATE NOT NULL, " + 
		   			"dataFine DATE NOT NULL )"
	;
	
	static final String createPrezzi = 
		    "CREATE " +
		   		"TABLE PREZZI ( " +
		 			"id INT AUTOINCREMENT NOT NULL PRIMARY KEY, " +
		   			"prezzo FLOAT NOT NULL, " + 
		   			"nomeStagione VARCHAR(40) NOT NULL, " +
		   			"FOREIGN KEY (nomeStagione) REFERENCES STAGIONI(nomeStagione) )"
	;
	
	static final String createPrezziOmbrelloni1 = 
		    "CREATE TABLE PREZZI_OMBRELLONI LIKE PREZZI" 
	;
	
	static final String createPrezziOmbrellone2 = 
			 "ALTER " +
				   		"TABLE PREZZI_OMBRELLONI " +
				 		"ADD filaInizio INT NOT NULL, " +
				   		"filaFine INT NOT NULL"
	;
	
	static final String createPrezziServizi1 = 
		    "CREATE TABLE PREZZI_SERVIZI LIKE PREZZI" 
	;
	
	static final String createPrezziServizi2 = 
			 "ALTER " +
				   		"TABLE PREZZI_OMBRELLONI " +
				 		"ADD nomeServizio VARCHAR(50) FOREIGN KEY REFERENCES SERVIZI(nome) )"
	
	;

	
	public static void createDB() {
    	
    
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/miche/Desktop/sample.db");
            Statement stmt;
            PreparedStatement pstmt;
            ResultSet rs;

            // creo la tabella
            stmt = conn.createStatement();
 
            // inserisco due record
            
 
            // recupero i dati
            
            rs = stmt.executeQuery("SELECT * from students");
            
            while(rs.next()) {
            	
            	System.out.println("ID: " + rs.getInt("id"));
            	System.out.println("FIRSTNAME: " + rs.getString("firstName"));
            	System.out.println("LASTNAME: " + rs.getString("lastName"));
           }
             
            stmt.close(); // rilascio le risorse
            //pstmt.close(); // rilascio le risorse
            conn.close(); // termino la connessione
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e);
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }
}
