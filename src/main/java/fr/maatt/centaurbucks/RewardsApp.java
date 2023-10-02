package fr.maatt.centaurbucks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RewardsApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RewardsApp.class.getResource("default.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 350);
        stage.setTitle("CentaurBucks – Rewards Member Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}