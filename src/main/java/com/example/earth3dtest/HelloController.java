package com.example.earth3dtest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import models.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloController implements Initializable{
    Server server;

    {
        try {
            server = new Server();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public TableView<PlaneData> tableView;
    public TableView<FlightData> tableFlights;
    public TableView<com.example.earth3dtest.StationDat> tableStations;
    public TableColumn<PlaneData,String> PlaneId;
    public TableColumn<PlaneData,Double> Lat;
    public TableColumn<PlaneData,Double> Lon;
    public ToggleButton toggleFlight;
    public ToggleButton toggleStation;
    public ToggleButton togglePlane;
    public Button addElement;
    public ListView<Station> contentList;

    ObservableList<PlaneData> planeData = FXCollections.observableArrayList();
    public TableColumn<StationDat,String> StationName;
    public TableColumn<StationDat,Double> StationLat;
    public TableColumn<StationDat,Double> StationLon;
    ObservableList<StationDat> StationDat = FXCollections.observableArrayList();

    @FXML
    public TableColumn<FlightData,String> FlightId;
    public TableColumn<FlightData,String> StartStation;
    public TableColumn<FlightData,String> DestinationStation;
    ObservableList<FlightData> FlightData = FXCollections.observableArrayList();

    @FXML
    private SubScene earthScene;
    private ModelScene modelScene;
    private boolean started = false;
    public ArrayList<Plane> planes = new ArrayList<>();
    public ArrayList<Station> Stations = new ArrayList<>();
    public ArrayList<Flight> flights = new ArrayList<>();
    private ArrayList<Station> stations;
    Plane p1 = new Plane(1000D,100D/6372.8,Math.toRadians(30D),20D,400D,new Position(90D,180D));
    Plane p2 = new Plane(1000D,100D/6372.8,Math.toRadians(30D),20D,400D,new Position(10D,10D));
    Station station1 = new Station("France",new Position(90D,180D),18000D,5);
    private void addStation(Station s){
        server.controlTower.addStation(s);
        modelScene.spawnStation(s);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitSubScene();


        // Pre added Stations
        Station alg = new Station("Algiers",new Position(36.75D,3.05D),100D,10);
        server.controlTower.addStation(alg);
        modelScene.spawnStation(alg);
        Station fra = new Station("France",new Position(48.85D,2.35D),100D,10);
        server.controlTower.addStation(fra);
        modelScene.spawnStation(fra);
        Station uae = new Station("Dubai",new Position( 25.033101999D, 55.37168350D),100D,10);
        server.controlTower.addStation(uae);
        modelScene.spawnStation(uae);
        Station russ = new Station("Moscow",new Position( 55.8869D, 37.6738D),100D,10);
        server.controlTower.addStation(russ);
        modelScene.spawnStation(russ);
        Station egp = new Station("Cairo",new Position( 30.1672D, 31.3676D),100D,10);
        server.controlTower.addStation(egp);
        modelScene.spawnStation(egp);



        contentList.setCellFactory(s->new StationComponentController());
        try {
            Naming.rebind("rmi://localhost:1099/MyServer", server);
            System.out.println("server ready");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        SelectedOptionStations();

    }

    @FXML
    protected void onHelloButtonClick() {
      if(!started){
        modelScene.startAnimation();
        started = true;
      }
      else {
          modelScene.stopAnimation();
          started = false;
      }
    }
    @FXML
    //when add station button is clicked
    protected void onAddStationButton(){
      /*  modelScene.setNewStationActive(true);
        modelScene.move(10D,0D);*/
        int i=0;
        Station station = new Station("France",new Position(90D,180D),18000D,5);
        while (i<stations.size()){
            if (station1.getPosition()==stations.get(i).getPosition() || station1.getNameStation().toUpperCase() == stations.get(1).getNameStation().toUpperCase()){
                break;
            }
        }
        if (i==stations.size()) {
            stations.add(station);
            for(Station st:stations){
                modelScene.spawnStation(st);
            }
        }

    }



    public void SelectedOption(){
        addElement.setCancelButton(true);
        addElement.setVisible(true);

//        tableView.setVisible(false);
//        tableFlights.setVisible(true);
//        tableStations.setVisible(false);

       // modelScene.startPlanes();

    }

    public void SelectedOptionStations(){
        addElement.setCancelButton(true);
        addElement.setVisible(true);
        for (Station s:server.controlTower.getAllStations().values()) {
            System.out.println(s.getNameStation());
        }

        contentList.setItems(FXCollections.observableArrayList(server.controlTower.getAllStations().values()));
        contentList.setVisible(true);
//        tableView.setVisible(false);
//        tableFlights.setVisible(false);
//        tableStations.setVisible(true);

    }
    public void SelectedOptionPlanes(){
        addElement.setCancelButton(false);
        addElement.setVisible(false);
//        tableView.setVisible(true);
//        tableFlights.setVisible(false);
//        tableStations.setVisible(false);
    }


    @FXML
    protected void onAddButton() throws IOException {
        Parent p=null;
        FXMLLoader fxmlLoader=null;
        if(toggleStation.isSelected()){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("formStation.fxml"));
            p=fxmlLoader.load();
            FormStationController c = fxmlLoader.getController();
            c.setControlTower(server.controlTower);
            c.modelScene=modelScene;

        }
        if(toggleFlight.isSelected()){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("FormFlights.fxml"));
            p=fxmlLoader.load();
            FormFlightController c = fxmlLoader.getController();
            c.setModelScene(modelScene);
            c.setServer(server);
            c.setControlTower(server.controlTower);
        }
        if (togglePlane.isSelected()){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("FormPlane.fxml"));
            p=fxmlLoader.load();

            FormPlaneController c = fxmlLoader.getController();
            c.setStations(new ArrayList<Station>(server.controlTower.getAllStations().values()));
            c.setControlTower(server.controlTower);
        }

        Scene scene = new Scene(p, 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Add");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent e){
            }
        });
    }


    void InitSubScene(){
        modelScene= new ModelScene(earthScene);
        server.setModelScene(modelScene);
    }



}