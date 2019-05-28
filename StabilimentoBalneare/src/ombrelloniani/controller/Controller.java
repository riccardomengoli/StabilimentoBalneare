package ombrelloniani.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Controller {
	
	private Connection dbConnection;
	private Writer logWriter;
	private static String path = "C:/Users/miche/Desktop/fileLog";
	
	public Controller() {};
	
	public synchronized Connection getConnection() {
		
		if(this.dbConnection == null)
			this.dbConnection = openConnection();
		
		return this.dbConnection;
	}

	private Connection openConnection() {
		
		Connection conn = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/miche/Desktop/sample.db");
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public synchronized void writeLog(String string) {
		
		if(this.logWriter == null)
			this.logWriter = openLogOperation(path);
		
		try {
			this.logWriter.append(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Writer openLogOperation(String string) {
		
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(string);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fw;
		
	}
	
	public synchronized Reader getLogReader() {
		
		FileReader fr = null;
		try {
			fr = new FileReader(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fr;
	}
	
	
}
