package com.libcat.main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public interface Context {
	void send(String dest, Object... objs);
	void register(String name, Consumer<Object[]> receiver);
	
	public static Scene requireScene(Context context, String name) {
		FXMLLoader loader = new FXMLLoader(
				Context.class.getResource("/layout/"+name+".fxml")
		);
		loader.setControllerFactory(c -> {
				try {
					Constructor<?> contextConstructor = c.getDeclaredConstructor(Context.class);
					return contextConstructor.newInstance(context);
				} catch (IllegalAccessException | InstantiationException | 
						IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
					throw new RuntimeException(e);
				}
		});
		try {
			Scene scene = new Scene(loader.load());
			scene.getStylesheets().add(
					Context.class.getResource("/style/style.css").toExternalForm()
			);
			return scene;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
