package ombrelloniani.view.fxmlControllers;

import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Callback;
import ombrelloniani.controller.interfaces.IControllerTermina;
import ombrelloniani.view.VistaNavigator;
import ombrelloniani.view.interfaces.IViewTermina;

public class TerminaPrenotazione extends FXMLController implements IViewTermina {
	@FXML private DatePicker dataInizio;
	@FXML private DatePicker dataFine;
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

	public TerminaPrenotazione() {
		// this.controller = new TerminaController(this);
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

		listaOmbrelloni.setItems(ombrelloni);
		listaServizi.setItems(servizi);
		
		tableSetup();
	}

	/**
	 * Richiama la ricerca di una prenotazione dato l'ID della prenotazione.
	 */
	@FXML
	private void handleRicercaPerIdPren(ActionEvent event) {
		// controller.cercaPrenotazione();
	}

	/**
	 * Richiama la ricerca di una prenotazione dato nome e cognome del cliente.
	 */
	@FXML
	private void handleRicercaPerCliente(ActionEvent event) {
		// controller.cercaPrenotazioni();
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
	 * Associa le propriet� startX e endX ai valori degli estremi x del contenitore
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
	
	@SuppressWarnings("unchecked")
	private void tableSetup() {
		tableRicevuta.setItems(ricevuta);
		TableColumn<String[], String> colDescrizione = new TableColumn<String[], String>("Descrizione");
		TableColumn<String[], String> colGiorni = new TableColumn<String[], String>("Giorni");
		TableColumn<String[], String> colEuro = new TableColumn<String[], String>("Euro");
		colDescrizione.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<String[], String> entry) {
		         return new ReadOnlyStringWrapper(entry.getValue()[0]);
		     }
		});
		colGiorni.setCellValueFactory(s -> new ReadOnlyStringWrapper(s.getValue()[1])); //XXX TEST LAMBDA
		colEuro.setCellValueFactory(s -> new ReadOnlyStringWrapper(s.getValue()[2]));
		
		//TODO larghezza colonne
		//TODO style table
		
		tableRicevuta.getColumns().setAll(colDescrizione, colGiorni, colEuro);
	}

	/*
	 * SETTERS
	 */
	@Override
	public void setDataInizio(LocalDate dataInizio) {
		this.dataInizio.setValue(dataInizio);
	}

	@Override
	public void setDataFine(LocalDate dataFine) {
		this.dataFine.setValue(dataFine);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void aggiornaTabellaRicevuta(List<String[]> ricevuta) {
		for (String[] entryRicevuta : ricevuta) {
			this.ricevuta.add(entryRicevuta);
		}
	}

	@Override
	public void aggiornaListaConvenzioni(List<String[]> convenzioni) {
		// TODO Auto-generated method stub

	}
}