package ombrelloniani.view.fxmlControllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Popup;
import ombrelloniani.controller.DisponibilitaController;
import ombrelloniani.controller.TerminaController;
import ombrelloniani.controller.interfaces.IControllerDisponibilita;
import ombrelloniani.controller.interfaces.IControllerTermina;
import ombrelloniani.view.VistaNavigator;
import ombrelloniani.view.interfaces.IViewDisponibilita;
import ombrelloniani.view.interfaces.IViewTermina;

public class ControlloDisponibilita extends FXMLController implements IViewDisponibilita {
	@FXML private DatePicker dataInizio;
	@FXML private DatePicker dataFine;
	@FXML private GridPane mappaOmbrelloniView;

	private IControllerDisponibilita controller;
	private int[] ombrelloneSelezionato = null;
	private Image ombLibero = new Image(
			getClass().getResourceAsStream("/ombrelloniani/view/images/ombrelloneLibero.png"));
	private Image ombOccupato = new Image(
			getClass().getResourceAsStream("/ombrelloniani/view/images/ombrelloneOccupato.png"));

	private Map<ImageView, int[]> mappaOmbrelloni = new HashMap<ImageView, int[]>();

	public ControlloDisponibilita() {
		this.controller = new DisponibilitaController(this);
	};

	/**
	 * Aggiunta degli ombrelloni alla mappa. Initialize viene invocato al termine
	 * del caricamento del contenuto del file fxml.
	 */
	@FXML
	private void initialize() {

		// Inizializzo le date
		dataInizio.setValue(LocalDate.now());
		dataFine.setValue(LocalDate.now());

		// Ottengo tutti gli ombrelloni dello stabilimento
		List<int[]> ombrelloniTotali = controller.getOmbrelloni();

		// Popolo mappa con chiave [riga, colonna] e valore ImageView
		for (int[] posizioneOmbrellone : ombrelloniTotali) {
			ImageView iv = new ImageView();
			iv.setFitWidth(60);
			iv.setPreserveRatio(true);
			iv.setCache(true);

			// Associo evento click
			iv.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					handleStatoOmbrellone(event);
				}
			});

			// Inserimento ombrellone nella mappa
			mappaOmbrelloni.put(iv, posizioneOmbrellone);

			// Mostro ombrelloni (-1 perché parte da 1), prima colonna poi riga
			mappaOmbrelloniView.add(iv, posizioneOmbrellone[1] - 1, posizioneOmbrellone[0] - 1);

			/*
			 * NON VA SFORTUNATAMENTE // Associo dimensioni per farli stare tutti nella
			 * mappa DoubleBinding dimOmbrHor = ((Pane)
			 * mappaOmbrelloniView.getParent()).widthProperty()
			 * .divide(ombrelloniTotali.get(ombrelloniTotali.size() - 1)[0]); DoubleBinding
			 * dimOmbrVer = ((Pane) mappaOmbrelloniView.getParent()).heightProperty()
			 * .divide(ombrelloniTotali.get(ombrelloniTotali.size() - 1)[1]);
			 * 
			 * 
			 * iv.fitWidthProperty().bind(Bindings.when(dimOmbrHor.lessThan(dimOmbrVer)).
			 * then(dimOmbrHor).otherwise(dimOmbrVer));
			 */
		}

		// Calcolo spazio dinamico tra gli ombrelloni
		DoubleBinding hgap = ((Pane) mappaOmbrelloniView.getParent()).widthProperty()
				.subtract(mappaOmbrelloniView.widthProperty()).divide(50);
		DoubleBinding vgap = ((Pane) mappaOmbrelloniView.getParent()).heightProperty()
				.subtract(mappaOmbrelloniView.heightProperty()).divide(30);

		mappaOmbrelloniView.hgapProperty().bind(Bindings.when(hgap.lessThan(30)).then(hgap).otherwise(30));
		mappaOmbrelloniView.vgapProperty().bind(Bindings.when(vgap.lessThan(30)).then(vgap).otherwise(30));

		// Ricevo ombrelloni occupati di data corrente da mostrare e aggiorno la mappa
		List<int[]> ombOccupati = controller.mostraStatoSpiaggia();

		aggiornaMappa(ombOccupati);

	}

	private void aggiornaMappa(List<int[]> ombrelloniOccupati) {
		// Tutti gli ombrelloni liberi
		for (ImageView iv : mappaOmbrelloni.keySet()) {
			iv.setImage(ombLibero);
		}

		// Cambia ombrelloni occupati
		ImageView iv = null;
		for (int[] ombrelloneOccupato : ombrelloniOccupati) {
			if ((iv = getImageViewByPosition(mappaOmbrelloni, ombrelloneOccupato)) != null) {
				iv.setImage(ombOccupato);
			}
		}
	}

	/**
	 * Ottiene la chiave da una mappa dato il valore. Ritorna il primo risultato in
	 * quanto la mappa è 1-1 (bidirezionale).
	 * 
	 * Altre soluzioni utilizzano librerie esterne (bidimap Apache o Google Guava)
	 */
	private ImageView getImageViewByPosition(Map<ImageView, int[]> map, int[] value) {
		for (Entry<ImageView, int[]> entry : map.entrySet()) {
			if ((value[0] == entry.getValue()[0]) && (value[1] == entry.getValue()[1])) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * Controlla che la data di fine sia dopo la data di inizio.
	 */
	private boolean dateOK() {
		if (getDataFine().compareTo(getDataInizio()) < 0) {
			showError("Date non valide", "Data fine precedente data inizio");
			return false;
		}
		return true;
	}

	/**
	 * Ottiene gli ombrelloni occupati per le date selezionate e li mostra a video.
	 */
	@FXML
	private void handleRicerca(ActionEvent event) {
		if (dateOK()) {
			List<int[]> ombOccupati = controller.mostraStatoSpiaggia();
			aggiornaMappa(ombOccupati);
		}
	}

	/**
	 * Richiama la visualizzazione dello stato dell'ombrellone. Ottiene i dati e
	 * carica il popup.
	 */
	@FXML
	private void handleStatoOmbrellone(MouseEvent event) {
		ombrelloneSelezionato = mappaOmbrelloni.get(event.getSource());
		String[] content = controller.mostraStatoOmbrellone();

		if (content != null) {
			Popup info = new Popup();
			info.getContent().add(new Popup_InfoOmbrellone(content));
			info.show(((ImageView) event.getSource()).getScene().getWindow(), event.getScreenX(), event.getScreenY());
			info.setAutoHide(true);
		}
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
		return ombrelloneSelezionato;
	}

}
