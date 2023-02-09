package com.example.earth3dtest;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class popUpController {

    @FXML
    private TextField IPadr;

    public void sendIP() {

        Parent p=null;

        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("FormPlane.fxml"));
        try {
            p=fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FormPlaneController c = fxmlLoader.getController();
        c.Ipadr = IPadr.getText();

        Scene scene = new Scene(p, 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Add");
        stage.setScene(scene);
        stage.show();


    }

}
