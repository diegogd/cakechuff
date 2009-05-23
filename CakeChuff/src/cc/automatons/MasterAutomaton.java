//TODO: recuperación de caídas +
//TODO: Localizar estados y enviárselos al SCADA para permitir la recuperación del master
//TODO: Errores simulados -> ajustar %, nº de tartas en el blister??
//TODO: Ajustar velocidades al entorno de ejecución -
//TODO: Recuperación desde parada de emergencia??

package cc.automatons;

import java.util.Observable;

import cc.communications.MasterMailbox;
import cc.simulation.state.Robot1State;

/**
 * Define the internal logic of the MasterAutomaton
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class MasterAutomaton extends Automaton {
	//Robot states
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
	// packet waiting for an empty quality control conveyor
	private boolean packet_waiting;
	// qc is available
	private boolean qc_free;
	//Simulated error
	private boolean mistake;

	/**
	 * Constructor
	 * Initialize the communication channels (mailboxes) from the MasterAutomaton 
	 * side with SCADA and the rest subsystems
	 * Initialize the control parameters
	 * @param scada Destination address for the SCADA MasterMailbox
	 * @param scada_in Input port for the SCADA MasterMailbox
	 * @param scada_out Output port the SCADA MasterMailbox
	 * @param cake Destination address for the Cake Subsystem MasterMailbox
	 * @param cake_in Input port for the Cake Subsystem MasterMailbox
	 * @param cake_out Output port for the Cake Subsystem MasterMailbox
	 * @param blister Destination address for the Blister Subsystem MasterMailbox
	 * @param blister_in Input port for the Blister Subsystem MasterMailbox
	 * @param blister_out Output port for the Blister Subsystem MasterMailbox
	 * @param qc Destination address for the Quality&Control Subsystem MasterMailbox
	 * @param qc_in Input port for the Quality&Control Subsystem MasterMailbox
	 * @param qc_out Output port for the Quality&Control Subsystem MasterMailbox
	 */
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

	/**
	 * Set robot start parameters
	 * @param movecaket Time needed to transport a cake by the robot
	 * @param moveblistert Time needed to transport a blister by the robot
	 * @param f_rate Failure rate in the cake's manufacture
	 */
	private void run_robot_start(float movecaket, float moveblistert, int f_rate) {
		this.movecaket = 3/movecaket;
		this.moveblistert = 13/moveblistert;
		robot.setRobot_velocity(moveblistert);
		f_chance=f_rate/4;
	}

	/**
	 * The robot has to take a blister
	 * Robot transition state to PICKUPBLISTER
	 * Set the robot speed to take a blister
	 */
	private void run_robot_blister() {
		System.out.println("[Master]: Robot1: Picking blister");
		// move robot arm to the conveyor
		robot.setRobot_velocity(moveblistert);
		robot.setGoToState(PICKUPBLISTER);
	}

	/**
	 * The robot has to take the 1st cake
	 * Master Automaton state: CAKE1
	 * Robot transition state to PICKUPCAKE
	 * Set the robot speed to take a cake
	 */
	private void run_robot_cake1() {
		state = CAKE1;
		robot.setRobot_velocity(movecaket);
		robot.setGoToState(PICKUPCAKE);
		System.out.println("[Master]:Picking cake 1");
		// put cake in the blister
		// tell the cake automaton
	}

	/**
	 * The robot has to take the 2nd cake
	 * Master Automaton state: CAKE2
	 * Robot transition state to PICKUPCAKE
	 * Set the robot speed to take a cake
	 */
	private void run_robot_cake2() {
		state = CAKE2;
		robot.setRobot_velocity(movecaket);
		robot.setGoToState(PICKUPCAKE);
		System.out.println("[Master]:Picking cake 2");
		// put cake in the blister
		// tell the cake automaton
	}

	/**
	 * The robot has to take the 3rd cake
	 * Master Automaton state: CAKE3
	 * Robot transition state to PICKUPCAKE
	 * Set the robot speed to take a cake
	 */
	private void run_robot_cake3() {
		state = CAKE3;
		robot.setRobot_velocity(movecaket);
		robot.setGoToState(PICKUPCAKE);
		System.out.println("[Master]:Picking cake 3");
	}

	/**
	 * The robot has to take the 4th (last) cake
	 * Master Automaton state: FULL
	 * Robot transition state to PICKUPCAKE
	 * Set the robot speed to take a cake
	 */
	private void run_robot_full() {
		state = FULL;
		robot.setRobot_velocity(movecaket);
		robot.setGoToState(PICKUPCAKE);
		System.out.println("[Master]:Picking last cake");
	}
	
	/**
	 * Restart of the robot
	 * Robot transition state to the previous which corresponds
	 * @param movecaket Time needed to transport a cake by the robot
	 * @param moveblistert Time needed to transport a blister by the robot
	 * @param f_rate Failure rate in the cake's manufacture
	 * @param state Previous state
	 */
	private void run_robot_start(float movecaket, float moveblistert, int f_rate,
			String state) {
		this.movecaket = 3/movecaket;
		this.moveblistert = 13/moveblistert;
		robot.setRobot_velocity(moveblistert);
		//A normal stop ends with the robot empty

		if(state.compareTo("init")==0 || state.compareTo("empty")==0){
			robot.setCurrentState(EMPTY);
		} else if (state.compareTo("blister")==0 ){
			robot.setCurrentState(BLISTER);
		}else if (state.compareTo("cake1")==0 ){
			robot.setCurrentState(CAKE1);
		}else if (state.compareTo("cake2")==0 ){
			robot.setCurrentState(CAKE2);
		}else if (state.compareTo("cake3")==0 ){
			robot.setCurrentState(CAKE3);
		}else if (state.compareTo("full")==0 ){
			robot.setCurrentState(FULL);
		}
		f_chance=f_rate/4;	
	}
	
	/**
	 * Control and synchronize the manufacture process in the different subsystems
	 * Notify the INIT, RESTART, STOP, EMERGENCY and RESET to the different subsystems
	 * @param msg Message employed for the synchronization
	 */
	@Override
	public synchronized void newMsg(String msg) {
		/*while (treatingupdate) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {

			}
		}*/
		System.out.println("[Master]: Received msg: "+msg);
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
					System.out.println("[Master]: Cake awaiting");
					// Error chance
					if ((Math.random() * 100) < f_chance) {
						System.out.println("[Master]: Simulated error");
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
			String[] pars = content[1].split("\\$");
			mboxCake.send("INIT:" + pars[0]);
			mboxBlister.send("INIT:" + pars[1]);
			mboxQC.send("INIT:" + pars[2]);
			String[] parsrob = pars[3].split("\\#");
			run_robot_start(Integer.parseInt(parsrob[0]), Integer
					.parseInt(parsrob[1]), Integer.parseInt(pars[2].split("\\#")[2]));
		} else if (content[0].equalsIgnoreCase("RESTART")) {
			
			String[] pars = content[1].split("\\$");
			System.out.println("Received recover information for " + pars[0]);
			if (pars[0].equalsIgnoreCase("A1")) {
				mboxCake.send("RESTART:" + pars[1]);
			} else if (pars[0].equalsIgnoreCase("A2")) {
				mboxBlister.send("RESTART:" + pars[1]);
			}else if (pars[0].equalsIgnoreCase("A3")) {
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
		}else if (content[0].equalsIgnoreCase("RESET")){
			String[] pars = content[1].split("\\$");
			mboxCake.send("RESET:"+ pars[0]);
			mboxBlister.send("RESET:"+pars[1]);
			mboxQC.send("RESET:"+ pars[2]);
			String[] parsrob = pars[3].split("\\#");
			run_robot_start(Integer.parseInt(parsrob[0]), Integer
					.parseInt(parsrob[1]), Integer.parseInt(pars[2].split("\\#")[2]), parsrob[2]);
			//Set to false for the next time
			stop = false;
		}
	}

	/**
	 * Transition state diagram for the manufacture process
	 * @param o RobotState
	 * @param arg 
	 */
	@Override
	public void update(Observable o, Object arg) {
		treatingupdate=true;
		if (o instanceof Robot1State && ! robot.getIfMoving() && ((Robot1State) o).isChanged_CS()) {
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
						if(mistake) state = CAKE2;
						else state = CAKE1;
					}else if(mistake) state = CAKE1;
				} else if (state == BLISTER) {
					if (cake_waiting) {
						System.out.println("[Master to Robot1]: BLISTER->CAKE1");
						robot.setRobot_velocity(movecaket);
						robot.setGoToState(PICKUPCAKE);
						if(mistake) state = CAKE2;
						else state = CAKE1;
					}else if(mistake) state = CAKE1;
				} else if (state == CAKE1) {
					if (cake_waiting) {
						System.out.println("[Master to Robot1]: CAKE1->CAKE2");
						robot.setRobot_velocity(movecaket);
						robot.setGoToState(PICKUPCAKE);
						if(mistake) state = CAKE3;
						else state = CAKE2;
					}else if(mistake) state = CAKE2;
				} else if (state == CAKE2) {
					if (cake_waiting) {
						System.out.println("R1: CAKE2->CAKE3");
						robot.setRobot_velocity(movecaket);
						robot.setGoToState(PICKUPCAKE);
						if(mistake) state = FULL;
						else state = CAKE3;
					}else if(mistake) state = CAKE3;
				} else if (state == CAKE3) {
					if (cake_waiting) {
						System.out.println("[Master to Robot1]: CAKE3->FULL");
						robot.setRobot_velocity(movecaket);
						robot.setGoToState(PICKUPCAKE);
						state = FULL;
					}else if(mistake){
						state = FULL;
						System.out.println("[Master to Robot1]: PICK PACKET");
						robot.setRobot_velocity(moveblistert);
						robot.setGoToState(PICKUPPACKET);
					}
				} else if (state == FULL ) {
					if(qc_free)
					System.out.println("[Master to Robot1]: PICK PACKET");
					robot.setRobot_velocity(moveblistert);
					robot.setGoToState(PICKUPPACKET);
				}else packet_waiting=true;
				mistake=false;
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
				mboxCake.send("R1:packet");
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

	/**
	 * Run the MasterAutomaton
	 */
	public void run() {
	};
	
	/**
	 * Stop the MasterAutomaton's communication channels (mailboxes)
	 * Deprecated stop method is employed for an "unclean" sudden stop
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void destroyAutomaton(){
		robot.deleteObserver(this);
		mboxBlister.mbox_thread.stop();
		mboxQC.mbox_thread.stop();
		mboxCake.mbox_thread.stop();
		mboxScada.mbox_thread.stop();
	}
	
	/**
	 * Execute the MaterAutomaton independently
	 * @param args The command line arguments
	 */
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
