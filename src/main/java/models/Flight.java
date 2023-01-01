package models;

import java.util.ArrayList;
import java.util.Date;

public class Flight {
    int idFlight;
    Date departureDate;
    Date destinationArrivalDate;
    Plane plane;
    ArrayList<Station> visitedStations;

    public Station destinationStation(){
        return visitedStations.get(visitedStations.size() - 1);
    }
    public Station startStation(){
        return visitedStations.get(visitedStations.size() - 1);
    }
}
