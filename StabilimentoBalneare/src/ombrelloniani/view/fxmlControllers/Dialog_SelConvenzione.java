package ombrelloniani.view.fxmlControllers;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ombrelloniani.controller.interfaces.IControllerTermina;
import ombrelloniani.view.VistaNavigator;

public class Dialog_SelConvenzione extends VBox {

	@FXML private ChoiceBox<String> listaConvenzioni;
	private ObservableList<String> convenzioni = FXCollections.observableArrayList();
	private IControllerTermina controller;

	public Dialog_SelConvenzione(IControllerTermina controller) {
		this.controller = controller;
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/ombrelloniani/view/fxml/Dialog_SelConvenzione.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Carico le convenzioni nella lista.
	 */
	@FXML
	private void initialize() {
		listaConvenzioni.setItems(convenzioni);
	}

	@FXML
	private void handleConfermaConvenzione(ActionEvent event) {
		if (getConvenzioneSelezionata() >= 0) {
			controller.convenzioneSelezionata(getConvenzioneSelezionata());
		}

		Stage stage = (Stage) listaConvenzioni.getScene().getWindow();
		stage.close();
	}

	public void aggiornaListaConvenzioni(List<String[]> convenzioni) {
		this.convenzioni.clear();
		for (String[] convenzione : convenzioni) {
			Double valoreSconto = Double.parseDouble(convenzione[2]);
			this.convenzioni.add(convenzione[0] + ", " + convenzione[1] + ", " +  String.valueOf((int) (valoreSconto*100)) + "%");
		}
	}

	// Ritorna -1 se non è selezionato niente
	public int getConvenzioneSelezionata() {
		return listaConvenzioni.getSelectionModel().getSelectedIndex();
	}

	public void showDialog() {
		// Richiedo lista convenzioni al controller, che poi manda risultati a termina
		// prenotazione, che inoltra a me
		// Non in initialize perché oggetto in termina non è ancora stato creato
		controller.mostraConvenzioni();
		VistaNavigator.loadPopup(this, "Seleziona convenzione");
	}
}
