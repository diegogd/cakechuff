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
	private MasterMailbox master;
		
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
	 * Constructor for a mailbox connected to a outgoing MasterMailbox
	 * Set the owner and the port of the mailbox
	 * @param owner Automaton owner of the mailbox
	 * @param port Port used by mailbox
	 * @param master Master Mailbox used together with this mailbox for I/O communication 
	 */
	public Mailbox(Automaton owner, int port, MasterMailbox master) throws IOException{
		this.owner=owner;
		ss = new ServerSocket(port);
		msg_list=new ArrayList<String>();
		this.master=master;
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
		while (true) {
			try {
				this.connect();
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
	 * @exception Exception Connection failure
	 * @exception IOException Communication error
	 */
	private void receiveMsgs() {
		String msg;
		while (!failure) {
			try {
				msg=din.readLine();
				if(msg!=null)
					owner.newMsg(msg);
				else{
					try{
						this.connect();
						if(master!=null){
							master.reconnect();
						}
					}catch(IOException ioe){
						//cannot connect
						ioe.printStackTrace();
					}
				}
			} catch (IOException ioe) {
				// connection failure
				ioe.printStackTrace();
			
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
			e.printStackTrace();
		}
	}
}
