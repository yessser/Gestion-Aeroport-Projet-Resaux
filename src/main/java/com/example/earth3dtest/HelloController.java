package com.example.earth3dtest;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import java.net.URL;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private SubScene earthScene;
    private ModelScene modelScene;

    @FXML
    void initialize(){
        InitSubScene();
    }


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
        modelScene.startAnimation();
    }
    @FXML
    protected void onAddStationButton(){
        modelScene.setNewStationActive(true);

    }


    void InitSubScene(){
        modelScene= new ModelScene(earthScene);
    }



}