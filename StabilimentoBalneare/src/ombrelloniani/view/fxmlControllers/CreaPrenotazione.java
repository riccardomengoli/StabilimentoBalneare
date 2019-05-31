package ombrelloniani.view.fxmlControllers;

import java.time.LocalDate;

import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import ombrelloniani.controller.CreaPrenotazioneController;
import ombrelloniani.controller.interfaces.IControllerCrea;
import ombrelloniani.view.VistaNavigator;
import ombrelloniani.view.interfaces.IViewCreazione;

public class CreaPrenotazione extends FXMLController implements IViewCreazione {
	@FXML private Button backButton;
	@FXML private Button cercaButton;
	@FXML private TextField idDocumento;
	@FXML private TextField nome;
	@FXML private TextField cognome;
	@FXML private TextField telefono;
	@FXML private TextField email;
	@FXML private DatePicker dataInizio;
	@FXML private DatePicker dataFine;
	@FXML private TextField numeroLettini;
	@FXML private TextField inputOmbrellone;
	@FXML private ListView<String> listaOmbrelloni;
	@FXML private Line hr1, hr2, hr3;
	private IControllerCrea controller;
	private ObservableList<String> ombrelloni = FXCollections.observableArrayList();

	public CreaPrenotazione() {
		this.controller = new CreaPrenotazioneController(this);
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
		setFullParentWidthLine(hr1);
		setDynamicWidthLine(hr2, hr1.startXProperty(), null);
		setDynamicWidthLine(hr3, hr1.startXProperty(), null);
		listaOmbrelloni.setItems(ombrelloni);
	}

	/**
	 * Richiama la ricerca di un cliente sul controller via bottone o invio su
	 * TextField idDocumento.
	 */
	@FXML
	private void handleRicercaUtente(ActionEvent event) {
		if (getIdDocumento() != null) {
			controller.cercaCliente();
		} else {
			showError("Campi mancanti", "Inserire un ID documento");
		}
	}

	/**
	 * Cambio di view verso Controllo Disponibilità.
	 */
	@FXML
	private void handleControlloDisp(ActionEvent event) {
		VistaNavigator.loadView(VistaNavigator.CONTROLLODISPONIBILITA);
	}

	/**
	 * Richiama l'aggiunta di un ombrellone sul controller.
	 */
	@FXML
	private void handleAggiungiOmbrellone(ActionEvent event) {
		if (getInputOmbrellone() != null) {
			controller.aggiungiOmbrellone();
			inputOmbrellone.clear();
		} else {
			showError("Campi mancanti", "Inserire un ombrellone");
		}
	}

	/**
	 * Richiama la rimozione di un ombrellone sul controller.
	 */
	@FXML
	private void handleRimuoviOmbrellone(ActionEvent event) {
		if (getInputOmbrellone() != null || getOmbrelloneSelezionato() != null) {
			controller.rimuoviOmbrellone();
			inputOmbrellone.clear();
		} else {
			showError("Campi mancanti", "Inserire o selezionare un ombrellone");
		}
	}

	/**
	 * Richiesta di creazione di una prenotazione al controller.
	 */
	@FXML
	private void handleCreaPrenotazione(ActionEvent event) {
		if (allFieldsFull()) {
			controller.creaPrenotazione();
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
	 * Associa le proprietà startX e endX ai valori degli estremi x del contenitore
	 * padre (valori leggermente personalizzati).
	 * 
	 * @param l Linea da dover associare
	 */
	private void setFullParentWidthLine(Line l) {
		Pane parent = (Pane) l.getParent();
		l.startXProperty().bind(parent.widthProperty().multiply(0.05));
		l.endXProperty().bind(parent.widthProperty().multiply(1.05));
		// Evito aliasing
		l.setStartY(0.5);
		l.setEndY(0.5);
	}

	/**
	 * Associa le proprietà di una linea ad altre passate come parametro. Se startX
	 * o endX null, impostate alla dimensione del contenitore padre (valori
	 * leggermente personalizzati).
	 * 
	 * @param l      Linea da dover associare
	 * @param startX Proprietà per il binding della coordinata X iniziale
	 * @param endX   Proprietà per il binding della coordinata X finale
	 */
	private void setDynamicWidthLine(Line l, DoubleProperty startX, DoubleProperty endX) {

		Pane parent = (Pane) l.getParent();
		if (startX != null) {
			l.startXProperty().bind(startX);
		} else {
			l.startXProperty().bind(parent.widthProperty().multiply(0.05));
		}

		if (endX != null) {
			l.endXProperty().bind(endX);
		} else {
			l.endXProperty().bind(parent.widthProperty().multiply(1.05));
		}

		// Evito aliasing
		l.setStartY(0.5);
		l.setEndY(0.5);
	}

	/**
	 * Controlla se tutti i campi sono stati riempiti.
	 */
	private boolean allFieldsFull() {
		if (getIdDocumento() == null) {
			showError("Campi mancanti", "Inserire ID documento");
			return false;
		}
		if (getNome() == null) {
			showError("Campi mancanti", "Inserire nome cliente");
			return false;
		}
		if (getCognome() == null) {
			showError("Campi mancanti", "Inserire cognome cliente");
			return false;
		}
		if (getTelefono() == null && getEmail() == null) {
			showError("Campi mancanti", "Inserire telefono o email");
			return false;
		}
		if (getDataInizio() == null) {
			showError("Campi mancanti", "Inserire data inizio prenotazione");
			return false;
		}
		if (getDataFine() == null) {
			showError("Campi mancanti", "Inserire data fine prenotazione");
			return false;
		}
		if (getNumeroLettini() < 0) {
			showError("Campi mancanti", "Inserire numero lettini");
			return false;
		}
		if (listaOmbrelloni.getItems().size() == 0) {
			showError("Campi mancanti", "Inserire almeno un ombrellone");
			return false;
		}

		return true;
	}

	public void showError(String titolo, String descrizione) {
		AlertHelper.showAlert(AlertType.ERROR, idDocumento.getScene().getWindow(), titolo, descrizione);
	}

	public void confermaCreazione(int idPrenotazioneCreata) {
		AlertHelper.showAlert(AlertType.CONFIRMATION, idDocumento.getScene().getWindow(), "Creazione completata",
				"Creazione prenotazione eseguita con successo. ID PRENOTAZIONE: " + idPrenotazioneCreata);
		idDocumento.clear();
		nome.clear();
		cognome.clear();
		telefono.clear();
		email.clear();
		dataInizio.setValue(null);
		dataFine.setValue(null);
		numeroLettini.clear();
		inputOmbrellone.clear();
		ombrelloni.clear();
	}

	/*
	 * GETTERS
	 */
	public String getIdDocumento() {
		if (idDocumento.textProperty().isEmpty().get()) {
			return null;
		}
		return idDocumento.getText();
	}

	public String getNome() {
		if (nome.textProperty().isEmpty().get()) {
			return null;
		}
		return nome.getText();
	}

	public String getCognome() {
		if (cognome.textProperty().isEmpty().get()) {
			return null;
		}
		return cognome.getText();
	}

	public String getTelefono() {
		if (telefono.textProperty().isEmpty().get()) {
			return null;
		}
		return telefono.getText();
	}

	public String getEmail() {
		if (email.textProperty().isEmpty().get()) {
			return null;
		}
		return email.getText();
	}

	public LocalDate getDataInizio() {
		if (dataInizio.valueProperty().isNull().get()) {
			return null;
		}
		return dataInizio.getValue();
	}

	public LocalDate getDataFine() {
		if (dataFine.valueProperty().isNull().get()) {
			return null;
		}
		return dataFine.getValue();
	}

	public int getNumeroLettini() {
		if (numeroLettini.textProperty().isEmpty().get()) {
			return -1;
		}

		int num = 0;
		try {
			num = Integer.parseInt(numeroLettini.getText());
		} catch (NumberFormatException e) {
			return -1;
		}

		return num;
	}

	public String getInputOmbrellone() {
		if (inputOmbrellone.textProperty().isEmpty().get()) {
			return null;
		}
		return inputOmbrellone.getText();
	}

	public String getOmbrelloneSelezionato() {
		if (listaOmbrelloni.getSelectionModel().selectedItemProperty().isNull().get()) {
			return null;
		}
		return listaOmbrelloni.getSelectionModel().getSelectedItem();
	}

	/*
	 * SETTERS
	 */
	public void setNome(String nome) {
		this.nome.setText(nome);
	}

	public void setCognome(String cognome) {
		this.cognome.setText(cognome);
	}

	public void setTelefono(String telefono) {
		this.telefono.setText(telefono);
	}

	public void setEmail(String email) {
		this.email.setText(email);
	}

	public void addOmbrelloneToList(String idOmbrellone) {
		this.ombrelloni.add(idOmbrellone);
	}

	public void removeOmbrelloneFromList(String idOmbrellone) {
		this.ombrelloni.remove(idOmbrellone);
	}
}
