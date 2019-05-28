package ombrelloniani.view.fxmlControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import ombrelloniani.controller.exceptions.UserNotFoundException;
import ombrelloniani.controller.interfaces.IControllerLogin;
import ombrelloniani.model.db.ControllerLogin;

public class Login extends FXMLController {
	@FXML private TextField username;
	@FXML private PasswordField password;
	@FXML private Button loginButton;
	private IControllerLogin controller;

	public Login() {
		this.controller = new ControllerLogin();
	}

	/**
	 * Evento al click di login o invio nei due campi username e password. Verifica
	 * delle credenziali di login su controller, se successo cambia view, se
	 * fallisce popup.
	 */
	@FXML
	private void handleLoginButtonClick(ActionEvent event) {

		// Controlli se valori presenti
		Window owner = loginButton.getScene().getWindow();
		if (username.getText().isEmpty()) {
			AlertHelper.showAlert(AlertType.ERROR, owner, "Dati mancanti", "Inserisci l'username");
			return;
		}
		if (password.getText().isEmpty()) {
			AlertHelper.showAlert(AlertType.ERROR, owner, "Dati mancanti", "Inserisci la password");
			return;
		}

		// Chiama login del controller con gestione di eccezioni
		try {
			controller.verificaCredenziali(username.getText(), password.getText());
		} catch (IllegalArgumentException e) {
			AlertHelper.showAlert(AlertType.ERROR, owner, "Login fallito", e.getMessage());
			password.clear();
		} catch (UserNotFoundException e) {
			AlertHelper.showAlert(AlertType.ERROR, owner, "Login fallito", e.getMessage());
			password.clear();
		}

	}

	/**
	 * Evento all'entrata del mouse sopra il bottone di login. Imposta il colore.
	 */
	@FXML
	private void handleHoverInButton(MouseEvent event) {
		loginButton.setStyle("-fx-background-color: #4449d9;");
	}

	/**
	 * Evento all'uscita del mouse dal bottone di login. Resetta il colore
	 * principale.
	 */
	@FXML
	private void handleHoverOutButton(MouseEvent event) {
		loginButton.setStyle("");
	}

}
