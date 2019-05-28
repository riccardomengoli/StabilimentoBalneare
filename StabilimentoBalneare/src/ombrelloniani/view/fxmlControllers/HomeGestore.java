package ombrelloniani.view.fxmlControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import ombrelloniani.view.VistaNavigator;

public class HomeGestore extends FXMLController {
	@FXML private Button logoutButton;
	@FXML private Button gestionePren;
	@FXML private Button controlloDisp;
	@FXML private Button impostazioniStab;
	@FXML private Button resocontoStag;
	
	public HomeGestore() {};
	
	/**
	 * Evento al click di logout.
	 * Ritorno alla view di login.
	 */
	@FXML
	private void handleLogoutButtonClick (ActionEvent event) throws Exception {
		
		VistaNavigator.loadView(VistaNavigator.LOGIN);
	}
	
	/**
	 * Evento al click del bottone di gestione prenotazione.
	 * Cambio scena.
	 */
	@FXML
	private void handleGestionePrenButtonClick (ActionEvent event) throws Exception {
		
		VistaNavigator.loadView(VistaNavigator.GESTIONEPRENOTAZIONE);
	}
	
	/**
	 * Evento al click del bottone di controllo disponibilit�.
	 * Cambio scena.
	 */
	@FXML
	private void handleControlloDispButtonClick (ActionEvent event) throws Exception {
		
		//VistaNavigator.loadView(VistaNavigator.CONTROLLODISPONIBILITA);
	}
	
	/**
	 * Evento al click del bottone di impostazioni stabilimento.
	 * Cambio scena.
	 */
	@FXML
	private void handleImpostazioniStabButtonClick (ActionEvent event) throws Exception {
		
		//VistaNavigator.loadView(VistaNavigator.IMPOSTAZIONISTABILIMENTO);
	}
	
	/**
	 * Evento al click del bottone di resoconto stagionale.
	 * Cambio scena.
	 */
	@FXML
	private void handleResocontoStagButtonClick (ActionEvent event) throws Exception {
		
		//VistaNavigator.loadView(VistaNavigator.RESOCONTOSTAGIONALE);
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