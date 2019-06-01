package ombrelloniani.view.fxmlControllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ombrelloniani.controller.interfaces.IControllerTermina;
import ombrelloniani.view.VistaNavigator;

public class Dialog_SelPrenByID extends VBox {

	@FXML private TextField idPrenotazione;
	private IControllerTermina controller;

	/*
	 * PER FUTURO USARE INTERFACCIA ICONTROLLERRICERCA IMPLEMENTATA DA TERMINA,
	 * CANCELLA, MODIFICA
	 */
	public Dialog_SelPrenByID(IControllerTermina controller) {
		this.controller = controller;
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/ombrelloniani/view/fxml/Dialog_SelPrenByID.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleRicerca(ActionEvent event) {
		controller.cercaPrenotazione(getIdPrenotazione());

		Stage stage = (Stage) idPrenotazione.getScene().getWindow();
		stage.close();

	}

	public int getIdPrenotazione() {
		try {
			return Integer.parseInt(this.idPrenotazione.getText());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public void showDialog() {
		VistaNavigator.loadPopup(this, "Cerca per ID prenotazione");
	}
}
