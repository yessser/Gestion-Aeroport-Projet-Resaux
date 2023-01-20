package com.example.earth3dtest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FormFlightController implements Initializable {

    ControlTower controlTower;
    private Server server;

    public  void setControlTower(ControlTower c){
        this.controlTower=c;
        StationChoice.setItems(FXCollections.observableArrayList(new ArrayList(controlTower.getAllStations().values())));
    }
    ArrayList<Station> visitedStation=new ArrayList<>();

    @FXML
    private VBox path;
    @FXML
    private ChoiceBox<Station> StationChoice;

    @FXML
    private ChoiceBox<Plane> airplaneChoice;

    @FXML
    void addStation(ActionEvent event) {
        if(StationChoice.getValue().getNameStation()!=null
                && !visitedStation.contains(StationChoice.getValue())){
            visitedStation.add(StationChoice.getValue());
            Text t= new Text(StationChoice.getValue().getNameStation());
            t.getStyleClass().add("textPath");
            t.setWrappingWidth(350);
            path.getChildren().add(t);
        }
        if(visitedStation.size()==1) {
            airplaneChoice.setItems(FXCollections.observableArrayList(new ArrayList(visitedStation.get(0).getStationPlanes().values())));
        }
        System.out.println(visitedStation);
    }
    @FXML
    void addFLight(ActionEvent event) {
        if(airplaneChoice.getValue()!=null&&visitedStation.size()>=2) {
            try {
                controlTower.addFlight(new Flight(airplaneChoice.getValue(),visitedStation));
                server.startFlight(airplaneChoice.getValue().getIdPlane());
            } catch (Exception e) {
                System.out.println("plane not in starting station SHOULDNT HAPPEN");
                throw new RuntimeException(e);
            }
            Stage stage=(Stage) path.getScene().getWindow();
            stage.close();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        airplaneChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(Plane o) {
                return o != null ? o.getIdPlane().toString() : "null";
            }

            @Override
            public Plane fromString(String s) {

                return null;
            }
        });
        StationChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(Station o) {
                return o != null ? o.getNameStation() : "null";
            }

            @Override
            public Station fromString(String s) {

                return null;
            }
        });


    }

    public void setServer(Server server) {
        this.server = server;
    }
}
