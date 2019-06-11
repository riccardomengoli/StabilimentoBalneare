package ombrelloniani.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.StringTokenizer;

import ombrelloniani.controller.exceptions.UserNotFoundException;
import ombrelloniani.controller.interfaces.IController;
import ombrelloniani.controller.interfaces.IControllerLogin;
import ombrelloniani.view.VistaNavigator;

public class ControllerLogin implements IControllerLogin,IController {

	private FileReader reader;
	private File filePassword;
	private Controller controller = new Controller();

	public ControllerLogin() {
	};

	private void openCredenziali() {
		try {
			filePassword = new File(getClass().getResource("/resources/password.txt").toURI());
			this.reader = new FileReader(filePassword);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void verificaCredenziali(String username, String password)
			throws IllegalArgumentException, UserNotFoundException {

		if (!sanitificazione(username + password)) {
			throw new IllegalArgumentException("Presenti caratteri vietati");
		}

		try {
			openCredenziali();
			StringTokenizer str;
			String line;
			String ruolo = null;
			String user = null;
			String passwd = null;
			boolean utenteTrovato = false;
			BufferedReader bf = new BufferedReader(this.reader);

			while ((line = bf.readLine()) != null) {
				str = new StringTokenizer(line);
				ruolo = str.nextToken();
				user = str.nextToken();
				passwd = str.nextToken();
				
				if (username.equals(user) && password.equals(passwd)) {
					this.reader.close();
					utenteTrovato = true;
					break;
				}
			}

			this.reader.close();

			if (utenteTrovato) {
				Controller.setUser(user);

				controller.writeLog(LocalDateTime.now(), Controller.username, "Login", "Login effettuato con successo");
				
				if (ruolo.equalsIgnoreCase("gestore")) {
					VistaNavigator.loadView(VistaNavigator.HOMEGESTORE);
				} else if (ruolo.equalsIgnoreCase("operatore")) {
					VistaNavigator.loadView(VistaNavigator.HOMEOPERATORE);
				}
			} else {
				throw new UserNotFoundException("Credenziali non corrette");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean sanitificazione(String str) {
		boolean result = false;
		if (str.length() < 80)
			result = true;

		if (str.contains("|") || str.contains("<") || str.contains(">") || str.contains(" ") || str.contains(";"))
			return false;

		return result;
	}

}
