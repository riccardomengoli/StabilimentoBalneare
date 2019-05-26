package ombrelloniani.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class LoginController {

	//eventualmente criptato
	private File filePassword;

	public LoginController() {
		super();
		this.filePassword = openCredenziali();
	}

	private File openCredenziali() {
		File passwd = new File("~/tmp/passwd.txt");
		
		try { 
			//crea un nuovo file solo se non esiste gia
			passwd.createNewFile();
		} catch (IOException e) {
		      //System.out.println("An error occurred.");
		      e.printStackTrace(); 
		    } 
		
		return passwd;
	}
	
	public void verificaCredenziali(String username, String passwd) {
		
		//sanificazione dell'imput
		if (username.co)
		
		try {
			Scanner myReader = new Scanner(this.filePassword); 
			String user = myReader.next(username + ":" + passwd);
			
			StringTokenizer st = new StringTokenizer(user);
			String role = st.nextToken(":");
			
			if(role.equalsIgnoreCase("operatore")) {
				//fa lo show della view dell'operatore
			}
			else if (role.equalsIgnoreCase("gestore")) {
				// fa lo show della view del gestore
			}
			
			myReader.close();
		} 
		catch (FileNotFoundException e) {
			//System.out.println("An error occurred.");
			e.printStackTrace();		
	    }
		catch (NoSuchElementException e) {
			// da lanciare il pop-up per user sbagliato
			e.printStackTrace();
		}
		
	}
	
	
}
