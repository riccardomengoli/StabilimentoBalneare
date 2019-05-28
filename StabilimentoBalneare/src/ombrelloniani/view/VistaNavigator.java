package ombrelloniani.view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ombrelloniani.view.fxmlControllers.FXMLController;

/**
 * Utility class for controlling navigation between vistas.
 *
 * All methods on the navigator are static to facilitate simple access from
 * anywhere in the application.
 */
public class VistaNavigator {

	private VistaNavigator() {};

	/**
	 * Convenience constants for fxml layouts managed by the navigator.
	 */
	public static final String LOGIN = "fxml/Login.fxml";
	public static final String HOMEOPERATORE = "fxml/HomeOperatore.fxml";
	public static final String HOMEGESTORE = "fxml/HomeGestore.fxml";
	public static final String GESTIONEPRENOTAZIONE = "fxml/GestionePrenotazione.fxml";

	private static Stage stage;

	public static void setStage(Stage stage) {
		VistaNavigator.stage = stage;
	}

	/**
	 * Loads the vista specified by the fxml file into the vistaHolder pane of the
	 * main application layout.
	 *
	 * Previously loaded vista for the same fxml file are not cached. The fxml is
	 * loaded anew and a new vista node hierarchy generated every time this method
	 * is invoked.
	 * 
	 * However the calling scene gets injected into the new scene controller, in
	 * order to go back to previous scenes.
	 *
	 * A more sophisticated load function could potentially add some enhancements or
	 * optimizations, for example: cache FXMLLoaders cache loaded vista nodes, so
	 * they can be recalled or reused allow a user to specify vista node reuse or
	 * new creation allow back and forward history like a browser
	 *
	 * @param fxml the fxml file to be loaded.
	 */
	public static void loadView(String fxml) {
		try {
			Scene currentScene = stage.getScene();

			FXMLLoader loader = new FXMLLoader(VistaNavigator.class.getResource(fxml));
			Parent root = loader.load();
			FXMLController controller = loader.<FXMLController>getController();
			controller.setCallingScene(currentScene);
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(VistaNavigator.class.getResource("css/styles.css").toExternalForm());

			switch (fxml) {
			case LOGIN:
				scene.getStylesheets().add(VistaNavigator.class.getResource("css/login.css").toExternalForm());
				break;
			case HOMEOPERATORE:
				scene.getStylesheets().add(VistaNavigator.class.getResource("css/home.css").toExternalForm());
				break;
			case HOMEGESTORE:
				scene.getStylesheets().add(VistaNavigator.class.getResource("css/home.css").toExternalForm());
				break;
			case GESTIONEPRENOTAZIONE:
				scene.getStylesheets().add(VistaNavigator.class.getResource("css/home.css").toExternalForm());
				break;
			default:
				break;
			}

			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadView(Scene scene) {
		stage.setScene(scene);
		stage.show();
	}
}
