package ombrelloniani.view.fxmlControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ombrelloniani.view.VistaNavigator;

public class HomeOperatoreEventsController {
	@FXML private Button logoutButton;
	@FXML private Button gestionePren;
	@FXML private Button controlloDisp;

	public HomeOperatoreEventsController() {
		
	}
	
	/*
	 * Evento al click di logout.
	 * Ritorno alla view di login.
	 */
	@FXML
	private void handleLogoutButtonClick (ActionEvent event) throws Exception {
		
		VistaNavigator.loadView(VistaNavigator.LOGIN);
		
		/*
		Stage owner = (Stage) logoutButton.getScene().getWindow();
		
		Parent root = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("css/styles.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("css/login.css").toExternalForm());
		owner.setScene(scene);
		
		System.out.println("Cambio scena effettuato correttamente"); //XXX TEMP
		*/
	}
	
	@FXML
	private void handleGestionePrenButtonClick (ActionEvent event) throws Exception {
		
		Stage owner = (Stage) gestionePren.getScene().getWindow();
		
		/*
		Parent root = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("css/styles.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("css/login.css").toExternalForm());
		owner.setScene(scene);
		
		System.out.println("Cambio scena effettuato correttamente"); //XXX TEMP
		*/
	}
	
	@FXML
	private void handleControlloDispButtonClick (ActionEvent event) throws Exception {
		
		Stage owner = (Stage) controlloDisp.getScene().getWindow();
		
		/*
		Parent root = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("css/styles.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("css/login.css").toExternalForm());
		owner.setScene(scene);
		
		System.out.println("Cambio scena effettuato correttamente"); //XXX TEMP
		*/
	}
	
	/*
	 * Evento all'entrata del mouse sopra il bottone di login.
	 * Imposta il colore.
	 */
	@FXML
	private void handleHoverInButton (MouseEvent event) {
		((Button) event.getSource()).setStyle("-fx-background-color: #4449d9;");
	}
	
	/*
	 * Evento all'uscita del mouse dal bottone di login.
	 * Resetta il colore principale.
	 */
	@FXML
	private void handleHoverOutButton (MouseEvent event) {
		((Button) event.getSource()).setStyle("");
	}
}
