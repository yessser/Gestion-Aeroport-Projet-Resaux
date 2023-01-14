package models;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    private Plane airplane;

    public Client(Socket socket, String username, Plane airplane) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
            this.airplane = airplane;
        } catch (IOException var4) {
            this.closeEverything(socket, this.bufferedReader, this.bufferedWriter);
        }

    }

    public void sendMessage(Position pos) {
        try {
           /* this.bufferedWriter.write(this.username);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();*/
            Scanner scanner = new Scanner(System.in);

            while(this.socket.isConnected()) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                String messageToSend = scanner.nextLine();
                this.bufferedWriter.write(this.username + " : " + pos.toString());
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();
            }
        } catch (IOException var3) {
            this.closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }

    }

    public void listenForMessage() {
        (new Thread(() -> {
            while(Client.this.socket.isConnected()) {
                try {
                    String msgFromTower = Client.this.bufferedReader.readLine();
                    System.out.println(msgFromTower);
                  //  System.out.println(airplane.getPosition().toString());
                } catch (IOException var3) {
                    Client.this.closeEverything(Client.this.socket, Client.this.bufferedReader, Client.this.bufferedWriter);
                }
            }

        })).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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

    public static void main(String[] args) throws IOException {


        //TODO check if the client is already created from flights
        Plane plane = new Plane(255.0, 450.0, 10.0, 10.0, 40.0,new Position(20.0D,45.0D ));

        String username = plane.getIdPlane().toString();
        new Scanner(System.in);
        Socket socket = new Socket("localhost", 3337);


        Client client = new Client(socket, username, plane);

        client.listenForMessage();
        client.sendMessage(plane.getPosition());
    }
}
