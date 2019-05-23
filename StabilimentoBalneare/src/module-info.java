module stabilimentoBalneare {
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;
	
	opens views to javafx.fxml;
	exports views;
}