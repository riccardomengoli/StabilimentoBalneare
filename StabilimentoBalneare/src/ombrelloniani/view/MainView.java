package ombrelloniani.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class MainView extends Application {

    @Override
    public void start(Stage stage) throws Exception {
    	Parent root = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("css/styles.css").toExternalForm());

        stage.getIcons().add(new Image(getClass().getResource("images/logoTransparent.png").toExternalForm())); 
        stage.setTitle("Stabilimento Balneare");
        stage.setMinWidth(1024);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}