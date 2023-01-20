package com.example.earth3dtest;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {
    @FXML
    private Sphere earth;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("FormPlane.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
