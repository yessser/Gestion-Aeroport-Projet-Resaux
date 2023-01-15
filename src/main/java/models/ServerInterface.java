package models;

import java.rmi.*;

public interface ServerInterface extends Remote {
    public Plane getObject() throws RemoteException;
    public void bindClientPlane(Plane obj) throws RemoteException;
    public void waitForFlight() throws RemoteException;
    public void startFlight() throws RemoteException;
    public Flight getFlight() throws RemoteException;
    public void sendPosition(Position position) throws RemoteException;
}
