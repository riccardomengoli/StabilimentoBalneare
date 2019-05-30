package ombrelloniani.view.fxmlControllers;

import java.time.LocalDate;

import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import ombrelloniani.controller.CreaPrenotazioneController;
import ombrelloniani.controller.interfaces.IControllerCrea;
import ombrelloniani.view.VistaNavigator;

public class CreaPrenotazione extends FXMLController {
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

	public CreaPrenotazione() {
		this.controller = new CreaPrenotazioneController();
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
	}

	/**
	 * Richiama la ricerca di un cliente sul controller via bottone o invio su
	 * TextField idDocumento.
	 */
	@FXML
	private void handleRicercaUtente(ActionEvent event) {
		controller.cercaCliente(idDocumento.getText());
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
		///TODO controller.aggiungiOmbrellone( );
	}
	
	/**
	 * Richiama la rimozione di un ombrellone sul controller.
	 */
	@FXML
	private void handleRimuoviOmbrellone(ActionEvent event) {
		///TODO controller.rimuoviOmbrellone( );
	}
	
	@FXML
	private void handleCreaPrenotazione(ActionEvent event) {
		//TODO FIX INTERFACE controller.creaPrenotazione();
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
	private void handleBack(ActionEvent event) throws Exception {
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

	/*
	 * GETTERS
	 */
	public String getIdDocumento() {
		return idDocumento.getText();
	}

	public String getNome() {
		return nome.getText();
	}

	public String getCognome() {
		return cognome.getText();
	}

	public String getTelefono() {
		return telefono.getText();
	}

	public String getEmail() {
		return email.getText();
	}

	public LocalDate getDataInizio() {
		return dataInizio.getValue();
	}

	public LocalDate getDataFine() {
		return dataFine.getValue();
	}

	public String getNumeroLettini() {
		return numeroLettini.getText();
	}
	
	public String getInputOmbrellone() {
		return inputOmbrellone.getText();
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
}
