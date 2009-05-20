package cc.communications;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import cc.scada.SCADA;

public class ScadaMailbox implements Runnable {

	private ServerSocket ss;
	private Socket so;
	private BufferedReader din;
	private ArrayList<String> msg_list;
	private SCADA owner;
	private boolean failure;
	public boolean isFailure() {
		return failure;
	}

	public void setFailure(boolean failure) {
		this.failure = failure;
	}

	public ScadaMailbox(SCADA owner, int port) throws IOException{
		this.owner=owner;
		ss = new ServerSocket(port);
		msg_list=new ArrayList<String>();
	}
	
	//@Override
	public void run() {
		//Accept connections
		try{
			this.connect();
			while(!failure){
				receiveMsgs();
			}
		}catch(IOException ioe){
			//cannot connect
		}
		
	}

	private void connect() throws IOException {
		so = ss.accept();
		din= new BufferedReader(new InputStreamReader( so.getInputStream()));
	}

	private void receiveMsgs() {
		String msg;
		while (true) {
			try {
				//msg = din.readUTF();
				msg=din.readLine();
				owner.newMsg(msg);				
			} catch (IOException ioe) {
				// connection failure
				ioe.printStackTrace();
			}
		}
	}
}