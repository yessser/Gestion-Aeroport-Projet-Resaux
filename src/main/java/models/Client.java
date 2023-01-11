package models;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username = "55";

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
        } catch (IOException var4) {
            this.closeEverything(socket, this.bufferedReader, this.bufferedWriter);
        }

    }

    public void sendMessage() {
        try {
            this.bufferedWriter.write(this.username);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);

            while(this.socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                this.bufferedWriter.write(this.username + " : " + messageToSend + "\n");
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
                    String msgFromGroupChat = Client.this.bufferedReader.readLine();
                    System.out.println(msgFromGroupChat);
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
        String username = "55";
        new Scanner(System.in);
        Socket socket = new Socket("localhost", 3336);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
}
