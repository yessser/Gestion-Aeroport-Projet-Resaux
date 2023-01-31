package models;

import java.rmi.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Collection;

public interface ServerInterface extends Remote {
    public void updateStation(Station station)throws RemoteException;
    public ArrayList<Station> getAllStations()throws RemoteException;
    public Plane getObject() throws RemoteException;
    public void bindClientPlane(Plane obj) throws RemoteException;
    public Flight waitForFlight(UUID id) throws RemoteException;
    public void startFlight(UUID id) throws RemoteException;
    public Flight getFlight() throws RemoteException;
    public void sendPosition(Plane plane) throws RemoteException;

    void despawnPlane(UUID idPlane) throws  RemoteException;
}
