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

//        TODO:change this to the correct distance calc HAVERSINE FORMULA
        return Math.sqrt(Math.pow(positionlat.get()-newPos.positionlat.get(),2)+Math.pow(positionLon.get()-newPos.positionLon.get(),2));
    }

}
