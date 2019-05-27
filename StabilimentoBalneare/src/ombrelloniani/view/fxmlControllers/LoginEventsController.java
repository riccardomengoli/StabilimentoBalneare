package ombrelloniani.view.fxmlControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import ombrelloniani.controller.interfaces.IControllerLogin;
import ombrelloniani.model.db.ControllerLogin;

public class LoginEventsController {
	@FXML private TextField username;
	@FXML private PasswordField password;
	@FXML private Button loginButton;
	private IControllerLogin controller;

	public LoginEventsController() {
		this.controller = new ControllerLogin();
	}
	
	/*
	 * Evento al click di login.
	 * Verifica delle credenziali di login su controller, se successo cambia view, se fallisce popup.
	 */
	@FXML
	private void handleLoginButtonClick (ActionEvent event) {
		
		//Controlli se valori presenti
		Window owner = loginButton.getScene().getWindow();
        if(username.getText().isEmpty()) {
            AlertHelper.showAlert(AlertType.ERROR, owner, "Dati mancanti", 
                    "Inserisci l'username");
            return;
        }
        if(password.getText().isEmpty()) {
            AlertHelper.showAlert(AlertType.ERROR, owner, "Dati mancanti", 
                    "Inserisci la password");
            return;
        }
		
		//Chiama login del controller
		controller.verificaCredenziali(username.getText(), password.getText());
	}
	
	/*
	 * Evento all'entrata del mouse sopra il bottone di login.
	 * Imposta il colore.
	 */
	@FXML
	private void handleHoverInButton (MouseEvent event) {
		loginButton.setStyle("-fx-background-color: #4449d9;");
	}
	
	/*
	 * Evento all'uscita del mouse dal bottone di login.
	 * Resetta il colore principale.
	 */
	@FXML
	private void handleHoverOutButton (MouseEvent event) {
		loginButton.setStyle("");
	}
	
}
