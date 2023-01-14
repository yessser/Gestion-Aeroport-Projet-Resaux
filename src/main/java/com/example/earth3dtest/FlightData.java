package com.example.earth3dtest;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class FlightData {
    private SimpleStringProperty FlightId;
    private SimpleStringProperty StartStation;
    private SimpleStringProperty DestinationStation;
    public FlightData(String PlaneId,String StartStation, String DestinationStation){
        this.FlightId = new SimpleStringProperty(PlaneId);
        this.StartStation = new SimpleStringProperty(StartStation);
        this.DestinationStation = new SimpleStringProperty(DestinationStation);
    }

    public String getFlightId() {
        return FlightId.get();
    }

    public SimpleStringProperty flightIdProperty() {
        return FlightId;
    }

    public String getStartStation() {
        return StartStation.get();
    }

    public SimpleStringProperty startStationProperty() {
        return StartStation;
    }

    public String getDestinationStation() {
        return DestinationStation.get();
    }

    public SimpleStringProperty destinationStationProperty() {
        return DestinationStation;
    }
}
