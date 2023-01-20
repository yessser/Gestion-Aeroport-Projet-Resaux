package com.example.earth3dtest;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FormPlaneController implements Initializable {
    ControlTower controlTower;
    Client client=new Client();
    @FXML
    private TextField RotationSpeed;

    @FXML
    private ChoiceBox<Station> StationsChoice;

    @FXML
    private Button addPlane;

    @FXML
    private TextField fuel;

    @FXML
    private TextField speed;


    @FXML
    void addPlane(ActionEvent event) {

        if(StationsChoice.getValue().getStationPlanes().size()<StationsChoice.getValue().getMaxNumberOfPlanes()){
            Plane p = new Plane(Double.valueOf(fuel.getText()),Double.valueOf(speed.getText()),
                    Double.valueOf(RotationSpeed.getText()),100D,300D,StationsChoice.getValue().getPosition());
            client.sendPlane(p);
            StationsChoice.getValue().addPlane(p);
            client.updateStation(StationsChoice.getValue());
            client.waitFlightCommand(p);
        }else{
            System.out.println("station is full");

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client.connect();
        System.out.println(client.getStations());
        StationsChoice.setItems(FXCollections.observableArrayList(client.getStations()));
        RotationSpeed.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    RotationSpeed.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        fuel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    fuel.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        speed.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    speed.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        StationsChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(Station o) {
                return o!=null? o.getNameStation():"null";
            }

            @Override
            public Station fromString(String s) {

                return null;
            }
        });
    }
    public void setStations(ArrayList<Station> stations){
            StationsChoice.setItems(FXCollections.observableArrayList(stations));

    }
    public  void setControlTower(ControlTower c){
        this.controlTower=c;
    }
}
