package models;

import java.rmi.*;
import java.util.ArrayList;

public class Client {

    
    public static void main(String[] args) {
        try {


            // Lookup the server object
            ServerInterface server = (ServerInterface) Naming.lookup("rmi://localhost:1099/MyServer");


            // testing **
            Position pos = new Position(45.0,44.0);
            Plane p1 = new Plane(55.0, 456.0, 55.0, 55.0, 45.0,pos);
            // *****

            // Send the object to the server
            server.bindClientPlane(p1);

            // wait for Flight signal
            server.waitForFlight();

            Flight currentFlight = server.getFlight();

            for (int i = 0; i < currentFlight.visitedStations.size(); i++) {
                p1.moveTo(currentFlight.visitedStations.get(i).getPosition(), server);

            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
