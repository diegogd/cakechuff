//TODO: recuperaci�n de ca�das +
//TODO: Errores simulados -> mover la tarta pero saltarse un estado
//(Random en el cambio de estado tras el update del robot)
//TODO: Ajustar velocidades al entorno de ejecuci�n -


package cc.automatons;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Observable;

import cc.communications.MasterMailbox;
import cc.simulation.state.CakeSubsystemState;
import cc.simulation.state.Robot1State;

public class MasterAutomaton extends Automaton {
	/* state for robot (inherited) */
	private Robot1State robot;
	private boolean movingrobot;
	private final int SUBSYSTEM1 = 1;
	private final int SUBSYSTEM2 = 2;
	private final int SUBSYSTEM3 = 3;
	private final int TABLE = 4;
	private final int PICKUPCAKE = 5;
	private final int DROPINTABLE = 6;
	private final int PICKUPBLISTER = 7;
	private final int PICKUPPACKET = 9;
	private final int DROPINSUB3 = 10;


	private static final int START = 0;
	private static final int EMPTY = 1;
	private static final int BLISTER = 2;
	private static final int CAKE1 = 3;
	private static final int CAKE2 = 4;
	private static final int CAKE3 = 5;
	private static final int FULL = 6;
	private static final int FAILURE = 7;
	// parameters for the robot
	private float moveblistert, movecaket;
	private int f_chance;

	// Mailboxes
	private MasterMailbox mboxScada;
	private MasterMailbox mboxCake;
	private MasterMailbox mboxBlister;
	private MasterMailbox mboxQC;

	// the automatons are running (used to check restarts)
	private boolean cake_on, blister_on, qc_on;
	// a cake is waiting but there is no blister
	private boolean cake_waiting;
	// blister waiting for an empty table
	private boolean blister_waiting;
	private boolean packet_waiting;
	// qc is available
	private boolean qc_free;
	
	private long lastupdate;
	
	private boolean mistake;

	public MasterAutomaton(String scada, int scada_in, int scada_out,
			String cake, int cake_in, int cake_out, String blister,
			int blister_in, int blister_out, String qc, int qc_in, int qc_out) {
		state = EMPTY;

		// comm
		System.out.print("First mbox...");
		mboxScada = new MasterMailbox(this, scada_in, scada_out, scada);
		System.out.println("OK");
		System.out.print("2nd mbox...");
		mboxCake = new MasterMailbox(this, cake_in, cake_out, cake);
		System.out.println("OK");
		System.out.print("3rd mbox...");
		mboxBlister = new MasterMailbox(this, blister_in, blister_out, blister);
		System.out.println("OK");
		System.out.print("4th mbox...");
		mboxQC = new MasterMailbox(this, qc_in, qc_out, qc);
		System.out.println("OK");

		// subscribe
		robot = Robot1State.getInstance();
		robot.addObserver(this);

		//
		cake_on = false;
		blister_on = false;
		qc_on = false;
		qc_free = true;
		cake_waiting = false;
		blister_waiting = false;
		//f_chance=50;
	}

	private void run_robot_start(float movecaket, float moveblistert, int f_rate) {
		this.movecaket = 3/movecaket;
		this.moveblistert = 13/moveblistert;
		robot.setRobot_velocity(moveblistert);
		f_chance=f_rate;
	}

	private void run_robot_blister() {
		System.out.println("[Master]: Robot1: Picking blister");
		// move robot arm to the conveyor
		robot.setRobot_velocity(moveblistert);
		robot.setGoToState(PICKUPBLISTER);
	}

	private void run_robot_cake1() {
		state = CAKE1;
		if(!mistake){
			robot.setRobot_velocity(movecaket);
			robot.setGoToState(PICKUPCAKE);
		}else{
			robot.setRobot_velocity(movecaket);
			robot.setGoToState(SUBSYSTEM1);
			mistake=false;
		}
		System.out.println("[Master]:Picking cake 1");
		// put cake in the blister
		// tell the cake automaton
	}

	private void run_robot_cake2() {
		state = CAKE2;
		robot.setRobot_velocity(movecaket);
		robot.setGoToState(PICKUPCAKE);
		System.out.println("[Master]:Picking cake 2");
		// put cake in the blister
		// tell the cake automaton
	}

	private void run_robot_cake3() {
		state = CAKE3;
		robot.setRobot_velocity(movecaket);
		robot.setGoToState(PICKUPCAKE);
		System.out.println("[Master]:Picking cake 3");
	}

	private void run_robot_full() {
		state = FULL;
		robot.setRobot_velocity(movecaket);
		robot.setGoToState(PICKUPCAKE);
		System.out.println("[Master]:Picking last cake");
	}

	@Override
	public synchronized void newMsg(String msg) {
		/*while (treatingupdate) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {

			}
		}*/
		String[] content = msg.split(":");
		if (content[0].equalsIgnoreCase("A1")) {
			if (content[1].equalsIgnoreCase("ON")) {
				if (cake_on == false)
					cake_on = true;
				else
					mboxScada.send("RECOVER:A1"); // was already running so it
				// must be a new instance
			} else {
				// state change
				mboxScada.send(msg);
				if (content[1].equalsIgnoreCase("WAIT")) {
					System.out.println("Cake awaiting");
					// Error chance
					if ((Math.random() * 100) < f_chance) {
						System.out.println("Simulated error");
						mistake = true;
					}
					if(robot.getIfMoving())cake_waiting = true;
					else if (state == BLISTER) {
						run_robot_cake1();
					} else if (state == CAKE1) {
						run_robot_cake2();
					} else if (state == CAKE2) {
						run_robot_cake3();
					} else if (state == CAKE3) {
						run_robot_full();
					} else {
						// the cake must wait for a blister
						cake_waiting = true;
					}
				}
			}
		} else if (content[0].equalsIgnoreCase("A2")) {
			if (content[1].equalsIgnoreCase("ON")) {
				if (blister_on == false)
					blister_on = true;
				else
					mboxScada.send("RECOVER:A2"); // was already running so it
				// must be a new instance
			} else {
				// state change
				mboxScada.send(msg);
				if (content[1].equalsIgnoreCase("BLISTER_READY")) {
					if (state == EMPTY && stop == false &&!robot.getIfMoving()) {
						run_robot_blister();
					} else
						blister_waiting = true;
				}
			}
		} else if (content[0].equalsIgnoreCase("A3")) {
			if (content[1].equalsIgnoreCase("ON")) {
				if (qc_on == false) {
					qc_free = true;
					qc_on = true;
				} else
					mboxScada.send("RECOVER:A3"); // was already running so it
				// must be a new instance
			} else {
				// store if the third conveyor is free to put a new blister
				// it is free if it is just starting to run (first init) or if
				// it is boxing its package
				if (content[1].equalsIgnoreCase("KO_WAIT")
						|| content[1].equalsIgnoreCase("OK_WAIT"))
					qc_free = true;
				else if ((content[1].equalsIgnoreCase("QC")))
					qc_free = false;
				// state change
				mboxScada.send(msg);
				if (qc_free && packet_waiting) {
					System.out.println("[Master to Robot1]: Waiting packet -> QC");
					robot.setRobot_velocity(moveblistert);
					robot.setGoToState(PICKUPPACKET);
				}
			}
		} else if (content[0].equalsIgnoreCase("INIT")) {
			stop = false;
			System.out.println("Par2:" + content[1]);
			String[] pars = content[1].split("\\$");
			mboxCake.send("INIT:" + pars[0]);
			mboxBlister.send("INIT:" + pars[1]);
			mboxQC.send("INIT:" + pars[2]);
			String[] parsrob = pars[3].split("\\#");
			run_robot_start(Integer.parseInt(parsrob[0]), Integer
					.parseInt(parsrob[1]), Integer.parseInt(pars[2].split("\\#")[2]));
		} else if (content[0].equalsIgnoreCase("RESTART")) {
			String[] pars = content[1].split("\\$");
			if (pars[0].equalsIgnoreCase("A1")) {
				mboxCake.send("RESTART:" + pars[1]);
			} else if (pars[0].equalsIgnoreCase("A2")) {
				mboxBlister.send("RESTART:" + pars[1]);
			}
			if (pars[0].equalsIgnoreCase("A3")) {
				mboxQC.send("RESTART:" + pars[1]);
			}
		} else if (content[0].equalsIgnoreCase("STOP")) {
			mboxCake.send(msg);
			mboxBlister.send(msg);
			mboxQC.send(msg);
			stop = true;
		} else if (content[0].equalsIgnoreCase("EMERGENCY")) {
			mboxCake.send(msg);
			mboxBlister.send(msg);
			mboxQC.send(msg);
			robot.deleteObserver(this);
			robot.setMoving(false);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		treatingupdate=true;
		if (o instanceof Robot1State && ! robot.getIfMoving() && ((Robot1State) o).isChanged_CS()) {
			// robot.deleteObserver(this);
			System.out.println("[Master to Robot1]:Robot state changed");
			System.out.println("New Robot1 state:"+robot.getCurrentState());
			switch (robot.getCurrentState()) {
			case (SUBSYSTEM1):
				//Simulated error
				System.out.println("[Master to Robot1]: CAKE -> TABLE... ups!!");
				cake_waiting = false;
				mboxCake.send("R1:cake");
				break;
			case (PICKUPCAKE):
				System.out.println("[Master to Robot1]: CAKE -> TABLE");
				cake_waiting = false;
				robot.setRobot_velocity(movecaket);
				robot.setGoToState(DROPINTABLE);
				mboxCake.send("R1:cake");
				break;
			case (DROPINTABLE):
				if (state == EMPTY) {	
					state = BLISTER;
					if (cake_waiting) {
						System.out.println("[Master to Robot1]: EMPTY->BLISTER");
						robot.setRobot_velocity(movecaket);
						robot.setGoToState(PICKUPCAKE);
						state = CAKE1;
					}
				} else if (state == BLISTER) {
					if (cake_waiting) {
						System.out.println("[Master to Robot1]: BLISTER->CAKE1");
						robot.setRobot_velocity(movecaket);
						robot.setGoToState(PICKUPCAKE);
						state = CAKE1;
					} 
				} else if (state == CAKE1) {
					if (cake_waiting) {
						System.out.println("[Master to Robot1]: CAKE1->CAKE2");
						robot.setRobot_velocity(movecaket);
						robot.setGoToState(PICKUPCAKE);
						state = CAKE2;
					} 
				} else if (state == CAKE2) {
					if (cake_waiting) {
						System.out.println("R1: CAKE2->CAKE3");
						robot.setRobot_velocity(movecaket);
						robot.setGoToState(PICKUPCAKE);
						state = CAKE3;
					} 
				} else if (state == CAKE3) {
					if (cake_waiting) {
						System.out.println("[Master to Robot1]: CAKE3->FULL");
						robot.setRobot_velocity(movecaket);
						robot.setGoToState(PICKUPCAKE);
						state = FULL;
					} 
				} else if (state == FULL ) {
					if(qc_free)
					System.out.println("[Master to Robot1]: PICK PACKET");
					robot.setRobot_velocity(moveblistert);
					robot.setGoToState(PICKUPPACKET);
				}else packet_waiting=true;
				break;
			case (PICKUPBLISTER):
				System.out.println("[Master to Robot1]: BLISTER -> TABLE");
				state=BLISTER;
				robot.setRobot_velocity(moveblistert);
				robot.setGoToState(DROPINTABLE);
				mboxBlister.send("R1:blister");
				break;
			case (PICKUPPACKET):
				packet_waiting=false;
				System.out.println("[Master to Robot1]: BLISTER -> QC");
				robot.setRobot_velocity(moveblistert);
				robot.setGoToState(DROPINSUB3);
				break;
			case (DROPINSUB3):
				System.out.println("[Master to Robot1]: PACK LEFT");
				mboxQC.send("R1:empty");
				state = EMPTY;
				System.out.println("\n\n------------[Robot1]:Pack Done-----------------\n\n");
				if (blister_waiting && !stop) {
					System.out.println("[Master to Robot1]: PICK AWAITING BLISTER");
					state=BLISTER;
					robot.setRobot_velocity(moveblistert);
					robot.setGoToState(PICKUPBLISTER);
				} 
				break;
			}

		}
		treatingupdate=false;
	}

	public void run() {
	};
	
	@Override
	public void destroyAutomaton(){
		mboxBlister.mbox_thread.stop();
		mboxQC.mbox_thread.stop();
		mboxCake.mbox_thread.stop();
		mboxScada.mbox_thread.stop();
	}

	public static void main(String args[]) {
		/*
		 * String scada, int scada_in, int scada_out, String cake, int cake_in,
		 * int cake_out, String blister, int blister_in, int blister_out, String
		 * qc, int qc_in, int qc_out
		 */
		try {
			MasterAutomaton aut = new MasterAutomaton(args[0], Integer
					.parseInt(args[1]), Integer.parseInt(args[2]), args[3],
					Integer.parseInt(args[4]), Integer.parseInt(args[5]),
					args[6], Integer.parseInt(args[7]), Integer
							.parseInt(args[8]), args[9], Integer
							.parseInt(args[10]), Integer.parseInt(args[11]));
			while (true) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			;
		}
	}
}
