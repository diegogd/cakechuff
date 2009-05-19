package cc.communications;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import cc.automatons.Automaton;

public class Mailbox implements Runnable {

	private ServerSocket ss;
	private Socket so;
	//private DataInputStream din;
	private BufferedReader din;
	//private DataOutputStream dout;
	private ArrayList<String> msg_list;
	private Automaton owner;
	private boolean failure;
	public boolean isFailure() {
		return failure;
	}

	public void setFailure(boolean failure) {
		this.failure = failure;
	}

	public Mailbox(Automaton owner, int port) throws IOException{
		this.owner=owner;
		ss = new ServerSocket(port);
		msg_list=new ArrayList<String>();
	}
	
	@Override
	public void run() {
		//Accept connections
		try{
			System.out.println("Mailbox: Connecting...");
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
		//din = new DataInputStream(so.getInputStream());
		//dout = new DataOutputStream(so.getOutputStream());
	}

	private void receiveMsgs() {
		String msg;
		while (true) {
			try {
			//System.out.println("Receiving message...");
				//msg = din.readUTF();
				msg=din.readLine();
				System.out.println("Received:"+msg);
				owner.newMsg(msg);				
			} catch (IOException ioe) {
				// connection failure
			}
		}
	}
}
