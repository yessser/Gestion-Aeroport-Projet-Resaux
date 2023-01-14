package com.example.earth3dtest;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class StationDat {
    private SimpleStringProperty StationName;
    private SimpleDoubleProperty StationLat;
    private SimpleDoubleProperty StationLon;

    public StationDat(String stationName, Double stationLat, Double stationLon) {
        this.StationName = new SimpleStringProperty(stationName);
        this.StationLat = new SimpleDoubleProperty(stationLat);
        this.StationLon = new SimpleDoubleProperty(stationLon);
    }

    public String getStationName() {
        return StationName.get();
    }

    public SimpleStringProperty stationNameProperty() {
        return StationName;
    }

    public double getStationLat() {
        return StationLat.get();
    }

    public SimpleDoubleProperty stationLatProperty() {
        return StationLat;
    }

    public double getStationLon() {
        return StationLon.get();
    }

    public SimpleDoubleProperty stationLonProperty() {
        return StationLon;
    }
}
