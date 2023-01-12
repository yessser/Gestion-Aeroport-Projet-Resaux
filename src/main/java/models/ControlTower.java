package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ControlTower {
    private HashMap<UUID,Plane> allPlanes;
    private HashMap<UUID,Station> allStations;
    private HashMap<UUID,Flight> allFlights;

    public void addStation(Station station){
        allStations.put(station.getIdStation(),station);
    }
    public void addPlane (Plane plane){
        allPlanes.put(plane.getIdPlane(),plane);
    }
}
