package com.libcat.controller;

import com.fxcontext.main.Context;
import com.fxcontext.message.Message;

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
		String keyword = input.getText();
		Message message = Message.newBuilder()
				.setAction("change_page")
				.putExtra("keyword", keyword)
				.putExtra("page", "results")
				.build();
		context.broadcastMessage(message);
	}
}
