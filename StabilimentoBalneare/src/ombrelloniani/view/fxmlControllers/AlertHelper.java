package ombrelloniani.view.fxmlControllers;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.Window;

public class AlertHelper {

    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(AlertHelper.class.getResource("/ombrelloniani/view/css/dialog.css").toExternalForm());
        alert.show();
    }
}
