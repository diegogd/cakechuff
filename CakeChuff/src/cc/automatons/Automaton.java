package cc.automatons;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observer;

import cc.communications.Mailbox;
import cc.sensors.Sensor;

public abstract class Automaton implements Observer{
	protected int state;
	
	//comm
	protected Socket sout;
	protected DataOutputStream dout;
	protected Mailbox mbox;
	protected String master;
	protected int portout;
	public Automaton() {
	}

	/*
	 * Manage new messages in the mailbox.
	 * The mailbox will run this method when a new message
	 * arrives.
	 * @param msg Message received.
	 */
	public abstract void newMsg(String msg);
	/*
	 * Send a message to the master automaton.
	 * @param msg Message to be sent.
	 */
	protected void send(String msg){
		
			try{
				dout.writeChars(msg);
			}catch(IOException ioe){
				//restart the receiving connection
				if(!mbox.isFailure())mbox.setFailure(true);
				//connection failure
				boolean success=false;
				while(!success){
					try{
					sout= new Socket(master, portout);
					dout = new DataOutputStream(sout.getOutputStream());
					}catch(IOException ioe2){
						//keep trying
					}
				}
			}
			
	}

}
