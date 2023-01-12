package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Station {


    private UUID idStation;
    private String nameStation;
    private Position position;
    private Double maxFuel;
    private Double currentFuel;
    private HashMap<String,Plane> stationPlanes;
    private int maxNumberOfPlanes;

    public void addPlane(){

    }
    public Station(String nameStation, Position position, Double maxFuel,int maxNumberOfPlanes) {
        this.idStation = UUID.randomUUID();
        this.nameStation = nameStation;
        this.position = position;
        this.maxFuel = maxFuel;
        this.maxNumberOfPlanes = maxNumberOfPlanes;
        stationPlanes= new HashMap<>();
    }


    public Station(){

    }

    public UUID getIdStation() {
        return idStation;
    }
}
