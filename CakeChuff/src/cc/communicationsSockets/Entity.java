package cc.communicationsSockets;

import java.net.*;
import javax.net.ssl.*;
import java.io.*;

public class Entity {

    public Entity() {
    }

	public void sendMessage(PrintWriter out, File f){
		try{
			int c=2;
			FileInputStream fis = new FileInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			System.out.println("Sending....");
			String msg = br.readLine();
			while(msg.length()>0 || c>0){
				System.out.println(msg);
				out.println(msg);
				msg = br.readLine();
				if (msg.length()==0){
					c--;
				}
			}
			out.println(); 
			out.flush();
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public File getResponse(BufferedReader in, String filename) throws java.io.IOException{
		int c=2;
		File f = new File(filename);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		String incoming;
		System.out.println("Receiving....");
		incoming = in.readLine();
		if (incoming.length() > 0){
			bw.write(incoming);
			bw.newLine();
		}
		while (incoming.length() > 0 || c>0) {
			System.out.println(">>"+incoming);
			incoming = in.readLine();
			if (incoming.length()==0){
				c--;
			}
			bw.write(incoming);
			bw.newLine();        
		}
		System.out.println();
		bw.close();
		return f;
	}
	
	public int closeCommunications(Socket socket, BufferedReader in, PrintWriter out){
		try {
			//Finish the communication by closing the socket and I/O buffers
			in.close();
			out.close();
			socket.close();
			System.out.println("Finished");
			return 0;
		} catch(IOException e) {
			System.out.println("Error while closing the buffers and sockets:");
			e.printStackTrace();
			System.exit(-6);
			return -6;
		}
	}

}
