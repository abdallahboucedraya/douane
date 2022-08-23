package com.wintex.douane;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class DouaneMain extends Application {

	Parent root;

	@Override
	public void start(Stage primaryStage) {
		try {
			root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Home.fxml")));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			Image image = new Image("/icon.png");
			primaryStage.getIcons().add(image);
			primaryStage.setTitle("Direction générale des douanes");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
