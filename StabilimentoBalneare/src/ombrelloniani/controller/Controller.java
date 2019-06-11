package ombrelloniani.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Controller {

	private Connection dbConnection;
	private Writer logWriter;
	private String dbPath = getClass().getResource("/resources/database.db").toExternalForm();
	public static String username;

	public Controller() {};

	public static void setUser(String user) {
		username = user;
	}
	
	public synchronized Connection getConnection() {

		if (this.dbConnection == null)
			this.dbConnection = openConnection();

		return this.dbConnection;
	}

	private Connection openConnection() {

		Connection conn = null;

		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return conn;
	}

	public synchronized void writeLog(LocalDateTime data, String user, String operazione, String esito) {
		
		if (this.logWriter == null)
			this.logWriter = openLogOperation();

		try {
			this.logWriter.append(""+ data.toString() + " , " + user + " , " + operazione + " , " + esito + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Writer openLogOperation() {

		File f = null;
		FileWriter fw = null;

		try {
			
			f = new File(getClass().getResource("/resources/log.txt").toURI());
			fw = new FileWriter(f);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return fw;

	}

	public synchronized Reader getLogReader() {

		File f = null;
		FileReader fr = null;
		
		try {
			f = new File(getClass().getResource("/resources/log.txt").toURI());
			fr = new FileReader(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return fr;
	}
}
