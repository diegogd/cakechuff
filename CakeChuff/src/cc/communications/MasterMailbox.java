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
	 * @param owner MasterAutomaton
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
			mbox= new Mailbox(owner, portin, this);
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
				try {
					dout.println(msg);
					success = true;
				} catch (Exception e) {
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
	 * The Incoming mailbox paired with this outgoing mastermailbox has detected an
	 * incoming connection failure, asking for reestablishing the outgoing connection too
	 * 
	 */
	public void reconnect(){
		try {
			sout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dout.close();
		connect();
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
				// TODO Auto-generated catch block
				ioe.printStackTrace();
			}
		}
	}
}
