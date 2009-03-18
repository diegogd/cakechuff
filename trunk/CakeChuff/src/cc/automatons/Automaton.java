package cc.automatons;

import cc.communications.Mailbox;
import cc.sensors.Sensor;

public abstract class Automaton {
	
	private Mailbox _mailbox;
	
	public Automaton() {
		//_mailbox = new Mailbox();
	}

	public abstract void start();
	
	public abstract void pause();
	
	public abstract void stop();
	
	public abstract void notifySensorChange(Sensor s);
}
