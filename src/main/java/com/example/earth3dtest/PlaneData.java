package com.example.earth3dtest;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.UUID;

public class PlaneData {
    private SimpleStringProperty PlaneId;
    private SimpleDoubleProperty Lat;
    private SimpleDoubleProperty Lon;

    public PlaneData(String PlaneId,Double Lat, Double Lon){
        this.PlaneId = new SimpleStringProperty(PlaneId);
        this.Lat = new SimpleDoubleProperty(Lat);
        this.Lon = new SimpleDoubleProperty(Lon);
    }

    public String getPlaneId() {
        return PlaneId.get();
    }

    public SimpleStringProperty planeIdProperty() {
        return PlaneId;
    }

    public double getLat() {
        return Lat.get();
    }

    public SimpleDoubleProperty latProperty() {
        return Lat;
    }

    public double getLon() {
        return Lon.get();
    }

    public SimpleDoubleProperty lonProperty() {
        return Lon;
    }
}
