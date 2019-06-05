package ombrelloniani.view.fxmlControllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Popup_InfoOmbrellone extends VBox {

	@FXML private Label idOmbrellone;

	public Popup_InfoOmbrellone(String[] content) {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/ombrelloniani/view/fxml/Popup_InfoOmbrellone.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		idOmbrellone.setText(content[0]);
	}
}
