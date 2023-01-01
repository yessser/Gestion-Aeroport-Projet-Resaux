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

     public Task<Position> moveTo(Position newPos){
//        TODO:may
        Task<Position> task = new Task<Position>() {
            @Override protected Position call() throws Exception {
                Position ogPos= new Position(position.positionlat.get(),position.positionLon.get());
                System.out.println("distance"+ogPos.distance(newPos));
                Double totalTime =ogPos.distance(newPos)/speed;

                System.out.println("total time "+totalTime);
                Double t;
                for (t=0D;t<=totalTime;t+=1) {
                    Thread.sleep(500);
                    if (isCancelled()) {
                        updateMessage("Cancelled");
                        break;
                    }
                    position.positionlat.set(ogPos.positionlat.get()+(newPos.positionlat.get()-ogPos.positionlat.get())*t/totalTime);
                    position.positionLon.set(ogPos.positionLon.get()+(newPos.positionLon.get()-ogPos.positionLon.get())*t/totalTime);
//                    TODO:check for planes in the dangerzone and  do stuff to move away
//                    TODO:add connection and hen the client disconnect
                    updateValue(new Position(position.positionlat.get(),position.positionLon.get()));
//                    updateProgress(iterations, 1000);

                }
                position=newPos;
                return newPos;
            }
        };
        return task;
    }
}
