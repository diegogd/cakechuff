//TODO: Cinta que no arranca (waitingcake bloqueando?) +++
//TODO: Parada sin lanzar más tartas de la cuenta --
//TODO: Estados dobles (esperando y chocolate, etc) -> booleanos para retrasar acciones, 
// no permitiendo más de una a la vez(lento) +


package cc.automatons;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import cc.communications.Mailbox;
import cc.simulation.state.CakeSubsystemState;
import cc.simulation.state.SystemState;

import cc.simulation.elements.Sensor;

/**
 * Define the internal logic of the CakeAutomaton
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class CakeAutomaton extends Automaton {
	
	Thread changingstate;
	//states
	private static final int START=0;
	private static final int INIT=1;
	private static final int CHOC=2;
	private static final int CHOC_CAR=3;
	private static final int CAR=4;
	private static final int CAR_WAIT=5;
	private static final int WAIT=6;
	private static final int FAILURE=7;
	
	//parameters
	private int belt_lg, cake_cap, vt1, vt2;
	private float speed;
	private int ncakes;
	private boolean waitingcake;
	int blistercakes;
	//simulation
	private CakeSubsystemState cakesystem;
	private SystemState sys;
	
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
	public CakeAutomaton(int portin, int portout, String master){
		System.out.println("[CakeAutomaton]:Creating...");
		state=START;
		stop=false;
		try{
			mbox= new Mailbox(this, portin);
			mbox_thread = (new Thread(mbox));
			mbox_thread.start();
			boolean connected=false;
			while(!connected){
				try{
					sout= new Socket(master, portout);
					connected=true;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			dout = new PrintWriter(sout.getOutputStream(),true);
			//subscribe
			cakesystem = CakeSubsystemState.getInstance();
			cakesystem.addObserver(this);
			sys=SystemState.getInstance();
			ncakes=cake_cap;
			//tell the master the automaton is on
			send("A1:ON");
			waitingcake=false;
			System.out.println("[CakeAutomaton]:Active");
		}catch(UnknownHostException uhe){
			
		}catch(IOException ioe){
			
		}catch(SecurityException se){
			
		}
		System.out.println("[CakeAutomaton]:Creation finished");
		
	}
	
	/**
	 * Set the CakeAutomaton start parameters
	 * @param cake_cap Number of cakes (capacity)
	 * @param speed	Conveyor belt speed
	 * @param belt_lg Conveyor belt length
	 * @param vt1 Caramel valve activation time
	 * @param vt2 Chocolate valve activation time
	 */
	private void run_start(int cake_cap, int speed, int belt_lg, int vt1, int vt2){
		System.out.println("Initializating cake automaton...");
		this.speed = (float)speed/(belt_lg*3);
		this.belt_lg = belt_lg;
		this.cake_cap = cake_cap;
		this.ncakes=cake_cap;
		this.vt1=vt1;
		this.vt2 = vt2;
		state=START;
		sys.setDropCake();
		blistercakes=1;
		ncakes--;
		if(!waitingcake)
			run_init();
		else run_wait();
		
	}
	
	/**
	 * The conveyor belt has to move
	 * Cake Automaton state: INIT
	 * Set the conveyor belt speed
	 */
	private void run_init(){
		//start conveyor
		cakesystem.setConveyor_velocity(speed);
		state= INIT;
		send("A1:init");
	}
	
	/**
	 * The chocolate valve has to be activated
	 * Cake Automaton state: CHOC
	 * Stop the conveyor belt
	 * Open the chocolate valve
	 * Close the chocolate valve
	 * Restart the conveyor belt
	 * @exception InterruptedException Thread error
	 */
	private void run_choc(){
		state=CHOC;
		send("A1:choc");
		//stop conveyor
		cakesystem.setConveyor_velocity(0);
		//open chocolate valve
		cakesystem.setValve1_open_secs(vt1);
		
		try{
			Thread.sleep(vt1*1000);
		}catch(InterruptedException ie){
			//Interrupted
		}
		//close chocolate valve ¿?
		cakesystem.setValve1_open_secs(0);
		//run conveyor
		run_choc_car();	
	}
	
	/**
	 * The cake is between the two valves
	 * Cake Automaton state: CHOC_CAR
	 * Retain the cake until the end of the conveyor belt is free
	 */
	private void run_choc_car(){
		state=CHOC_CAR;
		send("A1:choc_car");
		if(!waitingcake)cakesystem.setConveyor_velocity(speed);
		/*There are cakes left
		 * &
		 * if the automaton is going to stop, the number of cakes dropped for this blister mist be 4
		 * to fill it.
		 */		
		if(ncakes>0 && (!stop||blistercakes<4)){
			sys.setDropCake();
			ncakes--;
			blistercakes++;
		}	
	}
	
	/**
	 * The caramel valve has to be activated
	 * Cake Automaton state: CHOC
	 * Stop the conveyor belt
	 * Open the caramel valve
	 * Close the caramel valve
	 * Restart the conveyor belt
	 * @exception InterruptedException Thread error
	 */
	private void run_car(){
		state=CHOC;
		send("A1:car");
		//stop conveyor
		cakesystem.setConveyor_velocity(0);
		//open caramel valve
		cakesystem.setValve2_open_secs(vt2);		
		try{
			Thread.sleep(vt2*1000);
		}catch(InterruptedException ie){}
		//close caramel valve
		cakesystem.setValve2_open_secs(0);
		//run conveyor
		run_car_wait();
		
	}
	
	/**
	 * Another cake in the conveyor belt has to be taken by the robot
	 */
	private void run_car_wait(){
		state=CAR_WAIT;
		send("A1:car_wait");
		if(!waitingcake)cakesystem.setConveyor_velocity(speed);
		
	}
	
	/**
	 * The cake has to be taken by the robot beacuse it has rechaed the end of the
	 * conveyor belt
	 * Stop the conveyor belt
	 */
	private void run_wait(){
		state=WAIT;
		send("A1:wait");
		cakesystem.setConveyor_velocity(0);
		waitingcake=true;
	}
	
	/**
	 * Recover from a failure
	 */
	private void run_failure(String data){
		System.out.println("[CakeAutomaton]:Restoring");
		String pars[]=data.split("#");
		
		//Reload parameters
		this.cake_cap = Integer.parseInt(pars[1]);
		
		this.belt_lg = Integer.parseInt(pars[3]);
		this.speed = Float.parseFloat(pars[2])/(belt_lg*3);
		this.ncakes=cake_cap;
		this.vt1=Integer.parseInt(pars[4]);
		this.vt2 =Integer.parseInt(pars[5]);
		
		//Recover state
		if(pars[0].equalsIgnoreCase("INIT")){
			run_init();
		}else if(pars[0].equalsIgnoreCase("CHOC")){
			state=CHOC;
			changingstate=new Thread(this);
			changingstate.start();
		}else if(pars[0].equalsIgnoreCase("CHOC_CAR")){
			state=CHOC_CAR;
			changingstate=new Thread(this);
			changingstate.start();
		}else if(pars[0].equalsIgnoreCase("CAR")){
			state=CAR;
			changingstate=new Thread(this);
			changingstate.start();
		}else if(pars[0].equalsIgnoreCase("CAR_WAIT")){
			run_car_wait();
		}else if(pars[0].equalsIgnoreCase("WAIT")){
			run_wait();
		}
		
	}
	
	/**
	 * Restart after a stop
	 */
	private void run_stop(){
		cakesystem.setConveyor_velocity(0);
		cakesystem.setValve1_open_secs(0);
		cakesystem.setValve2_open_secs(0);
		state=START;
	}
	
	/**
	 * Manage the control and synchronize messages received from the MasterAutomaton
	 * @param msg Message received from the MasterAutomaton
	 */
	@Override
	public synchronized void newMsg(String msg) {
		/*while (treatingupdate){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {

			}
		}*/
		System.out.println("[CakeAutomaton]: Received msg: "+msg);
		String[] content = msg.split(":");
		// Emergencies work for any state
		if (content[0].equals("EMERGENCY"))
			run_stop();
		else if (content[0].equalsIgnoreCase("STOP"))
			stop = true;
		else if (content[0].equalsIgnoreCase("RESET")) 
			run_failure(content[1]);
		else if (content[0].equalsIgnoreCase("RESTART")) {
			String pars[] = content[1].split("\\$");
			run_failure(pars[1]);
		} else if (content[0].equalsIgnoreCase("INIT")) {
			String[] pars = content[1].split("\\#");
			run_start(Integer.parseInt(pars[0]), Integer
					.parseInt(pars[1]), Integer.parseInt(pars[2]),
					Integer.parseInt(pars[3]), Integer
							.parseInt(pars[4]));

		} else if (content[0].equalsIgnoreCase("R1")){
			waitingcake=false;
			if (content[1].equalsIgnoreCase("cake")){
				System.out.println("[CakeAutomaton]: Cake taken.");
				run_init();
			}
			else if (content[1].equalsIgnoreCase("blister"))
				blistercakes=0;
			
		}
			/*switch (state) {
			case START:
				if (content[0].equalsIgnoreCase("INIT")) {
					String[] pars = content[1].split("\\#");
					run_start(Integer.parseInt(pars[0]), Integer
							.parseInt(pars[1]), Integer.parseInt(pars[2]),
							Integer.parseInt(pars[3]), Integer
									.parseInt(pars[4]));
				}
				break;
			case INIT:
				break;
			case CHOC:
				break;
			case CHOC_CAR:
				break;
			case CAR:
				break;
			case CAR_WAIT:
				break;
			case WAIT:
				if (content[0].equalsIgnoreCase("R1")){
						System.out.println("[CakeAutomaton]: Cake taken.");
					run_init();
				}
				break;
			case FAILURE:
				break;
			}*/
	}

	/**
	 * Produce state transition depending on the signals received from the sensors
	 * @param arg1 Sensor in the conveyor belt which detects the position of the cake
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		treatingupdate=true;
		// which sensor?
		if (arg1 instanceof Sensor) {
			if (((Sensor) arg1).getName().equalsIgnoreCase("sensor1")) {
				if (((Sensor) arg1).isActived()){
					state=CHOC;
					changingstate=new Thread(this);
					changingstate.start();
				}
				/*else{
					state = CHOC_CAR;
					(new Thread(this)).start();
				}*/
					
			} else if (((Sensor) arg1).getName().equalsIgnoreCase("sensor2")) {
				if (((Sensor) arg1).isActived()){
					state=CAR;
					changingstate=new Thread(this);
					changingstate.start();
				}
				/*else
					run_car_wait();*/
			} else if (((Sensor) arg1).getName().equalsIgnoreCase("sensor3")) {
				
				if (((Sensor) arg1).isActived() && state!=WAIT){
					state=WAIT;
					changingstate=new Thread(this);
					changingstate.start();
				}
				// else -> no action, cake's pickup is received as a message
			}
			treatingupdate=false;;
		}
	}
	
	/**
	 * Run the CakeAutomaton
	 */
	public void run(){
		switch(state){
		case START:break;
		case INIT: break;
		case CHOC: run_choc();
				break;
		case CHOC_CAR: run_choc_car();
				break;
		case CAR:run_car();
				break;
		case CAR_WAIT:run_car_wait();
				break;
		case WAIT:run_wait();
		break;
		}
	}
	
	/**
	 * Execute the CakeAutomaton independently
	 * @param args The command line arguments
	 */
	public static void main(String args[]){
		CakeAutomaton aut=new CakeAutomaton(Integer.parseInt(args[0]),Integer.parseInt(args[1]),args[2]);
		
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Stop the CakeAutomaton's communication channel (mailbox)
	 * Deprecated stop method is employed for an "unclean" sudden stop
	 */
	@SuppressWarnings("deprecation")
	public void destroyAutomaton(){
		changingstate.stop();
		mbox_thread.stop();
		mbox.end();
		try {
			sout.close();
			dout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cakesystem.setValve1_open_secs(0);
		cakesystem.setConveyor_velocity(0);
		cakesystem.setValve2_open_secs(0);
		cakesystem.deleteObserver(this);
		
	}
}
