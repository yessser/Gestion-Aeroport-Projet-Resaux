package models;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUsername = this.bufferedReader.readLine();
            clientHandlers.add(this);
        } catch (IOException var3) {
            this.closeEverything(socket, this.bufferedReader, this.bufferedWriter);
        }

    }

    public void run() {
        this.listenForMessage();
        this.broadcastMessage();
    }

    public void broadcastMessage() {
        label23:
        while(true) {

            try {
                if (this.socket.isConnected()) {
                    Scanner scanner = new Scanner(System.in);
                    String msgToSend = scanner.nextLine();

                    Iterator clientsList = clientHandlers.iterator();

                    while(true) {
                        if (!clientsList.hasNext()) {
                            continue label23;
                        }

                        ClientHandler clientHandler = (ClientHandler)clientsList.next();
                        clientHandler.bufferedWriter.write(msgToSend);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();
                    }
                }
            } catch (IOException e) {
                this.closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return;
        }
    }

    public void listenForMessage() {
        (new Thread(new Runnable() {
            public void run() {
                while(ClientHandler.this.socket.isConnected()) {
                    try {
                        String msgRecieved = ClientHandler.this.bufferedReader.readLine();
                        System.out.println(msgRecieved);
                    } catch (IOException var3) {
                        ClientHandler.this.closeEverything(ClientHandler.this.socket, ClientHandler.this.bufferedReader, ClientHandler.this.bufferedWriter);
                    }
                }

            }
        })).start();
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        this.broadcastMessage();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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
