package models;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class Plane {
    private static int id = 0;
    private int idPlane;
    private Double reservoirMax;
    private Double speed;
    private Double rotationSpeed;
    private Double sizePlane;
    private Double dangerZoneSize;

    private Position position;
    private PlaneState state;



    public Plane( Double reservoirMax, Double speed, Double rotationSpeed, Double sizePlane, Double dangerZoneSize,Position position) {
        this.idPlane = id;
        id++;
        this.reservoirMax = reservoirMax;
        this.speed = speed;
        this.rotationSpeed = rotationSpeed;
        this.sizePlane = sizePlane;
        this.dangerZoneSize = dangerZoneSize;
        this.position = position;
    }

    public void setHourD(String HourD){
        this.HourD = HourD;
    }

    public String getHourD(){
        return HourD;
    }

    public void setHourA(String HourA){
        this.HourA = HourA;
    }

    public String getHourA(){
        return HourA;
    }

    ///
    public void setNumV(String NumV){
        this.NumV = NumV;
    }

    public String getNumV(){
        return NumV;
    }

    public void setAeroD(String AeroD){
        this.AeroD = AeroD;
    }

    public String getAeroD(){
        return AeroD;
    }

    public void setAeroA(String AeroA){
        this.AeroA = AeroA;
    }

    public String getAeroA(){
        return AeroA;
    }

    public int getrefA(){
        return refA;
    }

    public void setTV(String TV){
        this.TV = TV;
    }

    public String getTV(){
        return TV;
    }

    public void setSpeed(int Speed){
        this.Speed = Speed;
    }

    public int getSpeed(){
        return Speed;
    }
}
