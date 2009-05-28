
package cc.automatons;

import java.util.Observable;

import cc.communications.MasterMailbox;
import cc.simulation.elements.Robot;
import cc.simulation.state.Robot1State;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Define the internal logic of the MasterAutomaton
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class MasterAutomaton extends Automaton {
	private static Logger logger = Logger.getLogger(MasterAutomaton.class.getName());
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
	 * Initialize the communication channels (mailboxes) 
	 * side with SCADA and the rest subsystems
	 * Initialize the control parameters
	 * @param scada Destination address of the SCADA MasterMailbox
	 * @param scada_in Input port of the SCADA MasterMailbox
	 * @param scada_out Output port of the SCADA MasterMailbox
	 * @param cake Destination address of the Cake Subsystem MasterMailbox
	 * @param cake_in Input port of the Cake Subsystem MasterMailbox
	 * @param cake_out Output port of the Cake Subsystem MasterMailbox
	 * @param blister Destination address of the Blister Subsystem MasterMailbox
	 * @param blister_in Input port of the Blister Subsystem MasterMailbox
	 * @param blister_out Output port of the Blister Subsystem MasterMailbox
	 * @param qc Destination address of the Quality&Control Subsystem MasterMailbox
	 * @param qc_in Input port of the Quality&Control Subsystem MasterMailbox
	 * @param qc_out Output port of the Quality&Control Subsystem MasterMailbox
	 */
	public MasterAutomaton(String scada, int scada_in, int scada_out,
			String cake, int cake_in, int cake_out, String blister,
			int blister_in, int blister_out, String qc, int qc_in, int qc_out) {
		state = EMPTY;

		// comm
		logger.finest("First mbox...");
		mboxScada = new MasterMailbox(this, scada_in, scada_out, scada);
		logger.finest("2nd mbox...");
		mboxCake = new MasterMailbox(this, cake_in, cake_out, cake);
		logger.finest("3rd mbox...");
		mboxBlister = new MasterMailbox(this, blister_in, blister_out, blister);
		logger.finest("4th mbox...");
		mboxQC = new MasterMailbox(this, qc_in, qc_out, qc);

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
	 * Set the robot start parameters
	 * @param movecaket Time needed to transport a cake by the robot
	 * @param moveblistert Time needed to transport a blister by the robot
	 * @param f_rate Failure rate in the cake's manufacture
	 */
	private void run_robot_start(float movecaket, float moveblistert, int f_rate) {
		this.movecaket = 3/movecaket;
		this.moveblistert = 13/moveblistert;
		robot.setRobot_velocity(moveblistert);
		f_chance=f_rate;
		mboxScada.send("R1:empty");
	}

	/**
	 * The robot has to take a blister
	 * Robot transition state to PICKUPBLISTER
	 * Set the robot speed to take a blister
	 */
	private void run_robot_blister() {
		logger.fine("[Master]: Robot1: Picking blister");
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
		mboxScada.send("R1:cake1");
		robot.setRobot_velocity(movecaket);
		robot.setGoToState(PICKUPCAKE);
		logger.fine("[Master]:Picking cake 1");
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
		mboxScada.send("R1:cake2");
		robot.setRobot_velocity(movecaket);
		robot.setGoToState(PICKUPCAKE);
		logger.fine("[Master]:Picking cake 2");
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
		mboxScada.send("R1:cake3");
		robot.setRobot_velocity(movecaket);
		robot.setGoToState(PICKUPCAKE);
		logger.fine("[Master]:Picking cake 3");
	}

	/**
	 * The robot has to take the 4th (last) cake
	 * Master Automaton state: FULL
	 * Robot transition state to PICKUPCAKE
	 * Set the robot speed to take a cake
	 */
	private void run_robot_full() {
		state = FULL;
		mboxScada.send("R1:full");
		robot.setRobot_velocity(movecaket);
		robot.setGoToState(PICKUPCAKE);
		logger.fine("[Master]:Picking last cake");
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
		stop=false;
		f_chance=f_rate;
		robot.setRobot_velocity(moveblistert);
		//A normal stop ends with the robot empty
		if(robot.getCurrentState()!=robot.getGoToState()) 
			robot.setMoving(true);
	}
	
	/**
	 * Control and synchronize the manufacture process in the different subsystems
	 * Notify the INIT, RESTART, STOP, EMERGENCY and RESET to the different subsystems
	 * @param msg Message employed for the synchronization
	 */
	@Override
	public synchronized void newMsg(String msg) {
		logger.fine("[Master]: Received msg: "+msg);
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
					logger.fine("[Master]: Cake awaiting");
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
						|| content[1].equalsIgnoreCase("OK_WAIT")){
					qc_free = true;
					logger.fine("[Master]: QC is free, good!");
				}
					
				// state change
				mboxScada.send(msg);
				if (qc_free && packet_waiting) {
					logger.fine("[Master to Robot1]: Waiting packet -> QC");
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
			logger.fine("Received recover information for " + pars[0]);
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
			//robot.deleteObserver(this);
			robot.setMoving(false);
		}else if (content[0].equalsIgnoreCase("RESET")){
			stop = false;
			String[] pars = content[1].split("\\$");
			
			String[] parsrob = pars[3].split("\\#");
			run_robot_start(Integer.parseInt(parsrob[1]), Integer
					.parseInt(parsrob[2]), Integer.parseInt(pars[2].split("\\#")[4]), parsrob[0]);
			mboxCake.send("RESET:"+ pars[0]);
			mboxBlister.send("RESET:"+pars[1]);
			mboxQC.send("RESET:"+ pars[2]);
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
			logger.fine("[Master to Robot1]:Robot state changed");
			logger.fine("New Robot1 state:"+robot.getCurrentState());
			switch (robot.getCurrentState()) {
			case (PICKUPCAKE):
				logger.fine("[Master to Robot1]: CAKE -> TABLE");
				cake_waiting = false;
				robot.setRobot_velocity(movecaket);
				robot.setGoToState(DROPINTABLE);
				if(state==FULL)mboxCake.send("R1:fullblister");
				else mboxCake.send("R1:cake");
				
				break;
			case (DROPINTABLE):
				if (state == BLISTER) {
					// Error chance
					double error = Math.random();
					logger.fine("[Master]: Error chance : " + error + "<" +f_chance+"??");
					
					if ((error) < (Math.pow(f_chance, 4) / 100000000)) {
						logger.fine("[Master]: Sim. error, skipping all cakes");
						state = FULL;
						mboxScada.send("R1:full");
						if (qc_free) {
							robot.setRobot_velocity(moveblistert);
							robot.setGoToState(PICKUPPACKET);
						} else
							packet_waiting = true;
					} else if ((error) < (Math.pow(f_chance, 3) / 1000000)) {
						if (cake_waiting) {
							logger.fine("[Master to Robot1]: Sim. ERROR :BLISTER->CAKE4");
							robot.setRobot_velocity(movecaket);
							robot.setGoToState(PICKUPCAKE);
							state = FULL;
							mboxScada.send("R1:full");
						} else{
							state = CAKE3;
							mboxScada.send("R1:cake3");
						}
					} else if (error < ((f_chance * f_chance) / 10000d)) {
						if (cake_waiting) {
							logger.fine("[Master to Robot1]: Sim. ERROR :BLISTER->CAKE3");
							robot.setRobot_velocity(movecaket);
							robot.setGoToState(PICKUPCAKE);
							state = CAKE3;
							mboxScada.send("R1:cake3");
						} else{
							state = CAKE2;
							mboxScada.send("R1:cake2");
						}
					} else if (error < (f_chance / 100d)) {
						if (cake_waiting) {
							logger.fine("[Master to Robot1]: Sim. ERROR : BLISTER->CAKE2");
							robot.setRobot_velocity(movecaket);
							robot.setGoToState(PICKUPCAKE);
							state = CAKE2;
							mboxScada.send("R1:cake2");
						} else{
							state = CAKE1;
							mboxScada.send("R1:cake1");
						}
					} else {
						if (cake_waiting) {
							logger.fine("[Master to Robot1]: BLISTER->CAKE1");
							robot.setRobot_velocity(movecaket);
							robot.setGoToState(PICKUPCAKE);
							state = CAKE1;
							mboxScada.send("R1:cake1");
						}
					}
				} else if (state == CAKE1) {
					if (cake_waiting) {
						logger.fine("[Master to Robot1]: CAKE1->CAKE2");
						robot.setRobot_velocity(movecaket);
						robot.setGoToState(PICKUPCAKE);
						state = CAKE2;
						mboxScada.send("R1:cake2");
					}
				} else if (state == CAKE2) {
					if (cake_waiting) {
						logger.fine("R1: CAKE2->CAKE3");
						robot.setRobot_velocity(movecaket);
						robot.setGoToState(PICKUPCAKE);
						state = CAKE3;
						mboxScada.send("R1:cake3");
					}
				} else if (state == CAKE3) {
					if (cake_waiting) {
						logger.fine("[Master to Robot1]: CAKE3->FULL");
						robot.setRobot_velocity(movecaket);
						robot.setGoToState(PICKUPCAKE);
						state = FULL;
						mboxScada.send("R1:full");
					}
				} else if (state == FULL) {
					if (qc_free){
						logger.fine("[Master to Robot1]: PICK PACKET");
						robot.setRobot_velocity(moveblistert);
						robot.setGoToState(PICKUPPACKET);
					} else{
						logger.fine("[Master]: QC is not available, waiting...");
						packet_waiting = true;
					}
				}
				mistake = false;
				break;
			case (PICKUPBLISTER):
				blister_waiting=false;
				logger.fine("[Master to Robot1]: BLISTER -> TABLE");
				state=BLISTER;
				mboxScada.send("R1:blister");
				robot.setRobot_velocity(moveblistert);
				robot.setGoToState(DROPINTABLE);
				mboxBlister.send("R1:blister");
				break;
			case (PICKUPPACKET):
				packet_waiting=false;
				logger.fine("[Master to Robot1]: BLISTER -> QC");
				robot.setRobot_velocity(moveblistert);
				robot.setGoToState(DROPINSUB3);
				break;
			case (DROPINSUB3):
				logger.fine("[Master to Robot1]: PACK LEFT");
				mboxQC.send("R1:empty");
				state = EMPTY;
				mboxScada.send("R1:empty");
				qc_free=false;
				logger.fine("\n\n------------[Robot1]:Pack Done-----------------\n\n");
				if (blister_waiting && !stop) {
					logger.fine("[Master to Robot1]: PICK AWAITING BLISTER");
					//state=BLISTER;
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
			logger.severe(e.toString());
		}
	}
}
