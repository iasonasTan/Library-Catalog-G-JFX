package com.libcat.main;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.libcat.books.manager.BookManager;
import com.libcat.storage.Storage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application implements Context {
	private final Map<String, Consumer<Object[]>> mReceivers = new HashMap<>();

	public static void main(String[] args) {
		Storage.init(args[0]);
        	Storage.getInstance().loadBooks(BookManager.instance);
		launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {
		Scene scene = Context.requireScene(this, "home");
		stage.setScene(scene);
		stage.setHeight(500);
		stage.setWidth(500);
		stage.setTitle("Library Catalog Application");
		stage.show();
		register("main", objs -> {
			Scene scene1 = Context.requireScene(this, "results");
			stage.setScene(scene1);
			send("result", (String)objs[0]);
		});
		register("man", _ -> {
			Scene scene2 = Context.requireScene(this, "home");
			stage.setScene(scene2);
		});
	}

	@Override
	public void send(String dest, Object... objs) {
		mReceivers.get(dest).accept(objs);
	}

	@Override
	public void register(String name, Consumer<Object[]> receiver) {
		mReceivers.put(name, receiver);
	}

}
