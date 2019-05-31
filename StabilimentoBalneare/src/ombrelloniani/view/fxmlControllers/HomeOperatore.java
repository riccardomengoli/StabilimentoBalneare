package ombrelloniani.view.fxmlControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import ombrelloniani.view.VistaNavigator;

public class HomeOperatore extends FXMLController {
	@FXML private Button logoutButton;
	@FXML private Button gestionePren;
	@FXML private Button controlloDisp;

	public HomeOperatore() {};
	
	/**
	 * Evento al click di logout.
	 * Ritorno alla view di login.
	 */
	@FXML
	private void handleLogout (ActionEvent event) {
		
		VistaNavigator.loadView(VistaNavigator.LOGIN);
	}
	
	/**
	 * Evento al click del bottone di gestione prenotazione.
	 * Cambio scena.
	 */
	@FXML
	private void handleGestionePren (ActionEvent event) {
		
		VistaNavigator.loadView(VistaNavigator.GESTIONEPRENOTAZIONE);
	}
	
	/**
	 * Evento al click del bottone di controllo disponibilità.
	 * Cambio scena.
	 */
	@FXML
	private void handleControlloDisp (ActionEvent event) {
		
		//VistaNavigator.loadView(VistaNavigator.CONTROLLODISPONIBILITA);
	}
	
	/**
	 * Evento all'entrata del mouse sopra i bottoni del menu.
	 * Imposta il colore.
	 */
	@FXML
	private void handleHoverInButton (MouseEvent event) {
		((Button) event.getSource()).setStyle("-fx-background-color: #4449d9;");
	}
	
	/**
	 * Evento all'uscita del mouse dai bottoni del menu.
	 * Resetta il colore principale.
	 */
	@FXML
	private void handleHoverOutButton (MouseEvent event) {
		((Button) event.getSource()).setStyle("");
	}
}
