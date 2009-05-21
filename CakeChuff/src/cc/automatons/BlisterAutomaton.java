package cc.automatons;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import cc.communications.Mailbox;
import cc.simulation.elements.Sensor;
import cc.simulation.elements.LightSensor;
import cc.simulation.elements.TouchSensor;
import cc.simulation.state.BlisterSubsystemState;
import cc.simulation.state.CakeSubsystemState;
import cc.simulation.state.QualitySubsystemState;
import cc.simulation.state.SystemState;

public class BlisterAutomaton extends Automaton {

	private static final int START = 0;
	private static final int INIT = 1;
	private static final int PRESS = 2;
	private static final int CUTTING = 3;
	private static final int BLISTER_READY = 4;
	private static final int FAILURE = 5;

	// param
	private int belt_lg;
	private float speed;

	// simulation
	private BlisterSubsystemState blistersystem;
	private SystemState sys;
	private Stamper stamper;

	public BlisterAutomaton(int portin, int portout, String master) {
		state = START;
		stop = false;
		try {
			mbox = new Mailbox(this, portin);
			mbox_thread = (new Thread(mbox));
			mbox_thread.start();
			boolean connected = false;
			while (!connected) {
				try {
					sout = new Socket(master, portout);
					connected = true;
				} catch (Exception e) {
					System.err.println("[Blisters]: ERROR");
					e.printStackTrace();
				}
			}
			dout = new PrintWriter(sout.getOutputStream(), true);
			// subscribe
			blistersystem = BlisterSubsystemState.getInstance();
			blistersystem.addObserver(this);
			sys = SystemState.getInstance();
			// tell the master the automaton is on
			send("A2:ON");
		} catch (UnknownHostException uhe) {
			System.err.println("[Blisters]: ERROR");
			uhe.printStackTrace();
		} catch (IOException ioe) {
			System.err.println("[Blisters]: ERROR");
			ioe.printStackTrace();
		} catch (SecurityException se) {
			System.err.println("[Blisters]: ERROR");
			se.printStackTrace();
		}
	}

	public void run_start(int speed, int belt_lg) {
		this.speed = (float) speed / (belt_lg * 7);
		this.belt_lg = belt_lg;
		stamper = new Stamper(blistersystem, sys, speed);
		(new Thread(stamper)).start();
		state = START;
		// send new state
		// send("A2:START");
		run_init();

	}

	public void run_init() {
		// start conveyor
		blistersystem.setConveyor_velocity(speed);
		state = INIT;
		// send new state
		send("A2:init");
		stamper.work();
		run_press();

	}

	public void run_press() {
		state = PRESS;
		// send new state
		send("A2:press");
	}

	public void run_cutting() {
		// blade down
		blistersystem.setCutter_secs((int) (60 / (speed * 10)));
		state = CUTTING;
		// send new state
		send("A2:cutting");
	}

	public void run_blister_ready() {
		// stop conveyor
		blistersystem.setConveyor_velocity(0);
		stamper.stop();
		state = BLISTER_READY;
		// send new state
		send("A2:blister_ready");

	}

	public void run_failure(String data) {
		String pars[] = data.split("#");
		this.belt_lg = Integer.parseInt(pars[2]);
		this.speed = Float.parseFloat(pars[1]) / (belt_lg * 7);
		if (pars[0].equalsIgnoreCase("INIT")) {
			run_init();
		} else if (pars[0].equalsIgnoreCase("PRESS")) {
			state = PRESS;
			blistersystem.setConveyor_velocity(speed);
			stamper.work();
		} else if (pars[0].equalsIgnoreCase("CUTTING")) {
			blistersystem.setConveyor_velocity(speed);
			stamper.work();
			run_cutting();
		} else if (pars[0].equalsIgnoreCase("BLISTER_READY")) {
			run_blister_ready();
		}

	}

	public void run_stop() {
		blistersystem.setConveyor_velocity(0);
		stamper.stop();
		state = START;
	}

	@Override
	public synchronized void newMsg(String msg) {
		while (treatingupdate) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {

			}
		}
		String[] content = msg.split(":");
		// Emergencies work for any state
		if (content[0].equals("EMERGENCY"))
			run_stop();
		else if (content[0].equalsIgnoreCase("STOP"))
			stop = true;
		else if (content[0].equalsIgnoreCase("RESTART")) {
			String pars[] = content[1].split("\\$");
			run_failure(pars[1]);
		} else
			switch (state) {
			case START:
				if (content[0].equalsIgnoreCase("init")) {
					String[] pars = content[1].split("\\#");
					run_start(Integer.parseInt(pars[0]), Integer
							.parseInt(pars[1]));
				}
				break;
			case INIT:
				break;
			case PRESS:
				break;
			case CUTTING:
				break;
			case BLISTER_READY:
				if (content[0].equalsIgnoreCase("R1")
						&& content[1].equalsIgnoreCase("blister")) {
					run_init();
				}
				break;
			case FAILURE:
				break;

			}

	}

	public void run() {
		switch (state) {
		case CUTTING:
			run_cutting();
			break;
		case BLISTER_READY:
			run_blister_ready();
			break;
		}
		treatingupdate = false;
	}

	@Override
	public void update(Observable o, Object arg) {
		treatingupdate = true;
		// which sensor?
		if (arg instanceof LightSensor) {
			if (((Sensor) arg).isActived()) {
				state = CUTTING;
				(new Thread(this)).start();
			} else
				treatingupdate = false;
			// else run_choc_car();

		} else if (arg instanceof TouchSensor) {
			if (((Sensor) arg).isActived() && state != BLISTER_READY) {
				state = BLISTER_READY;
				(new Thread(this)).start();
			} else
				treatingupdate = false;
			// else -> no action, blister's pickup is received as a message
		} else
			treatingupdate = false;
	}

	public static void main(String args[]) {
		BlisterAutomaton aut = new BlisterAutomaton(Integer.parseInt(args[0]),
				Integer.parseInt(args[1]), args[2]);
		while (true) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	//stop is not wrong because we want an "unclean", sudden stop
	@SuppressWarnings("deprecation")
	public void destroyAutomaton(){
		blistersystem.setConveyor_velocity(0);		
		mbox_thread.stop();
		stamper.stop();
	}

	private class Stamper implements Runnable {
		private boolean working;
		private float time;
		// simulation
		private BlisterSubsystemState blistersystem;
		private int freq;
		private SystemState sys;

		private Stamper(BlisterSubsystemState blisters, SystemState sys,
				int speed) {
			time = 0;
			blistersystem = blisters;
			this.sys = sys;
			freq = (int) (60 * 1000 * 3 / (speed));
			working = false;
		}

		private void work() {
			working = true;
		}

		private void stop() {
			working = false;
		}

		public void run() {
			while (true) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
				}
				if (working) {
					time = time + 500;
					if (time == freq) {
						blistersystem.setEngraver_secs(5);
						sys.setMakeBlister();
						time = 0;
					}
				}
			}
		}
	}
}