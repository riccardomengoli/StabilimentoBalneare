package ombrelloniani.model.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import ombrelloniani.view.VistaNavigator;
import ombrelloniani.view.fxmlControllers.LoginEventsController;

public class ControllerLogin {
	
	private LoginEventsController viewLogin;
	private FileReader reader;
	private static String filePath = "C:/Users/miche/Desktop/filePassword.txt";
	
	public ControllerLogin(LoginEventsController viewLogin) {
		this.viewLogin = viewLogin;
	}
	
	private void openCredenziali() {
		
		try {
			
			if(this.reader == null)
				this.reader = new FileReader(filePath);
				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	

	
	public void verificaCredenziali(String username,String password) {
		
		if(!sanitificazione(username + password)) {
			//throw new CredenzialiNonCorretteException("")
		}
		
		try {
			openCredenziali();
			StringTokenizer str;
			String line;
			String ruolo = null;
			BufferedReader bf = new BufferedReader(this.reader);
			
			while((line = bf.readLine()) != null) {
				str = new StringTokenizer(line);
				ruolo = str.nextToken();
				if(username.equals(str.nextToken()) && password.equals(str.nextToken())) {
					this.reader.close();
					break;
				}
			}
			
			if(ruolo.equals("gestore")) {
				VistaNavigator.loadView(VistaNavigator.HOMEGESTORE);
			}
			
			else if(ruolo.equals("operatore")) {
				VistaNavigator.loadView(VistaNavigator.HOMEOPERATORE);
			}
			
			else {
				//throw new OperatoreNonTrovatoException()
			}
			
			this.reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	private boolean sanitificazione(String str) {
		boolean result = false;
		if(str.length() < 80)
			result = true;
		
		if(str.contains("|") || str.contains("<") || str.contains(">") || str.contains(" ") || str.contains(";"))
			return false;
		
		return result;
	}

}
