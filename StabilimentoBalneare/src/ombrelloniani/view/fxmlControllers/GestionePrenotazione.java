package ombrelloniani.view.fxmlControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import ombrelloniani.view.VistaNavigator;

public class GestionePrenotazione extends FXMLController {
	@FXML private Button logoutButton;
	@FXML private Button nuovaPren;
	@FXML private Button modificaPren;
	@FXML private Button terminaPren;
	@FXML private Button cancellaPren;

	public GestionePrenotazione() {};
	
	/**
	 * Evento al click del bottone di nuova prenotazione.
	 * Cambio scena.
	 */
	@FXML
	private void handleNuovaPrenButtonClick (ActionEvent event) throws Exception {
		
		//VistaNavigator.loadView(VistaNavigator.CREAPRENOTAZIONE);
	}
	
	/**
	 * Evento al click del bottone di modifica prenotazione.
	 * Cambio scena.
	 */
	@FXML
	private void handleModificaPrenButtonClick (ActionEvent event) throws Exception {
		
		//VistaNavigator.loadView(VistaNavigator.MODIFICAPRENOTAZIONE);
	}
	
	/**
	 * Evento al click del bottone di termina prenotazione.
	 * Cambio scena.
	 */
	@FXML
	private void handleTerminaPrenButtonClick (ActionEvent event) throws Exception {
		
		//VistaNavigator.loadView(VistaNavigator.TERMINAPRENOTAZIONE);
	}
	
	/**
	 * Evento al click del bottone di cancella prenotazione.
	 * Cambio scena.
	 */
	@FXML
	private void handleCancellaPrenButtonClick (ActionEvent event) throws Exception {
		
		//VistaNavigator.loadView(VistaNavigator.CANCELLAPRENOTAZIONE);
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
	
	/**
	 * Evento al click di indietro.
	 * Ritorno alla view precedente.
	 */
	@FXML
	private void handleBackButtonClick (ActionEvent event) throws Exception {
		if (getCallingScene() != null) {
			VistaNavigator.loadView(getCallingScene());
		}
	}
}
