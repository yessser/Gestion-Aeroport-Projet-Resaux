package models;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        while(true) {
            try {
                if (!this.serverSocket.isClosed()) {
                    Socket socket = this.serverSocket.accept();
                    System.out.println("A new Client connected");
                    ClientHandler clientHandler = new ClientHandler(socket);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                    continue;
                }
            } catch (IOException var4) {
                System.out.println("Connection ERROR");
            }

            return;
        }
    }

    public void closeServerSocket() {
        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3336);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
