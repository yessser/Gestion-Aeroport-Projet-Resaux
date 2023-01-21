package models;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

public class Plane implements Serializable {
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
//        this to make movment from km/h to angular speed on earth
//        and also devide by 7200 to go to km/0,5 second (our client updates)
        this.speed = speed/(7200*6372.8);
        this.rotationSpeed = rotationSpeed;
        this.sizePlane = sizePlane;
        this.dangerZoneSize = dangerZoneSize;
        this.position = position;
    }
    public Double getCurrentRotation() {
        return currentRotation;
    }
    Position nextPos(){
        double prelat = Math.toRadians(this.position.positionlat);
        double prelon = Math.toRadians(this.position.positionLon);
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
        double deltalon = dx/111.320*Math.cos(position.positionlat);
        double deltalat = dy/110.54;
        return new Position(this.position.positionlat+deltalat,this.position.positionLon+deltalon);
    }
    Position nextPosRight(){
        double prelat = Math.toRadians(this.position.positionlat);
        double prelon = Math.toRadians(this.position.positionLon);
        Double lat =Math.asin(Math.sin(prelat)*Math.cos(speed)
                +Math.cos(prelat)*Math.sin(speed)*Math.cos(currentRotation+rotationSpeed));
        Double lon = prelon+
                Math.atan2(Math.sin(currentRotation+rotationSpeed)*Math.sin(speed)*Math.cos(prelat),
                        Math.cos(speed)-Math.sin(prelat)*Math.sin(lat));
        return new Position(Math.toDegrees(lat),Math.toDegrees(lon));
//        double dx=speed*6372.8*Math.sin(currentRotation+rotationSpeed);
//        double dy=speed*6372.8*Math.cos(currentRotation+rotationSpeed);
//        double deltalon = dx/111.320*Math.cos(position.positionlat);
//        double deltalat = dy/110.54;
//        return new Position(this.position.positionlat+deltalat,this.position.positionLon+deltalon);
    }
    Position nextPosLeft(){
        double prelat = Math.toRadians(this.position.positionlat);
        double prelon = Math.toRadians(this.position.positionLon);
        Double lat2 =Math.asin(Math.sin(prelat)*Math.cos(speed)
                +Math.cos(prelat)*Math.sin(speed)*Math.cos(currentRotation-rotationSpeed));
        Double lon2 = prelon+
                Math.atan2(Math.sin(currentRotation-rotationSpeed)*Math.sin(speed)*Math.cos(prelat),
                        Math.cos(speed)-Math.sin(prelat)*Math.sin(lat2));
        return new Position(Math.toDegrees(lat2),Math.toDegrees(lon2));
//        double dx=speed*6372.8*Math.sin(currentRotation-rotationSpeed);
//        double dy=speed*6372.8*Math.cos(currentRotation-rotationSpeed);
//        double deltalon = dx/111.320*Math.cos(position.positionlat);
//        double deltalat = dy/110.54;
//        return new Position(this.position.positionlat+deltalat,this.position.positionLon+deltalon);
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
     public void moveTo(Position newPos, ServerInterface server) throws InterruptedException {
//        TODO:update reservoir after each move
//         todo check for crash
//         todo if two planes going to the same station might soft lock cause they avoid each other
        //turn the plane to the distination direction
         currentRotation=this.position.calculateRotation(newPos);


         System.out.println("STARTING THREAD");
                while(position.distance(newPos)>speed){
//                    todo ask server for nearby planes
                    System.out.println(position.distance(newPos));
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
                    try {
                        server.sendPosition(this);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }


                    System.out.println("POS:"+position);
                }

                System.out.println("POOOOOOOOOOOP");
                position=newPos;
                 try {
                     System.out.println(this);
                     server.sendPosition(this);
                 } catch (RemoteException e) {
                     throw new RuntimeException(e);
                 }



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
