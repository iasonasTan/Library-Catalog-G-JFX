/**
 * 
 */
/**
 * 
 */
module LibraryCatalogFX {
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	
	opens com.libcat.controller to javafx.fxml;
	
	exports com.libcat.controller;
	exports com.libcat.main;
}