package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Station implements Serializable {


    private UUID idStation;
    private String nameStation;
    private Position position;
    private Double maxFuel;
    private Double currentFuel;
    private HashMap<UUID,Plane> stationPlanes;
    private int maxNumberOfPlanes;

    public void addPlane(Plane plane){
        stationPlanes.put(plane.getIdPlane(),plane);
    }
    public void removePlane(UUID planeUUID){
        stationPlanes.remove(planeUUID);
    }
    public Station(String nameStation, Position position, Double maxFuel,int maxNumberOfPlanes) {
        this.idStation = UUID.randomUUID();
        this.nameStation = nameStation;
        this.position = position;
        this.maxFuel = maxFuel;
        this.maxNumberOfPlanes = maxNumberOfPlanes;
        stationPlanes= new HashMap<>();
    }
    public Position getPosition() {
        return position;
    }
    public HashMap<UUID, Plane> getStationPlanes() {
        return stationPlanes;
    }

    public UUID getIdStation() {
        return idStation;
    }

    public void setNameStation(String nameStation) {
        this.nameStation = nameStation;
    }

    public String getNameStation() {
        return nameStation;
    }

    public int getMaxNumberOfPlanes() {
        return maxNumberOfPlanes;
    }
}
