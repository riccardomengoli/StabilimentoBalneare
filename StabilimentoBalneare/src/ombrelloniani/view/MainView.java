package ombrelloniani.view;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class MainView extends Application {

    @Override
    public void start(Stage stage) throws Exception {
    	VistaNavigator.setStage(stage);
    	VistaNavigator.loadView(VistaNavigator.LOGIN);

        stage.getIcons().add(new Image(getClass().getResource("images/logoTransparent.png").toExternalForm())); 
        stage.setTitle("Stabilimento Balneare");
        stage.setMinWidth(1024);
        stage.setMinHeight(600);
    }

    public static void main(String[] args) {
        launch(args);
    }

}