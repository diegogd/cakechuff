//TODO:Recepción después del reinicio
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
	private BufferedReader din;
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
		while (!failure) {
			try {
				System.out.print("[Mailbox]: Connecting...");
				this.connect();
				System.out.println("ok");
				receiveMsgs();
			} catch (IOException ioe) {
				// cannot connect
				ioe.printStackTrace();
			}
		}
		
	}

	private void connect() throws IOException {
		so = ss.accept();
		din= new BufferedReader(new InputStreamReader( so.getInputStream()));
	}

	private void receiveMsgs() {
		String msg;
		while (!failure) {
			try {
				msg=din.readLine();
				owner.newMsg(msg);				
			} catch (Exception e) {
				// connection failure
				try{
					System.out.print("[Mailbox]:Error, Re-connecting...");
					this.connect();
					System.out.println("ok");
				}catch(IOException ioe){
					//cannot connect
					ioe.printStackTrace();
				}
			}
		}
	}
	public void end(){
		try {
			so.close();
			ss.close();
			din.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
