package ombrelloniani.model.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ControllerLogin {
	
	public ControllerLogin() {}
	private FileReader reader;
	private static String filePath = "C:/Users/miche/Desktop/filePassword.txt";
	
	private void openCredenziali() {
		
		try {
			
			if(this.reader == null)
				this.reader = new FileReader(filePath);
				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	

	
	public String verificaCredenziali(String username,String password) {
		
		if(!sanitificazione(username + password)) {
			return null;
		}
		
		try {
			openCredenziali();
			StringTokenizer str;
			String line;
			String ruolo;
			BufferedReader bf = new BufferedReader(this.reader);
			
			while((line = bf.readLine()) != null) {
				str = new StringTokenizer(line);
				ruolo = str.nextToken();
				if(username.equals(str.nextToken()) && password.equals(str.nextToken())) {
					this.reader.close();
					return ruolo;
				}
			}
			
			this.reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
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
