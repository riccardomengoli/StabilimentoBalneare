module stabilimentoBalneare {
	requires java.sql;
	
	requires javafx.base;
	requires transitive javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;
	
	opens views to javafx.fxml;
	exports views;
}