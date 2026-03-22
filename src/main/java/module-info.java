/**
 * Made by Jason Tan.
 * Using JavaFX, ApachePoi and Je's Libs.
 * Modular gradle application.
 */
module LibraryCatalogFX {
	// JavaFX
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires JavaFXContext;

	// Xlxs
	requires org.apache.poi.poi;
	requires org.apache.poi.ooxml;

	// Other
	requires Lib;
	
	opens com.libcat.controller to javafx.fxml;
	
	exports com.libcat.controller;
	exports com.libcat.main;
}