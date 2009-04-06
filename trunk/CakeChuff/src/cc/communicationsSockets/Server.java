package cc.communicationsSockets;

import java.net.*;
import java.io.*;

public class Server extends Entity{
    private Socket _socket;    

    public Server(int port) {
	ServerSocket myServerSocket;
	try{	
            myServerSocket = new ServerSocket(port);
            _socket = myServerSocket.accept();
        } catch (IOException e) {
            System.out.println("Error while creating the socket with the server:");
            e.printStackTrace();
            System.exit(-2);
        }
    }
	
    public Socket getSocket(){
        return this._socket;
    }
    
    public int closeCommunications(Socket socket, BufferedReader in){
        try {
            //Finish the communication by closing buffers and sockets
            in.close();
            socket.close();
            System.out.println("Finished");
            return 0;
        } catch(IOException e) {
            System.out.println("Error while closing buffers and socket:");
            e.printStackTrace();
            System.exit(-6);
            return -6;
        }
    }
}

