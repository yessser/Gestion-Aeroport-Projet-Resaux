package models;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.util.concurrent.*;

public class Server extends UnicastRemoteObject implements ServerInterface {
    private ExecutorService threadPool;
    private Map<Plane, Thread> threadMap;
    private Map<UUID, Object> lockMap = new HashMap<>();

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
            Object lockC = new Object();
            lockMap.put(obj.getIdPlane(), lockC);
            System.out.println("Received object: " + obj);

            //* test
        });
    }


    public void startFlight(UUID id) throws RemoteException {

            // checks for the existence of a flight for a plane before notifying the plane client

            Object locky = lockMap.get(id);

            synchronized (locky) {
                locky.notify();
            }



    }

    public Flight getFlight() throws RemoteException {
        //TODO add FLight info from GUI here

        // test only delete later
        Position pos = new Position(45.0,44.0);
        Plane objToSend = new Plane(55.0, 456.0, 55.0, 55.0, 45.0,pos);
        ArrayList<Station> lst = new ArrayList<>();
        Station s = new Station("ff",pos,1.0,5);
        s.addPlane(objToSend);
        lst.add(s);



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

    public Flight waitForFlight(UUID id) throws RemoteException {

        Object locky = lockMap.get(id);
        System.out.println(locky);
        synchronized (locky) {
            try {
                locky.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Flight flight = controlTower.searchFlightByPlaneID(id);
        if (flight != null) {
            return flight;
        }
        return null;
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
            Thread.sleep(5000);

            System.out.println(server.getThreadMap().values());

            Thread.sleep(15000);
            System.out.println("it's trying with multi clients ");


            // wake client
            //server.startFlight();

            // *******
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
