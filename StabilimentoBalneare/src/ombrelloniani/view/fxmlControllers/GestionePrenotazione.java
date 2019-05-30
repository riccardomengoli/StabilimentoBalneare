package ombrelloniani.view.fxmlControllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import ombrelloniani.view.VistaNavigator;

public class GestionePrenotazione extends FXMLController {
	@FXML private Button backButton;
	@FXML private Button nuovaPren;
	@FXML private Button modificaPren;
	@FXML private Button terminaPren;
	@FXML private Button cancellaPren;
	@FXML private Line hr1;

	public GestionePrenotazione() {
	};

	/**
	 * Lega la lunghezza della linea di separazione alla lunghezza del suo
	 * container, necessario in quanto gli oggetti Shape non sono resizable. Vengono
	 * aggiunti 0.5 px alla coordinata y per ottenere un effetto di pixel pieno,
	 * evitando aliasing (Javadoc Shape). Initialize viene invocato al termine del
	 * caricamento del contenuto del file fxml.
	 */
	@FXML
	private void initialize() {
		Pane parent = (Pane) hr1.getParent();
		hr1.startXProperty().bind(new SimpleDoubleProperty(50));
		hr1.endXProperty().bind(parent.widthProperty().subtract(50));
		// Evito aliasing
		hr1.setStartY(0.5);
		hr1.setEndY(0.5);
	}

	/**
	 * Evento al click del bottone di nuova prenotazione. Cambio scena.
	 */
	@FXML
	private void handleNuovaPren(ActionEvent event) throws Exception {

		VistaNavigator.loadView(VistaNavigator.CREAPRENOTAZIONE);
	}

	/**
	 * Evento al click del bottone di modifica prenotazione. Cambio scena.
	 */
	@FXML
	private void handleModificaPren(ActionEvent event) throws Exception {

		// VistaNavigator.loadView(VistaNavigator.MODIFICAPRENOTAZIONE);
	}

	/**
	 * Evento al click del bottone di termina prenotazione. Cambio scena.
	 */
	@FXML
	private void handleTerminaPren(ActionEvent event) throws Exception {

		// VistaNavigator.loadView(VistaNavigator.TERMINAPRENOTAZIONE);
	}

	/**
	 * Evento al click del bottone di cancella prenotazione. Cambio scena.
	 */
	@FXML
	private void handleCancellaPren(ActionEvent event) throws Exception {

		// VistaNavigator.loadView(VistaNavigator.CANCELLAPRENOTAZIONE);
	}

	/**
	 * Evento all'entrata del mouse sopra i bottoni del menu. Imposta il colore.
	 */
	@FXML
	private void handleHoverInButton(MouseEvent event) {
		((Button) event.getSource()).setStyle("-fx-background-color: #4449d9;");
	}

	/**
	 * Evento all'uscita del mouse dai bottoni del menu. Resetta il colore
	 * principale.
	 */
	@FXML
	private void handleHoverOutButton(MouseEvent event) {
		((Button) event.getSource()).setStyle("");
	}

	/**
	 * Evento al click di indietro. Ritorno alla view precedente.
	 */
	@FXML
	private void handleBack(ActionEvent event) throws Exception {
		if (getCallingScene() != null) {
			VistaNavigator.loadView(getCallingScene());
		}
	}
}
