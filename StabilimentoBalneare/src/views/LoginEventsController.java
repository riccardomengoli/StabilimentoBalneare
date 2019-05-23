package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;


public class LoginEventsController {
	@FXML private TextField username;
	@FXML private PasswordField password;
	@FXML private Button loginButton;
	//private ILoginController controller;

	public LoginEventsController() {
		//this.controller = new LoginController(this);
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
		//controller.verificaCredenziali(username.getText(), password.getText());
		System.out.println("Chiamata a funzione login");
	}
	
	@FXML
	private void handleHoverInButton (MouseEvent event) {
		loginButton.setStyle("-fx-background-color: #4449d9;");
	}
	
	@FXML
	private void handleHoverOutButton (MouseEvent event) {
		loginButton.setStyle("");
	}
}
