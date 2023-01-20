package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ControlTower implements Serializable {
    private HashMap<UUID,Plane> allPlanes = new HashMap<>();
    private HashMap<UUID,Station> allStations = new HashMap<>();
    private HashMap<UUID,Flight> allFlights = new HashMap<>();

    public HashMap<UUID, Plane> getAllPlanes() {
        return allPlanes;
    }

    public HashMap<UUID, Station> getAllStations() {
        return allStations;
    }

    public HashMap<UUID, Flight> getAllFlights() {
        return allFlights;
    }
    public void addFlight(Flight flight){allFlights.put(flight.getIdFlight(),flight);}
    public void addStation(Station station){
        allStations.put(station.getIdStation(),station);
    }
    public void addPlane (Plane plane){
        allPlanes.put(plane.getIdPlane(),plane);
    }
    public void checkForCollisions(){
        ArrayList<Plane> activePlanes = new ArrayList<>();
//        todo:reminder to maybe check if the plane is actif or not
        for (Flight f:allFlights.values()){
                activePlanes.add(f.plane);
        }
        for (Plane p1:activePlanes){
            ArrayList<Plane> dangerPlanes= new ArrayList<>();
            for (Plane p2 :activePlanes) {
//                if(p1.getPosition().distance(p2.getPosition()) < p1.getSizePlane()+ p2.getSizePlane())
                if(p1.getPosition().distance(p2.getPosition()) < p1.getDangerZoneSize()+ p2.getDangerZoneSize())
                {
                    dangerPlanes.add(p2);
                }
            }
            p1.setDangerZonePlanes(dangerPlanes);
        }
    }


    public Flight searchFlightByPlaneID(UUID id) {

        for (int i = 1; i < allFlights.size(); i++) {

            if (allFlights.get(i).plane.getIdPlane().compareTo(id) == 0) {
                return allFlights.get(i);
            }
        }

        return null;
    }
}
