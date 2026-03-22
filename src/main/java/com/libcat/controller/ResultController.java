package com.libcat.controller;

import com.libcat.books.manager.BookManager;

import com.fxcontext.main.Context;
import com.fxcontext.message.Message;
import com.fxcontext.receiver.MessageReceiver;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ResultController extends VBox implements MessageReceiver {
	@FXML
	private Label title;
	
	@FXML
	private VBox results;
	
	private Context context;
	
	@FXML
	public void showHome() {
		Message message = Message.newBuilder()
				.setAction("change_page")
				.putExtra("page", "home")
				.build();
		context.broadcastMessage(message);
	}

	public ResultController(Context context) {
		this.context = context;
		context.registerReceiver(this);
	}

	@Override
	public void onReceive(Message message) {
		if(message.getAction().equals("search-books")) {
			final String str = message.getBundle().getString("keyword");
			Platform.runLater(() -> {
				title.setText("Search results for \""+str+"\"");
			});
			BookManager.instance.findByTitle(str, book -> {
				Platform.runLater(() -> {
					results.getChildren().add(book.toGuiNode());
				});
			});
			BookManager.instance.findByWriter(str, book -> {
				Platform.runLater(() -> {
					results.getChildren().add(book.toGuiNode());
				});
			});
			BookManager.instance.findById(str, book -> {
				Platform.runLater(() -> {
					results.getChildren().add(book.toGuiNode());
				});
			});
		}
	}
}
