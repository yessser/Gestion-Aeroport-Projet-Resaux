package com.example.earth3dtest;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import models.Position;
import models.Station;

import java.io.IOException;

public class StationComponentController extends ListCell<Station> {

    @FXML
    private Text StationName;
    @FXML
    Text stationLat;
    @FXML
    Text stationLon;
    @FXML
    HBox HBOX;
    @FXML
    AnchorPane CellPane;


    private FXMLLoader mLLoader;
    public void setStationName(String stationName){
        this.StationName.setText(stationName);
    }

    public void setPosition(Position pos){
        this.stationLat.setText(String.valueOf(pos.positionlat));
        this.stationLon.setText(String.valueOf(pos.positionLon));
    }
    @Override
    protected void updateItem(Station s,boolean empty){
        super.updateItem(s,empty);
        if(empty ||s==null){
            setStyle("-fx-control-inner-background:  #1b1b1b;");
            setText(null);
            setGraphic(null);

        }else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("StationComponent.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                setStyle("-fx-control-inner-background:  #1b1b1b;");
                setStationName(s.getNameStation());
                setPosition(s.getPosition());
                setText(null);
                setGraphic(HBOX);
                setGraphic(CellPane);

            }


        }
    }
}
