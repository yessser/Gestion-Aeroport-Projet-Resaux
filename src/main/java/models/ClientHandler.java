package models;

import javafx.beans.property.DoubleProperty;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientHandler implements Runnable {
    public static HashMap<Plane,ClientHandler> clientHandlers = new HashMap<>();
    private Socket socket;
    public ObjectInputStream bufferedReader;
    public ObjectOutputStream bufferedWriter;
    public float positionlat;
    public float positionLon;

    public ClientHandler(Socket socket) {
        try {
            System.out.println("BEEP");

            this.socket = socket;
            System.out.println("read object ");
            this.bufferedReader = new ObjectInputStream(socket.getInputStream());
            Plane p = (Plane)bufferedReader.readObject();
            System.out.println(p.getPosition());
            clientHandlers.put(p,this);

        } catch (IOException var3) {
            this.closeEverything(socket, this.bufferedReader, this.bufferedWriter);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("crash");
    }

    public void run() {

    }

//    public void sendDangerPlanes(ArrayList<Plane> dangerPlanes,Plane p) {
//            try {
//                for(ClientHandler clientHandler:clientHandlers){
//                    clientHandler.bufferedWriter.writeObject(dangerPlanes);
//                    clientHandler.bufferedWriter.flush();
//                }
//            } catch (IOException e) {
//                this.closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//
//            return;
//    }


    public void listenForMessage() {

        (new Thread(new Runnable() {
            public void run() {
                while(ClientHandler.this.socket.isConnected()) {
                    try {
                        int i = 0;
                        String msgRecieved = ClientHandler.this.bufferedReader.readLine();
                        System.out.println(msgRecieved);

                        String regex="([0-9]+[.][0-9]+)";
                        String input= msgRecieved;

                        Pattern pattern=Pattern.compile(regex);
                        Matcher matcher=pattern.matcher(input);

                        while(matcher.find()) {
                            if (i==0)
                               positionlat = Float.parseFloat(matcher.group(1));
                            else
                                positionLon = Float.parseFloat(matcher.group(1));
                            i++;
                        }
                        i=0;
                    } catch (IOException var3) {
                        ClientHandler.this.closeEverything(ClientHandler.this.socket, ClientHandler.this.bufferedReader, ClientHandler.this.bufferedWriter);
                    }
                }

            }
        })).start();
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
//        this.broadcastMessage();
    }

    public void closeEverything(Socket socket, ObjectInputStream bufferedReader, ObjectOutputStream bufferedWriter) {
        this.removeClientHandler();

        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (bufferedWriter != null) {
                bufferedWriter.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }
}
