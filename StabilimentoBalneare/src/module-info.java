module stabilimentoBalneare {
	requires java.sql;
	
	requires javafx.base;
	requires transitive javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;
	
	opens ombrelloniani.view.fxmlControllers to javafx.fxml;
	exports ombrelloniani.view;
}