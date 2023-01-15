package com.example.earth3dtest;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloController {

    @FXML
    private SubScene earthScene;
    private ModelScene modelScene;

    @FXML
    void initialize(){
//        server start
        InitSubScene();
    }


    @FXML
    protected void onHelloButtonClick() {

        modelScene.startAnimation();
    }
    @FXML
    protected void onAddStationButton(){
//        spawnPlane
//        server.startflight(F)
        modelScene.setNewStationActive(true);
//        modelScene.move(10D,0D);
    }


    @FXML
    protected void onAddButton() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("formStation.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Add");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }


    void InitSubScene(){
        modelScene= new ModelScene(earthScene);
    }



}