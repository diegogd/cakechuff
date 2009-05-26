//TODO: REcuperacion: REconocer si el sensor de final sigue activo para arrancar el movimiento
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

/**
 * Define the internal logic of the BlisterAutomaton
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class BlisterAutomaton extends Automaton {

	private static final int START = 0;
	private static final int INIT = 1;
	private static final int PRESS = 2;
	private static final int CUTTING = 3;
	private static final int BLISTER_READY = 4;
	private static final int FAILURE = 5;
	private Thread changingstate;
	// param
	private int belt_lg;
	private float speed;

	// simulation
	private BlisterSubsystemState blistersystem;
	private SystemState sys;
	private Stamper stamper;
	private Thread stamperthread;

	/**
	 * Constructor
	 * Initialize the communication channel (mailbox) with the MasterAutomaton 
	 * @param portin Input port of the Mailbox with the MasterAutomaton
	 * @param portout Output port of the Mailbox with the MasterAutomaton
	 * @param master Destination address of the Mailbox with the MasterAutomaton
	 * @exception UnknownHostException Communication error
	 * @exception IOException Communication error
	 * @exception SecurityException Security error
	 */
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
			
			//Prepare the stamper
			stamper = new Stamper(blistersystem, sys, this.speed);
			stamperthread= new Thread(stamper);
			stamperthread.start();
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

	/**
	 * Set the BlisterAutomaton start parameters
	 * @param speed	Conveyor belt speed
	 * @param belt_lg Conveyor belt length
	 */
	public void run_start(float speed, int belt_lg) {
		this.speed = speed / (belt_lg * 7);
		this.belt_lg = belt_lg;
		stamper.setFreq(1000 *4/ this.speed);
		state = START;
		// send new state
		// send("A2:START");
		run_init();
	}

	/**
	 * The conveyor belt has to move
	 * Blister Automaton state: INIT
	 * Set the conveyor belt speed
	 * set the stamper working
	 */
	public void run_init() {
		// start conveyor
		blistersystem.setConveyor_velocity(speed);
		state = INIT;
		// send new state
		send("A2:init");
		stamper.work();
		run_press();

	}

	/**
	 * The stamper makes the holes in the blister
	 * Blister Automaton state: PRESS
	 */
	public void run_press() {
		state = PRESS;
		// send new state
		send("A2:press");
	}

	/**
	 * The blade cuts the blister
	 * Blister Automaton state: CUTTING
	 * Set the blade cutting time
	 */
	public void run_cutting() {
		blistersystem.setConveyor_velocity(0);
		// blade down
		blistersystem.setCutter_secs((int) (80 / (speed)));
		stamper.stop();
		try {
			Thread.sleep((int) (2*1000 / (speed)));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		stamper.work();
		state = CUTTING;
		// send new state
		send("A2:cutting");
		if(!blistersystem.isTouchSensorActived()) blistersystem.setConveyor_velocity(speed);
	}

	/**
	 * The blister has to be taken by the robot because it has reached the end of the conveyor belt
	 * Blister Automaton state: BLISTER_READY
	 * Stop the conveyor belt
	 */
	public void run_blister_ready() {
		// stop conveyor
		blistersystem.setConveyor_velocity(0);
		stamper.stop();
		state = BLISTER_READY;
		// send new state
		send("A2:blister_ready");

	}

	/**
	 * Recover from a stop
	 */
	public void run_reset(String data) {
		String pars[] = data.split("#");
		this.belt_lg = Integer.parseInt(pars[2]);
		this.speed = Float.parseFloat(pars[1]) / (belt_lg * 7);
		stamper.setFreq(1000*4/speed);
		if (pars[0].equalsIgnoreCase("INIT")) {
			run_init();
		} else if (pars[0].equalsIgnoreCase("PRESS")) {
			state = PRESS;
			blistersystem.setConveyor_velocity(speed);
			stamper.work();
		} else if (pars[0].equalsIgnoreCase("CUTTING")) {
			//blistersystem.setConveyor_velocity(speed);
			stamper.work();
			state=CUTTING;
			changingstate=new Thread(this);
			changingstate.run();
			//run_cutting();
		} else if (pars[0].equalsIgnoreCase("BLISTER_READY")) {
			run_blister_ready();
		}

	}
	/**
	 * Recover from a failure
	 */
	public void run_failure(String data) {
		String pars[] = data.split("#");
		this.belt_lg = Integer.parseInt(pars[2]);
		this.speed = Float.parseFloat(pars[1]) / (belt_lg * 7);
		stamper.setFreq(1000*4/speed);
		if (pars[0].equalsIgnoreCase("INIT")) {
			run_init();
		} else if (pars[0].equalsIgnoreCase("PRESS")) {
			state = PRESS;
			blistersystem.setConveyor_velocity(speed);
			stamper.work();
		} else if (pars[0].equalsIgnoreCase("CUTTING")) {
			//blistersystem.setConveyor_velocity(speed);
			stamper.work();
			state=CUTTING;
			changingstate=new Thread(this);
			changingstate.run();
			//run_cutting();
		} else if (pars[0].equalsIgnoreCase("BLISTER_READY")) {
			//check sensor
			if (blistersystem.isTouchSensorActived())
				run_blister_ready();
			else{ //The robot has picked the blister while the automaton was off
				blistersystem.setConveyor_velocity(speed);
			}
				
		}

	}
	/**
	 * Stop the subsystem
	 */
	public void run_stop() {
		if(changingstate!=null) changingstate.stop();
		blistersystem.setConveyor_velocity(0);
		stamper.stop();
		blistersystem.setCutter_secs(0);
		//state = START;
	}

	/**
	 * Manage the control and synchronize messages received from the MasterAutomaton
	 * @param msg Message received from the MasterAutomaton
	 */
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
		else if (content[0].equalsIgnoreCase("RESET")) 
			run_reset(content[1]);
		else if (content[0].equalsIgnoreCase("RESTART")) {
			//String pars[] = content[1].split("\\$");
			run_failure(content[1]);
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

	/**
	 * Run the BlisterAutomaton
	 */
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

	/**
	 * Produce state transition depending on the signals received from the sensors
	 * @param arg Sensor in the conveyor belt which detects the position of the blister
	 */
	@Override
	public void update(Observable o, Object arg) {
		treatingupdate = true;
		// which sensor?
		if (arg instanceof LightSensor) {
			if (((Sensor) arg).isActived()) {
				state = CUTTING;
				changingstate=new Thread(this);
				changingstate.start();
			} else
				treatingupdate = false;
			// else run_choc_car();

		} else if (arg instanceof TouchSensor) {
			if (((Sensor) arg).isActived() && state != BLISTER_READY) {
				state = BLISTER_READY;
				changingstate=new Thread(this);
				changingstate.start();
			} else
				treatingupdate = false;
			// else -> no action, blister's pickup is received as a message
		} else
			treatingupdate = false;
	}

	/**
	 * Execute the BlisterAutomaton independently
	 * @param args The command line arguments
	 */
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
	
	/**
	 * Stop the BlisterAutomaton's communication channel (mailbox)
	 * Deprecated stop method is employed for an "unclean" sudden stop
	 */
	@SuppressWarnings("deprecation")
	public void destroyAutomaton(){
		blistersystem.setConveyor_velocity(0);
		if(changingstate!=null) changingstate.stop();
		mbox_thread.stop();
		mbox.end();
		mbox.end();
		try {
			sout.close();
			dout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		blistersystem.deleteObserver(this);
		stamper.stop();
		
	}

	/**
	 * Define the internal logic of the Samper
	 * @version 1.0, 29/05/09
	 * @author CaKeChuff team
	 */
	private class Stamper implements Runnable {
		private boolean working;
		private float time;
		// simulation
		private BlisterSubsystemState blistersystem;
		private float freq;
		private SystemState sys;

		/**
		 * Constructor 
		 * Set the Stamper parameters
		 * @param blisters Number of blisters
		 * @param sys State of the system
		 * @param speed Speed of the conveyor belt
		 */
		private Stamper(BlisterSubsystemState blisters, SystemState sys,
				float speed) {
			time = 0;
			blistersystem = blisters;
			this.sys = sys;
			freq =  (1000 *4/ (speed));
			working = false;
		}

		public void setFreq(float freq) {
			System.out.println("[STAMPER]: Nueva Velocidad: " +freq);
			this.freq = freq;
		}

		/**
		 * Set to work
		 */
		private void work() {
			working = true;
		}

		/** 
		 * Set to stop
		 */
		private void stop() {
			working = false;
		}

		/**
		 * Run the stamper
		 */
		public void run() {
			while (true) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
				}
				if (working) {
					time = time + 500;
					if (time >= freq) {
						blistersystem.setEngraver_secs(5);
						sys.setMakeBlister();
						time = 0;
					}
				}
			}
		}
	}
}