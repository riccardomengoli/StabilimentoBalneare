package ombrelloniani.view.fxmlControllers;

import javafx.scene.Scene;

public abstract class FXMLController {
	private Scene callingScene = null;
	
	protected Scene getCallingScene() {
		return this.callingScene;
	}
	
	public void setCallingScene(Scene s) {
		this.callingScene = s;
	}
}
