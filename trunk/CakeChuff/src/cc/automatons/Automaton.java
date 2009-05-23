package cc.automatons;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observer;

import cc.communications.Mailbox;

/**
 * Generic Automaton which defines the basic communication features
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public abstract class Automaton implements Observer, Runnable{
	protected int state;
	protected boolean stop;
	protected boolean treatingupdate;
	//comm
	protected Socket sout;
	//protected DataOutputStream dout;
	PrintWriter dout;
	protected Mailbox mbox;
	public Thread mbox_thread;
	protected String master;
	protected int portout;
	public Automaton() {
	}

	/**
	 * Manage the new arrived messages in the mailbox
	 * @param msg Message received.
	 */
	public abstract void newMsg(String msg);
	
	/**
	 * Send a message to the master automaton.
	 * @param msg Message to be sent.
	 */
	protected void send(String msg){
			try{
				dout.println(msg);
			}catch(Exception e){
				e.printStackTrace();
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
	
	/**
	 * Stop the automaton's thread
	 * Deprecated stop method is employed for an "unclean" sudden stop
	 */
	//
	@SuppressWarnings("deprecation")
	public void destroyAutomaton(){
		mbox_thread.stop();
	}

}
