package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Flight {
    UUID idFlight;
    Date departureDate;
    Date destinationArrivalDate;
    Plane plane;
    ArrayList<Station> visitedStations;

    public Station destinationStation(){
        return visitedStations.get(visitedStations.size() - 1);
    }
    public Station startStation(){
        return visitedStations.get(0);
    }

    public UUID getIdFlight() {
        return idFlight;
    }

    public Flight(Plane plane, ArrayList<Station> stations) throws Exception {

        if(!stations.get(0).getStationPlanes().containsValue(plane)){
            throw new Exception("plane isnt in the starting station");
        }
        this.plane = plane;
        plane.setState(PlaneState.Active);
        this.visitedStations=stations;
        this.departureDate=new Date();
//        TODO:compute arrival time
    }
}
