package cc.automatons;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observer;

import cc.communications.Mailbox;
import cc.sensors.Sensor;

public abstract class Automaton implements Observer, Runnable{
	protected int state;
	protected boolean stop;
	protected boolean treatingupdate;
	//comm
	protected Socket sout;
	//protected DataOutputStream dout;
	PrintWriter dout;
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
				dout.println(msg);
			}catch(Exception e){
				//restart the receiving connection
				if(!mbox.isFailure())mbox.setFailure(true);
				//connection failure
				boolean success=false;
				while(!success){
					try{
					sout= new Socket(master, portout);
					dout = new PrintWriter(sout.getOutputStream(),true); 
					}catch(IOException ioe2){
						//keep trying
					}
				}
			}
			
	}

}
