package com.libcat.controller;

import com.libcat.books.manager.BookManager;
import com.libcat.main.Context;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ResultController extends VBox {
	@FXML
	private Label title;
	
	@FXML
	private VBox results;
	
	private Context context;
	
	public void showHome() {
		context.send("man", "home");
	}

	public ResultController(Context context) {
		this.context = context;
		context.register("result", objects -> {
			final String str = (String)objects[0];
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
		});
	}
}
