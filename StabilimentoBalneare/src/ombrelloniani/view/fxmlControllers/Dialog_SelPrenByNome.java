package ombrelloniani.view.fxmlControllers;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import ombrelloniani.controller.interfaces.IControllerTermina;
import ombrelloniani.view.VistaNavigator;

public class Dialog_SelPrenByNome extends VBox {
	@FXML private TextField nome;
	@FXML private TextField cognome;
	@FXML private Line hr_g1;
	@FXML private Label label_g1;
	@FXML private ListView<String> listaPrenotazioni;

	private IControllerTermina controller;
	private ObservableList<String> prenotazioni = FXCollections.observableArrayList();

	/*
	 * PER FUTURO USARE INTERFACCIA ICONTROLLERRICERCA IMPLEMENTATA DA TERMINA,
	 * CANCELLA, MODIFICA
	 */
	public Dialog_SelPrenByNome(IControllerTermina controller) {
		this.controller = controller;
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/ombrelloniani/view/fxml/Dialog_SelPrenByNome.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void initialize() {
		Pane parent = (Pane) hr_g1.getParent();
		hr_g1.endXProperty().bind(parent.widthProperty().multiply(1));
		// Evito aliasing
		hr_g1.setStartY(0.5);
		hr_g1.setEndY(0.5);
		label_g1.layoutXProperty().bind(hr_g1.startXProperty());
		label_g1.layoutYProperty().bind(hr_g1.startYProperty().subtract(25));

		listaPrenotazioni.setItems(prenotazioni);
	}

	@FXML
	private void handleRicerca(ActionEvent event) {
		controller.cercaPrenotazioni(getNome(), getCognome());

	}

	@FXML
	private void handleSelezione(ActionEvent event) {
		if (getPrenotazioneSelezionata() >= 0) {
			controller.prenotazioneSelezionata(getPrenotazioneSelezionata());

			Stage stage = (Stage) nome.getScene().getWindow();
			stage.close();
		}
	}

	public String getNome() {
		return this.nome.getText();
	}

	public String getCognome() {
		return this.cognome.getText();
	}

	public int getPrenotazioneSelezionata() {
		if (listaPrenotazioni.getSelectionModel().selectedItemProperty().isNull().get()) {
			return -1;
		}
		return listaPrenotazioni.getSelectionModel().getSelectedIndex();
	}

	public void aggiornaListaPrenotazioniDisponibili(List<String[]> prenotazioni) {
		this.prenotazioni.clear();
		for (String[] prenotazione : prenotazioni) {
			this.prenotazioni.add(prenotazione[0] + ", " + prenotazione[1] + ", " + prenotazione[2]);
		}
	}
	
	public void showDialog() {
		VistaNavigator.loadPopup(this, "Cerca per nome cliente");
	}
}
