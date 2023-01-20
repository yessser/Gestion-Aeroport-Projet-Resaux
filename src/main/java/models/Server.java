package models;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.util.concurrent.*;

public class Server extends UnicastRemoteObject implements ServerInterface {
    private ExecutorService threadPool;
    private Map<Plane, Thread> threadMap;
    private Object lock;
    private ControlTower controlTower;
    public Server() throws RemoteException {
        super();
        threadPool = Executors.newCachedThreadPool();
        threadMap = new HashMap<>();
        lock = new Object();
    }

    public Plane getObject() throws RemoteException {
        // Create the object to be sent
        //Plane obj = new Plane("hello", 123);
        return null;
    }


    public void updateStation(Station station)throws RemoteException{
        controlTower.getAllStations().put(station.getIdStation(),station);
    }
    public Collection<Station> getAllStations() throws RemoteException{
        return controlTower.getAllStations().values();
    }
    public void bindClientPlane(Plane obj) throws RemoteException {
        threadPool.execute(() -> {
            //Handle the received object in a separate thread
            Thread currentThread = Thread.currentThread();
            threadMap.put(obj, currentThread);
            System.out.println("Received object: " + obj);
        });
    }


    public void startFlight() throws RemoteException {
            synchronized (lock) {
                lock.notify();
            }
    }

    public Flight getFlight() throws RemoteException {
        //TODO add FLight info from GUI here

        // test only delete later
        Position pos = new Position(45.0,44.0);
        Plane objToSend = new Plane(55.0, 456.0, 55.0, 55.0, 45.0,pos);
        ArrayList<Station> lst = new ArrayList<>();
        lst.add(new Station("ff",pos,1.0,5));
        try {

            return new Flight(objToSend,lst);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // ************

    }

    @Override
    public void sendPosition(Position position) throws RemoteException {
        //TODO add rotation and location for plane GUI
        System.out.println(position.positionLon + position.positionLon);
    }

    public void waitForFlight() throws RemoteException {

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<Plane, Thread> getThreadMap() throws RemoteException{
        return threadMap;
    }

    public static void main(String[] args) {
        try {
            // Create and export the server object
            Server server = new Server();
            Naming.rebind("rmi://localhost:1099/MyServer", server);

            System.out.println("Server ready.");

            // testing ***


            System.out.println(server.getThreadMap().values());



            // wake client
            server.startFlight();

            // *******
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
