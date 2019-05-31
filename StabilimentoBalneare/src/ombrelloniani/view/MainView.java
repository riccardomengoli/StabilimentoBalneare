package ombrelloniani.view;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainView extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		VistaNavigator.setStage(stage);

		stage.getIcons().add(new Image(getClass().getResource("images/logoTransparent.png").toExternalForm()));
		stage.setTitle("Stabilimento Balneare");
		stage.setMinWidth(1024);
		stage.setMinHeight(600);
		stage.setWidth(1024);
		stage.setHeight(650);
		stage.centerOnScreen();

		// XXX TEMPORARY FOR DEBUG
		VistaNavigator.loadView(VistaNavigator.LOGIN);
	}

	public static void main(String[] args) {
		launch(args);
	}

}