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

/**
 * Communication channel for the Subsystem Automatons side
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Mailbox implements Runnable {

	private ServerSocket ss;
	private Socket so;
	private BufferedReader din;
	private ArrayList<String> msg_list;
	private Automaton owner;
	private boolean failure;
		
	/**
	 * Constructor
	 * Set the owner and the port of the mailbox
	 * @param owner Automaton owner of the mailbox
	 * @param port Port used by mailbox
	 */
	public Mailbox(Automaton owner, int port) throws IOException{
		this.owner=owner;
		ss = new ServerSocket(port);
		msg_list=new ArrayList<String>();
	}
	
	/**
	 * Get the state of the mailbox connection
	 * @return failure True if the mailbox connection has failed
	 */
	public boolean isFailure() {
		return failure;
	}

	/**
	 * Set if the mailbox connection has failed
	 * @param failure True if the mailbox connection has failed
	 */
	public void setFailure(boolean failure) {
		this.failure = failure;
	}
	
	/**
	 * Run the mailbox connection while no failure is produced
	 * @exception IOException Communication error
	 */
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

	/**
	 * Connect the mailbox with the server socket
	 * @throws IOException Communication error
	 */
	private void connect() throws IOException {
		so = ss.accept();
		din= new BufferedReader(new InputStreamReader( so.getInputStream()));
	}

	/**
	 * Loop for receiving messages from the MasterMailbox
	 * @exception Connection failure
	 * @exception IOException Communication error
	 */
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
	
	/**
	 * Close the sockets and buffers of the mailbox
	 * @exception IOException Communication error
	 */
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
