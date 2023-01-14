package models;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.UUID;

public class Plane {
    private static int id = 0;
    private UUID idPlane;
    private Double reservoirMax;
    private Double speed;
    private Double rotationSpeed;
    private Double currentRotation;
    private Double sizePlane;
    private Double dangerZoneSize;

    private ArrayList<Plane> dangerZonePlanes=new ArrayList<>();
    private Position position;
    private PlaneState state;



    public Plane( Double reservoirMax, Double speed, Double rotationSpeed, Double sizePlane, Double dangerZoneSize,Position position) {
        this.idPlane = UUID.randomUUID();

        this.reservoirMax = reservoirMax;
        this.speed = speed;
        this.rotationSpeed = rotationSpeed;
        this.sizePlane = sizePlane;
        this.dangerZoneSize = dangerZoneSize;
        this.position = position;
    }
    public Double getCurrentRotation() {
        return currentRotation;
    }
    Position nextPos(){
        double prelat = Math.toRadians(this.position.positionlat.get());
        double prelon = Math.toRadians(this.position.positionLon.get());
        Double lat =Math.asin(Math.sin(prelat)*Math.cos(speed)
                +Math.cos(prelat)*Math.sin(speed)*Math.cos(currentRotation));
        Double lon = prelon+
                Math.atan2(Math.sin(currentRotation)*Math.sin(speed)*Math.cos(prelat),
                        Math.cos(speed)-Math.sin(prelat)*Math.sin(lat));
        return new Position(Math.toDegrees(lat),Math.toDegrees(lon));
    }
    Position nextPos2(){
        double dx=speed*6372.8*Math.sin(currentRotation);
        double dy=speed*6372.8*Math.cos(currentRotation);
        double deltalon = dx/111.320*Math.cos(position.positionlat.get());
        double deltalat = dy/110.54;
        return new Position(this.position.positionlat.get()+deltalat,this.position.positionLon.get()+deltalon);
    }
    Position nextPosRight(){
        double prelat = Math.toRadians(this.position.positionlat.get());
        double prelon = Math.toRadians(this.position.positionLon.get());
        Double lat =Math.asin(Math.sin(prelat)*Math.cos(speed)
                +Math.cos(prelat)*Math.sin(speed)*Math.cos(currentRotation+rotationSpeed));
        Double lon = prelon+
                Math.atan2(Math.sin(currentRotation+rotationSpeed)*Math.sin(speed)*Math.cos(prelat),
                        Math.cos(speed)-Math.sin(prelat)*Math.sin(lat));
        return new Position(Math.toDegrees(lat),Math.toDegrees(lon));
//        double dx=speed*6372.8*Math.sin(currentRotation+rotationSpeed);
//        double dy=speed*6372.8*Math.cos(currentRotation+rotationSpeed);
//        double deltalon = dx/111.320*Math.cos(position.positionlat.get());
//        double deltalat = dy/110.54;
//        return new Position(this.position.positionlat.get()+deltalat,this.position.positionLon.get()+deltalon);
    }
    Position nextPosLeft(){
        double prelat = Math.toRadians(this.position.positionlat.get());
        double prelon = Math.toRadians(this.position.positionLon.get());
        Double lat2 =Math.asin(Math.sin(prelat)*Math.cos(speed)
                +Math.cos(prelat)*Math.sin(speed)*Math.cos(currentRotation-rotationSpeed));
        Double lon2 = prelon+
                Math.atan2(Math.sin(currentRotation-rotationSpeed)*Math.sin(speed)*Math.cos(prelat),
                        Math.cos(speed)-Math.sin(prelat)*Math.sin(lat2));
        return new Position(Math.toDegrees(lat2),Math.toDegrees(lon2));
//        double dx=speed*6372.8*Math.sin(currentRotation-rotationSpeed);
//        double dy=speed*6372.8*Math.cos(currentRotation-rotationSpeed);
//        double deltalon = dx/111.320*Math.cos(position.positionlat.get());
//        double deltalat = dy/110.54;
//        return new Position(this.position.positionlat.get()+deltalat,this.position.positionLon.get()+deltalon);
    }
    Position nextPosClosest(Position p){

        Position p1= nextPosRight();

        Position p2= nextPosLeft();
        if(p1.distance(p)<p2.distance(p)){
            return p1;

        }
        return p2;
    }
    double sumOfDangerZoneDistances(Position pos){
        double s=0;
        for (Plane plane:dangerZonePlanes) {
            s=s + pos.distance(plane.position);
        }
        return s;
    }

    double anglebetween(double alpha,double beta){
        double phi = Math.abs(beta - alpha) % Math.PI;       // This is either the distance or 360 - distance
        double distance = phi > Math.PI/2 ? Math.PI - phi : phi;
        return distance;
    }
    public static void main(String[] args) {
        Position p1 = new Position(-10D,-10D);
        Position p2 = new Position(10.0,-10.0);
        Position p3 = new Position(10.0,10.0);
        Position p4 = new Position(-10.0,10.0);

        p1.calculateRotation(p2);
        p2.calculateRotation(p3);
        p3.calculateRotation(p4);
        p1.calculateRotation(p4);

    }
     public Task<Position> moveTo(Position newPos){
//        TODO:update reservoir after each move
//         todo check for crash
//         todo if two planes going to the same station might soft lock cause they avoid each other
        //turn the plane to the distination direction
         currentRotation=this.position.calculateRotation(newPos);

        Task<Position> task = new Task<Position>() {
            @Override
            protected Position call() throws Exception {
                Double t;
                System.out.println("STARTING THREAD");

                while(position.distance(newPos)>speed*6372.8){
                    System.out.println(position.distance(newPos));
                    System.out.println(speed*6372.8);
                    Thread.sleep(500);
//                    no planes near continue to destination
                    if(dangerZonePlanes.isEmpty()){
                        System.out.println("EMPTYY");
                            if(rotationSpeed>=anglebetween(currentRotation,position.calculateRotation(newPos))){
                                System.out.println("slightly");
                                currentRotation= position.calculateRotation(newPos);
                                position=nextPos();
                            }else {
//                            check to turn left or right
                                System.out.println("rotating");
                                position = nextPosClosest(newPos);
                                currentRotation= position.calculateRotation(newPos);
                            }

                    }
                    else{
                        System.out.println("TOOO CLOSE");
                            if(sumOfDangerZoneDistances(nextPosRight())>sumOfDangerZoneDistances(nextPosLeft())){
                                position = nextPosRight();
                            }else{
                                position = nextPosLeft();
                            }
                    }
                    updateValue(new Position(position.positionlat.get(),position.positionLon.get()));
                    System.out.println("POS:"+position);
                }
                System.out.println("POOOOOOOOOOOP");
                position=newPos;
                return newPos;
            }
        };
        return task;
    }

    public UUID getIdPlane() {
        return idPlane;
    }
    public Double getSizePlane() {
        return sizePlane;
    }
    public Double getDangerZoneSize() {
        return dangerZoneSize;
    }
    public void setDangerZonePlanes(ArrayList<Plane> dangerZonePlanes) {
        this.dangerZonePlanes = dangerZonePlanes;
    }
    public void addDangerZonePlane(Plane p){
        dangerZonePlanes.add(p);
    }
    public Position getPosition() {
        return position;
    }

    public PlaneState getState() {
        return state;
    }

    public void setState(PlaneState state) {
        this.state = state;
    }
}
