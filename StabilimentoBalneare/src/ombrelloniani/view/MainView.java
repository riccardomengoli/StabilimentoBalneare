package ombrelloniani.view;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ombrelloniani.controller.GestionePrenotazioneController;

public class MainView extends Application {

	@Override
	public void start(Stage stage) {
		VistaNavigator.setStage(stage);

		stage.getIcons().add(new Image(getClass().getResource("images/logoTransparent.png").toExternalForm()));
		stage.setTitle("Stabilimento Balneare");
		stage.setMinWidth(1024);
		stage.setMinHeight(600);
		stage.setWidth(1024);
		stage.setHeight(700);
		stage.centerOnScreen();

		//Cambiare qui per modificare starting view
		VistaNavigator.loadView(VistaNavigator.LOGIN);
		
		//Inizializzazione liste all'avvio
		loadLists();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void loadLists() {
		GestionePrenotazioneController mainController = new GestionePrenotazioneController();
		mainController.aggiornaListaConvenzioni();
		mainController.aggiornaListaFedelta();
		mainController.aggiornaListaPrezzi();
		mainController.aggiornaListaStagioni();
	}
}