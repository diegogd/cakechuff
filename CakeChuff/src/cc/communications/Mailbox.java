package cc.communications;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import cc.automatons.Automaton;

public abstract class Mailbox implements Runnable {

	ServerSocket ss;
	Socket so;
	DataInputStream din; 
	DataOutputStream dout;
	ArrayList<String> msg_list;
	
	public Mailbox(Automaton owner, int port) throws IOException{
		ss = new ServerSocket(port);
		msg_list=new ArrayList<String>();
	}
	
	@Override
	public void run() {
		//Accept connections
		try{
			this.connect();
			while(true){
				receiveMsgs();
			}
		}catch(IOException ioe){
			//cannot connect
		}
		
	}

	private void connect() throws IOException {
		so = ss.accept();
		din = new DataInputStream(so.getInputStream());
		dout = new DataOutputStream(so.getOutputStream());
	}

	private void receiveMsgs() {
		String msg;
		while (true) {
			try {
				msg = din.readUTF();
				manageMsg(msg);
			} catch (IOException ioe) {
				// connection failure, put automaton into failure state
			}
		}
	}
	/*
	 * Manage messages received, specific for each automaton
	 */
	protected abstract void manageMsg(String msg);
}
