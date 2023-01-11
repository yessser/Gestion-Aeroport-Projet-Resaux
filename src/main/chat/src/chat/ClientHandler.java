package chat;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket){
        try {
            this.socket=socket;
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);

        }
    }


    @Override
    public void run() {

      //  while(socket.isConnected()){
               // msgToSend = bufferedReader.readLine();
                listenForMessage();
                broadcastMessage();

        //}
    }

    public void broadcastMessage(){
        try {
        while (socket.isConnected()) {
            Scanner scanner = new Scanner(System.in);
            String msgToSend =scanner.nextLine();

            for (ClientHandler clientHandler : clientHandlers) {
                //System.out.println(clientHandler.clientUsername);

                    //if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(msgToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                    //}
                }
            }
        }catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgRecieved;

                while(socket.isConnected()){
                    try{
                        msgRecieved = bufferedReader.readLine();
                        System.out.println(msgRecieved);
                    }catch (IOException e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage();

    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try {
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
