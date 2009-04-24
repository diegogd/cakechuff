package cc.automatons;

import java.util.Observer;

import cc.communications.Mailbox;
import cc.sensors.Sensor;

public abstract class Automaton implements Observer{
	
	protected Mailbox mbox;
	
	public Automaton() {
		//_mailbox = new Mailbox();
	}
	/*
	public abstract void start();
	
	public abstract void pause();
	
	public abstract void stop();
	
	public abstract void notifySensorChange(Sensor s);
	*/
	/*
	 * Manage new messages in the mailbox.
	 * The mailbox will run this method when a new message
	 * (standard priority) arrives.
	 */
	public abstract void newMsg(String[] msg);

}
