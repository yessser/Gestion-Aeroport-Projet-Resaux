package com.example.earth3dtest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Plane;
import models.Position;
import models.Station;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloController implements Initializable{

    public TableView<PlaneData> tableView;
    public TableColumn<PlaneData,String> PlaneId;
    public TableColumn<PlaneData,Double> Lat;
    public TableColumn<PlaneData,Double> Lon;
    ObservableList<PlaneData> planeData = FXCollections.observableArrayList();

    @FXML
    private SubScene earthScene;
    private ModelScene modelScene;
    private boolean started = false;
    public ArrayList<Plane> planes = new ArrayList<>();
    private ArrayList<Station> stations;
    Plane p1 = new Plane(1000D,100D/6372.8,Math.toRadians(30D),20D,400D,new Position(90D,180D));
    Plane p2 = new Plane(1000D,100D/6372.8,Math.toRadians(30D),20D,400D,new Position(10D,10D));
    Station station1 = new Station("France",new Position(90D,180D),18000D,5);
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        planes.add(p1);
        planes.add(p2);

        PlaneId.setCellValueFactory(new PropertyValueFactory<>("PlaneId"));
        Lat.setCellValueFactory(new PropertyValueFactory<>("Lat"));
        Lon.setCellValueFactory(new PropertyValueFactory<>("Lon"));
        for (Plane pl:planes){
            int i=0;
            Double positionlat=0D,positionLon=0D;
            String regex="([0-9]+[.][0-9]+)";
            String input= pl.getPosition().toString();

            Pattern pattern=Pattern.compile(regex);
            Matcher matcher=pattern.matcher(input);

            while(matcher.find()) {
                if (i==0)
                    positionlat = Double.parseDouble(matcher.group(1));
                else
                    positionLon = Double.parseDouble(matcher.group(1));
                i++;
            }
            planeData.add(new PlaneData(pl.getIdPlane().toString(),positionlat,positionLon));
        }

        tableView.setItems(planeData);
        InitSubScene();

    }

    @FXML
    void initialize(){

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
        //modelScene.spawnStation(station1);


        modelScene.startPlanes();

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