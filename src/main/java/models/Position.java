package models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Position {
// this may not be needed and might just use Double its like this rn cause i tried  using BIND to the property
    public DoubleProperty positionlat;
    public DoubleProperty positionLon;

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

    public Position(DoubleProperty positionlatProperty, DoubleProperty positionLonProperty) {
        this.positionlat = positionlatProperty;
        this.positionLon = positionLonProperty;
    }
    public Position(Double positionlat, Double positionLon) {
        this.positionlat = new SimpleDoubleProperty( positionlat);
        this.positionLon = new SimpleDoubleProperty(positionLon);
    }
    public Double distance(Position newPos) {

        final double R = 6372.8; // Earth's radius In kilometers

        double dLat = Math.toRadians(newPos.positionlat.get() - positionlat.get());
        double dLon = Math.toRadians(newPos.positionLon.get() - positionLon.get());
        double lat1 = Math.toRadians(positionlat.get());
        double lat2 = Math.toRadians(newPos.positionlat.get());

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return R * c;
    }

}
