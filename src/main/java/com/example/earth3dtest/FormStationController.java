package com.example.earth3dtest;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.ControlTower;
import models.Position;
import models.Station;

import java.net.URL;
import java.util.ResourceBundle;

public class FormStationController implements Initializable {
    ControlTower controlTower;
    ModelScene modelScene;
    public  void setControlTower(ControlTower c){
        this.controlTower=c;
    }

    @FXML
    private TextField capacity;

    @FXML
    private TextField fuel;

    @FXML
    private TextField lat;

    @FXML
    private TextField lon;

    @FXML
    private TextField name;

    @FXML
    void addStation(ActionEvent event) {
        Station s= new Station(name.getText(),new Position(Double.valueOf(lat.getText()),Double.valueOf(lon.getText())),
                Double.valueOf(fuel.getText()),Integer.parseInt(capacity.getText()));
            controlTower.addStation(s);
            modelScene.spawnStation(s);
        Stage stage=(Stage) name.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fuel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    fuel.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        lat.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    fuel.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        lon.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    fuel.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        capacity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    fuel.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
}
