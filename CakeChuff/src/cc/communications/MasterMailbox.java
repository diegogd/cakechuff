package cc.communications;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import cc.automatons.MasterAutomaton;
import cc.simulation.state.CakeSubsystemState;

/**
 * Communication channel for the Master Automatons side
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class MasterMailbox implements Runnable{

	private Mailbox mbox;
	private Thread receiving;
	private MasterAutomaton owner;
	private Socket sout;
	//private DataOutputStream dout;
	private PrintWriter dout;
	private int portin, portout;
	private String destination;
	public Thread mbox_thread;
	Vector<String> msgs;
	
	/**
	 * Constructor
	 * Set the owner, the input port, the output port and the destination
	 * Address of the mailbox
	 * @param owner Automaton owner of the mailbox
	 * @param portin Input port used by mailbox
	 * @param portout Output port used by mailbox
	 * @param address Destination address of the mailbox
	 * @exception UnknownHostException Communication error
	 * @exception IOException Communication error
	 * @exception SecurityException Security error
	 */
	public MasterMailbox(MasterAutomaton owner, int portin, int portout, String address){
		this.portin=portin;
		this.portout=portout;
		this.destination=address;
		msgs=new Vector<String>();
		try{
			mbox= new Mailbox(owner, portin);
			mbox_thread = (new Thread(mbox));
			mbox_thread.start();
			//outgoing connection will be opened when the first message is sent
		}catch(UnknownHostException uhe){
			
		}catch(IOException ioe){
			
		}catch(SecurityException se){
			
		}		
	}
	
	/**
	 * Run the mailbox connection while messages are being sent
	 * @exception Exception Error
	 */
	public void run(){
		if(sout==null || !sout.isConnected()){
			connect();
		}
		while (msgs.size()>0){
			String msg=msgs.remove(0);
			boolean success=false;
			while (!success) {
				System.out.println();
				try {
					dout.println(msg);
					success = true;
					System.out.println("[Master]: Sent: "+msg);
				} catch (Exception e) {
					System.out.println("Error:");
					e.printStackTrace();
					// restart the receiving connection
					if (!mbox.isFailure())
						mbox.setFailure(true);
					connect();
				}
			}
		}
	}
	
	/**
	 * Send a message through the mailbox
	 * @param msg Message to be sent 
	 */
	public synchronized void send(String msg){
		msgs.add(msg);
		(new Thread(this)).start();
	}
	
	/**
	 * Connect the mailbox with the client socket and the destination buffer
	 * @exception IOException Communication error
	 */
	private void connect(){
		boolean success=false;
		while(!success){
			try{
				sout=new Socket(destination,portout);
				success=true;
				dout=  new PrintWriter(sout.getOutputStream(), true);
			}catch(IOException ioe){
				
			}
		}
	}
}
