package com.libcat.controller;

import com.libcat.main.Context;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class HomeController extends VBox {
	private final Context context;
	
	@FXML
	private TextField input;

	public HomeController(Context context) {
		this.context = context;
	}
	
	@FXML
	public void showResults() {
		String s = input.getText();
		context.send("main", s);
	}

}
