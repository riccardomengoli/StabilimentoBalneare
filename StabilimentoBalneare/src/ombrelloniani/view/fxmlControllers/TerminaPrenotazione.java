package ombrelloniani.view.fxmlControllers;

import java.util.List;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import ombrelloniani.controller.TerminaController;
import ombrelloniani.controller.interfaces.IControllerTermina;
import ombrelloniani.view.VistaNavigator;
import ombrelloniani.view.interfaces.IViewTermina;

public class TerminaPrenotazione extends FXMLController implements IViewTermina {
	@FXML private TextField dataInizio;
	@FXML private TextField dataFine;
	@FXML private TextField numeroLettini;
	@FXML private ListView<String> listaOmbrelloni;
	@FXML private ListView<String> listaServizi;
	@FXML private Line hr_g1, hr1, hr_g2, hr_g3;
	@FXML private Label label_g1, label_g2, label_g3;
	@FXML private Button btn_g3;
	@FXML private TableView<String[]> tableRicevuta;

	private IControllerTermina controller;
	private ObservableList<String> ombrelloni = FXCollections.observableArrayList();
	private ObservableList<String> servizi = FXCollections.observableArrayList();
	private ObservableList<String[]> ricevuta = FXCollections.observableArrayList();

	private Dialog_SelPrenByNome dialogNome = null;
	private Dialog_SelConvenzione dialogConvenzione = null;

	public TerminaPrenotazione() {
		this.controller = new TerminaController(this);
	};

	/**
	 * Varie impostazioni di setup post caricamento. Initialize viene invocato al
	 * termine del caricamento del contenuto del file fxml.
	 */
	@FXML
	private void initialize() {
		// Setup linee orizzontali e testo sezione
		setFullParentWidthLine(hr_g1, -0.03, 1.03);
		setFullParentWidthLine(hr1, -0.03, 1.03);
		setFullParentWidthLine(hr_g2, -0.03, 1.03);
		setFullParentWidthLine(hr_g3, 0, 1);
		label_g1.layoutXProperty().bind(hr_g1.startXProperty());
		label_g1.layoutYProperty().bind(hr_g1.startYProperty().subtract(25));
		label_g2.layoutXProperty().bind(hr_g2.startXProperty());
		label_g2.layoutYProperty().bind(hr_g2.startYProperty().subtract(25));
		label_g3.layoutXProperty().bind(hr_g3.startXProperty());
		label_g3.layoutYProperty().bind(hr_g3.startYProperty().subtract(25));
		btn_g3.layoutXProperty().bind(hr_g3.endXProperty().subtract(btn_g3.widthProperty()));
		btn_g3.layoutYProperty().bind(hr_g3.endYProperty().subtract(27.5));

		// Binding liste a ListView
		listaOmbrelloni.setItems(ombrelloni);
		listaServizi.setItems(servizi);

		// Setup tabella ricevuta
		tableSetup();

		// Campi non editable
		dataInizio.setEditable(false);
		dataFine.setEditable(false);
		numeroLettini.setEditable(false);
		listaOmbrelloni.setEditable(false);
		listaServizi.setEditable(false);
	}

	/**
	 * Richiama la ricerca di una prenotazione dato l'ID della prenotazione.
	 */
	@FXML
	private void handleRicercaPerIdPren(ActionEvent event) {
		// Pulisco i campi
		clearFields();

		// Mostro dialog
		Dialog_SelPrenByID dialogID = new Dialog_SelPrenByID(controller);
		dialogID.showDialog();
	}

	/**
	 * Richiama la ricerca di una prenotazione dato nome e cognome del cliente.
	 */
	@FXML
	private void handleRicercaPerCliente(ActionEvent event) {
		// Pulisco i campi
		clearFields();

		// Mostro dialog
		dialogNome = new Dialog_SelPrenByNome(controller);
		dialogNome.showDialog();
	}
	
	/**
	 * Richiama la finestra di selezione di una convenzione.
	 */
	@FXML
	private void handleSelezionaConvenzione(ActionEvent event) {
		// Mostro dialog
		dialogConvenzione = new Dialog_SelConvenzione(controller);
		dialogConvenzione.showDialog();
	}

	/**
	 * Richiesta di terminazione della prenotazione al controller.
	 */
	@FXML
	private void handleTerminaPrenotazione(ActionEvent event) {
		controller.terminaPrenotazione();
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
	 * Associa le proprietà startX e endX ai valori degli estremi x del contenitore
	 * padre.
	 * 
	 * @param l                Linea da dover associare
	 * @param multiplierStartX Moltiplicatore per l'inizio della x
	 * @param multiplierEndX   Moltiplicatore per la fine della x
	 */
	private void setFullParentWidthLine(Line l, double multiplierStartX, double multiplierEndX) {
		Pane parent = (Pane) l.getParent();
		l.startXProperty().bind(parent.widthProperty().multiply(multiplierStartX));
		l.endXProperty().bind(parent.widthProperty().multiply(multiplierEndX));
		// Evito aliasing
		l.setStartY(0.5);
		l.setEndY(0.5);
	}

	/**
	 * Setup iniziale della tabella ricevuta.
	 */
	@SuppressWarnings("unchecked")
	private void tableSetup() {
		// Binding a lista elementi
		tableRicevuta.setItems(ricevuta);

		// Creazione pseudoclasse ulima riga
		PseudoClass lastRow = PseudoClass.getPseudoClass("last-row");

		tableRicevuta.setRowFactory(tv -> new TableRow<String[]>() {
			@Override
			public void updateIndex(int index) {
				super.updateIndex(index);
				pseudoClassStateChanged(lastRow, index >= 0 && index == tableRicevuta.getItems().size() - 1);
			}
		});

		// Colonne dimensione max come tabella
		tableRicevuta.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Creazione colonne
		TableColumn<String[], String> colDescrizione = new TableColumn<String[], String>("Descrizione");
		TableColumn<String[], String> colGiorni = new TableColumn<String[], String>("Giorni");
		TableColumn<String[], String> colEuro = new TableColumn<String[], String>("Euro");

		// Valore delle celle
		colDescrizione.setCellValueFactory(s -> new ReadOnlyStringWrapper(s.getValue()[0]));
		colGiorni.setCellValueFactory(s -> new ReadOnlyStringWrapper(s.getValue()[1]));
		colEuro.setCellValueFactory(s -> new ReadOnlyStringWrapper(s.getValue()[2]));

		// Non sortable
		colDescrizione.setSortable(false);
		colGiorni.setSortable(false);
		colEuro.setSortable(false);

		// Non reordable
		colDescrizione.setReorderable(false);
		colGiorni.setReorderable(false);
		colEuro.setReorderable(false);

		// Larghezza colonne
		colDescrizione.minWidthProperty().bind(tableRicevuta.widthProperty().multiply(0.50));
		colGiorni.minWidthProperty().bind(tableRicevuta.widthProperty().multiply(0.15));
		colEuro.minWidthProperty().bind(tableRicevuta.widthProperty().multiply(0.20));

		// Stili tabella
		colGiorni.getStyleClass().add("columnRight");
		colEuro.getStyleClass().add("columnRight");

		// Placeholder tabella vuota
		Label placeholder = new Label("Selezionare una prenotazione");
		tableRicevuta.setPlaceholder(placeholder);

		// Aggiunta colonne a tabella
		tableRicevuta.getColumns().setAll(colDescrizione, colGiorni, colEuro);
	}

	/**
	 * Mostra un popup di errore.
	 * 
	 * @param titolo      Titolo della finestra popup.
	 * @param descrizione Descrizione dell'errore.
	 */
	public void showError(String titolo, String descrizione) {
		AlertHelper.showAlert(AlertType.ERROR, dataInizio.getScene().getWindow(), titolo, descrizione);
	}

	/**
	 * Mostra un popup di successo della terminazione.
	 */
	@Override
	public void confermaTerminazione() {
		AlertHelper.showAlert(AlertType.INFORMATION, dataInizio.getScene().getWindow(), "Terminazione completata",
				"Terminazione prenotazione eseguita con successo.");
		clearFields();
	}

	/**
	 * Svuota tutti i campi.
	 */
	private void clearFields() {
		dataInizio.clear();
		dataFine.clear();
		numeroLettini.clear();
		ombrelloni.clear();
		servizi.clear();
		ricevuta.clear();
	}

	/*
	 * SETTERS
	 */
	@Override
	public void setDataInizio(String dataInizio) {
		this.dataInizio.setText(dataInizio);
	}

	@Override
	public void setDataFine(String dataFine) {
		this.dataFine.setText(dataFine);
	}

	@Override
	public void setNumeroLettini(int numeroLettini) {
		this.numeroLettini.setText(Integer.toString(numeroLettini));
	}

	@Override
	public void addOmbrelloneToList(String idOmbrellone) {
		this.ombrelloni.add(idOmbrellone);
	}

	@Override
	public void addServizioToList(String nomeServizio) {
		this.servizi.add(nomeServizio);

	}

	@Override
	public void aggiornaListaPrenotazioniDisponibili(List<String[]> prenotazioni) {
		dialogNome.aggiornaListaPrenotazioniDisponibili(prenotazioni);

	}

	@Override
	public void aggiornaTabellaRicevuta(List<String[]> ricevuta) {
		this.ricevuta.setAll(ricevuta);
	}

	@Override
	public void aggiornaListaConvenzioni(List<String[]> convenzioni) {
		//Inoltra risultati richiesta
		dialogConvenzione.aggiornaListaConvenzioni(convenzioni);
	}
}
