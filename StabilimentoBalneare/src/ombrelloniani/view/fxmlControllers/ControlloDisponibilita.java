package ombrelloniani.view.fxmlControllers;

import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import ombrelloniani.controller.DisponibilitaController;
import ombrelloniani.controller.TerminaController;
import ombrelloniani.controller.interfaces.IControllerDisponibilita;
import ombrelloniani.controller.interfaces.IControllerTermina;
import ombrelloniani.view.VistaNavigator;
import ombrelloniani.view.interfaces.IViewControlloDisponibilita;
import ombrelloniani.view.interfaces.IViewTermina;

public class ControlloDisponibilita extends FXMLController implements IViewControlloDisponibilita {
	@FXML private DatePicker dataInizio;
	@FXML private DatePicker dataFine;
	@FXML private GridPane mappaOmbrelloni;

	private IControllerDisponibilita controller;

	public ControlloDisponibilita() {
		// XXX this.controller = new DisponibilitaController(this);
	};

	/**
	 * Aggiunta degli ombrelloni alla mappa. Initialize viene invocato al termine
	 * del caricamento del contenuto del file fxml.
	 */
	@FXML
	private void initialize() {
		
		//XXX TEST
		Image image = new Image(getClass().getResourceAsStream("/ombrelloniani/view/images/ombrelloneLibero.png"));
		ImageView iv = new ImageView();
		iv.setImage(image);
		iv.fitWidthProperty().bind(mappaOmbrelloni.widthProperty().divide(20));
		iv.setPreserveRatio(true);
		iv.setCache(true);

		mappaOmbrelloni.add(iv, 0, 0);
	}

	/**
	 * Richiama la visualizzazione degli ombrelloni liberi e occupati.
	 */
	@FXML
	private void handleRicerca(ActionEvent event) {
		controller.mostraStatoSpiaggia();
	}

	/**
	 * Richiama la visualizzazione dello stato dell'ombrellone.
	 */
	@FXML
	private void handleStatoOmbrellone(ActionEvent event) {
		controller.mostraStatoOmbrellone();
	}

	/**
	 * Evento all'entrata del mouse sopra i bottoni. Imposta il colore.
	 */
	@FXML
	private void handleHoverInButton(MouseEvent event) {
		((Button) event.getSource()).setStyle("-fx-background-color: #4449d9;");
	}

	/**
	 * Evento all'uscita del mouse dai bottoni. Resetta il colore principale.
	 */
	@FXML
	private void handleHoverOutButton(MouseEvent event) {
		((Button) event.getSource()).setStyle("");
	}

	/**
	 * Evento al click di indietro. Ritorno alla view precedente.
	 */
	@FXML
	private void handleBack(ActionEvent event) {
		if (getCallingScene() != null) {
			VistaNavigator.loadView(getCallingScene());
		}
	}

	/**
	 * Mostra un popup di errore.
	 * 
	 * @param titolo      Titolo della finestra popup.
	 * @param descrizione Descrizione dell'errore.
	 */
	@Override
	public void showError(String titolo, String descrizione) {
		AlertHelper.showAlert(AlertType.ERROR, dataInizio.getScene().getWindow(), titolo, descrizione);
	}

	@Override
	public LocalDate getDataInizio() {
		if (dataInizio.valueProperty().isNull().get()) {
			return null;
		}
		return dataInizio.getValue();
	}

	@Override
	public LocalDate getDataFine() {
		if (dataFine.valueProperty().isNull().get()) {
			return null;
		}
		return dataFine.getValue();
	}

	@Override
	public int[] getOmbrelloneSelezionato() {
		// TODO return {numero riga, numero colonna}
	}

}
