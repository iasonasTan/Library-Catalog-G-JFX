package com.libcat.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.libcat.books.manager.BookManager;
import com.libcat.storage.Storage;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.fxcontext.main.Context;
import com.fxcontext.message.Message;
import com.fxcontext.receiver.MessageReceiver;

public class Main extends Application implements Context, MessageReceiver {
	private final List<MessageReceiver> mReceivers = new ArrayList<>();
	private Stage mStage;

	public static void main(String[] args) {
		Storage.init(BookManager.instance);
		Storage.getInstance().loadBooksFromXlxs(args[0]);
		launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {
		mStage = stage;
		registerReceiver(this);

		Message message = Message.newBuilder()
				.setAction("change_page")
				.putExtra("page", "home")
				.build();
		broadcastMessage(message);
		
		stage.setHeight(500);
		stage.setWidth(500);
		stage.setTitle("Library Catalog Application");
		stage.show();
	}

	@Override
	public void onReceive(Message message) {
		if(message.getAction().equals("change_page")) {
			Parent scene = Context.loadFXML(
				this,
				getClass().getResource("/layout/"+message.getBundle().getString("page")+".fxml"),
				getClass().getResource("/style/style.css")
			);
			mStage.setScene(new Scene(scene, 500, 400));

			if(message.getBundle().getString("page").equals("results")) {
				Message message1 = Message.newBuilder()
					.setAction("search-books")
					.putExtra("keyword", message.getBundle().getString("keyword"))
					.build();
				broadcastMessage(message1);
			}
		}
	}

	public void broadcastMessage(Message msg) {
		for (int i = 0; i < mReceivers.size(); i++) {
			mReceivers.get(i).onReceive(msg);
		}
	}

	public void registerReceiver(MessageReceiver receiver) {
		mReceivers.add(receiver);
	}
}
