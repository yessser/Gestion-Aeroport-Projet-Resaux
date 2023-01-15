package models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;

import java.io.Serializable;

public class Position implements Serializable {
// this may not be needed and might just use Double its like this rn cause i tried  using BIND to the property
    public Double positionlat;
    public Double positionLon;

    public Position(Position newPosition) {
        this.positionlat=newPosition.positionlat;
        this.positionLon=newPosition.positionLon;
    }

    @Override
    public String toString() {
        return "Position{" +
                "positionlat=" + positionlat +
                ", positionLon=" + positionLon +
                '}';
    }


    public Position(Double positionlat, Double positionLon) {
        if(positionLon>180||positionLon<=-180){
            positionLon=-1*positionLon%180;
        }
        if(positionlat>90||positionlat<=-90){
            positionlat=-1*positionlat%90;
        }
        this.positionlat = positionlat;
        this.positionLon = positionLon;
    }
    public Double distance(Position newPos) {

        final double R = 6372.8; // Earth's radius In kilometers

        double dLat = Math.toRadians(newPos.positionlat - positionlat);
        double dLon = Math.toRadians(newPos.positionLon - positionLon);
        double lat1 = Math.toRadians(positionlat);
        double lat2 = Math.toRadians(newPos.positionlat);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return R * c;
    }

    public Double calculateRotation(Position p2){
//        this function is given a point you wanna turn to
//        and give you the angle to it in compared to the north pole in radian
        Position p1 = this;
        //this is some math magic from this https://www.movable-type.co.uk/scripts/latlong-vectors.html
        //IMPORTANT NOTE:
        //each equation translate to a side of the globe in our -z -y x javafx world this is the correct orritentation of stuff
        Point3D vecPos1 = p1.toVec();
        Point3D vecPos2 = p2.toVec();
        Point3D greatCircleAB = vecPos1.crossProduct(vecPos2);
//        0 -1 0 is the North Pole in our world
        Point3D greatCircleANorth = vecPos1.crossProduct(new Point3D(0,-1,0));
        Double sinTheta = greatCircleAB.crossProduct(greatCircleANorth).magnitude() * Math.signum( greatCircleAB.crossProduct(greatCircleANorth).dotProduct(vecPos1)) ;
        Double cosTheta = greatCircleAB.dotProduct(greatCircleANorth);
        Double theta= Math.atan2(sinTheta,cosTheta);
//        System.out.println("angle in rad "+theta);
        System.out.println("angle in degree "+Math.toDegrees(theta));
        return theta;
    }

    public Point3D toVec() {
        return new Point3D(
                Math.cos(Math.toRadians(this.positionlat))*Math.sin(Math.toRadians(this.positionLon)), //our east is on x
                Math.sin(Math.toRadians(this.positionlat))*-1,// our north point is on -y
                Math.cos(Math.toRadians(this.positionlat))*Math.cos(Math.toRadians(this.positionLon))*-1//our 0 0 cord is in -z
        );
    }

}
