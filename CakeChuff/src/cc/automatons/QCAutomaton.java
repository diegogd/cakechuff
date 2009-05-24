//TODO: Parar el robot en la parada de emergencia (el comportamiento es distinto al del robot1)

package cc.automatons;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import cc.communications.Mailbox;
import cc.simulation.elements.LightSensor;
import cc.simulation.elements.Sensor;
import cc.simulation.elements.TouchSensor;
import cc.simulation.state.QualitySubsystemState;


/**
 * Define the internal logic of the QCAutomaton
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class QCAutomaton extends Automaton {
	

	private Thread changingstate;
	//states
	private static final int START=0;
	private static final int INIT=1;
	private static final int QC=2;
	private static final int QC_STAMP=3;
	private static final int STAMP=4;
	private static final int STAMP_WAIT=5;
	private static final int KO_MOV=6;
	private static final int OK_WAIT=7;
	private static final int KO_WAIT=8;
	private static final int FAILURE=9;
	
	//parameters
	private int belt_lg, t_stamp, t_rob,f_chance;
	private float speed;
	private int cakes_blister;
	//simulation
	private QualitySubsystemState qcsystem;
	private boolean passed;

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
	public QCAutomaton(int portin, int portout, String master){
		state=START;
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
					
				}
			}
			dout = new PrintWriter(sout.getOutputStream(),true);
			//subscribe
			qcsystem = QualitySubsystemState.getInstance();
			qcsystem.addObserver(this);
			//tell the master the automaton is on
			send("A3:ON");
		}catch(UnknownHostException uhe){
			
		}catch(IOException ioe){
			
		}catch(SecurityException se){
			
		}
	}
	
	/**
	 * Set the QCAutomaton start parameters
	 * @param speed	Conveyor belt speed
	 * @param belt_lg Conveyor belt length
	 * @param f_rate Failure rate
	 * @param t_stamp Stamper time
	 * @param t_rob Robot time
	 */
	private void run_start(int speed,int belt_lg,int f_rate,int t_stamp,int t_rob){
		this.speed = (float)speed/(belt_lg*3);
		this.belt_lg=belt_lg;
		this.t_stamp=t_stamp;
		this.t_rob=7/t_rob;
		this.f_chance=f_rate;
		state=START;
		qcsystem.setRobot_velocity(this.t_rob);
		run_init();
		
	}
	
	/**
	 * The conveyor belt has to move
	 * Quality&Control Automaton state: INIT
	 * Set the conveyor belt speed
	 */
	private void run_init(){
		state=INIT;
		send("A3:init");
		if(!stop){
			qcsystem.setConveyor_velocity(speed);
			
		}
	}
	
	/**
	 * The sensor have to test the quality
	 * Quality&Control Automaton state: QC
	 * Stop the conveyor belt
	 * Depending of the quality test result, blisters are classified
	 */
	private void run_QC(){
		qcsystem.setConveyor_velocity(0);
		state=QC;
		send("A3:qc");
		qcsystem.setQualityCheck(true);
		
		try{
			Thread.sleep(3*1000);
		}catch(InterruptedException ie){
			//System.out.println("Interrupted");
			ie.printStackTrace();
		}
		cakes_blister=qcsystem.getIfQualityPassed();
		qcsystem.setQualityCheck(false);
		if(cakes_blister==4){
			passed=true;
			
			run_qc_stamp();
		}else{
			passed=false;
			run_ko_mov();
		}
	}
	
	/**
	 * Quality&Control Automaton state: QC_STAMP
	 * Restart the conveyor belt
	 */
	private void run_qc_stamp(){
		qcsystem.setConveyor_velocity(speed);
		state=QC_STAMP;
		send("A3:qc_stamp");		
	}
	
	/**
	 * The blister has to be stamped
	 * Stop the conveyor belt
	 * Stamp the blister
	 */
	private void run_stamp(){
		qcsystem.setConveyor_velocity(0);
		qcsystem.setWrapper_secs(t_stamp*2);
		qcsystem.setWrappedUp(true);
		try{
			Thread.sleep(t_stamp*1000);
		}catch(InterruptedException ie){
			ie.printStackTrace();
		}
		run_stamp_wait();
	}
	
	/**
	 * Quality&Control Automaton state: STAMP_WAIT
	 * Restart the conveyor belt
	 */
	private void run_stamp_wait(){
		qcsystem.setConveyor_velocity(speed);
		state=STAMP_WAIT;
		send("A3:stamp_wait");
	}
	
	/**
	 * The blister has not passed the quality control
	 * Quality&Control Automaton state: KO_MOV
	 * Restart the conveyor belt
	 */
	private void run_ko_mov(){
		qcsystem.setConveyor_velocity(speed);
		state=KO_MOV;
		send("A3:ko_mov:"+cakes_blister);
	}
	
	/**
	 * The blister has passed the quality control and it has to wait
	 * Quality&Control Automaton state: KO_WAIT
	 * Stop the conveyor belt
	 * Robot transition state to PICKUPPACKET
	 */
	private void run_ok_wait(){
		qcsystem.setConveyor_velocity(0);
		state=OK_WAIT;
		send("A3:ok_wait");
		//pick and box
		qcsystem.setRobotGoToState(qcsystem.PICKUPPACKET);
	}
	
	/**
	 * The blister has not passed the quality control and it has to wait
	 * Quality&Control Automaton state: KO_WAIT
	 * Stop the conveyor belt
	 * Robot transition state to PICKUPPACKET
	 */
	private void run_ko_wait(){
		qcsystem.setConveyor_velocity(0);
		state=KO_WAIT;
		send("A3:ko_wait");
		//pick and dispose
		qcsystem.setRobotGoToState(qcsystem.PICKUPPACKET);
	}
	
	
	/**
	 * Recover from a failure
	 */
	private void run_failure(String data){
		//TODO: Ver que hacer con el estado del robot 
		String pars[]=data.split("#");
		stop=false;
		this.belt_lg = Integer.parseInt(pars[3]);
		//qcsystem.addObserver(this);
		this.speed = Float.parseFloat(pars[2])/(belt_lg*3);
		this.t_stamp=Integer.parseInt(pars[5]);
		this.t_rob=Integer.parseInt(pars[6]);
		//this.f_chance=Integer.parseInt(pars[4]);
		//qcsystem.setRobotMoving(true);
		//Recover state
		if(pars[0].equalsIgnoreCase("INIT")){
			run_init();
		}else if(pars[0].equalsIgnoreCase("QC")){
			state=QC;
			changingstate=new Thread(this);
			changingstate.start();
		}else if(pars[0].equalsIgnoreCase("QC_STAMP")){
			run_qc_stamp();
		}else if(pars[0].equalsIgnoreCase("STAMP")){
			state=STAMP;
			changingstate=new Thread(this);
			changingstate.start();
		}else if(pars[0].equalsIgnoreCase("STAMP_WAIT")){
			run_stamp_wait();
		}else if(pars[0].equalsIgnoreCase("KO_MOV")){
			run_ko_mov();
		}else if(pars[0].equalsIgnoreCase("OK_WAIT")){
			state=OK_WAIT;
			changingstate=new Thread(this);
			changingstate.start();
		}else if(pars[0].equalsIgnoreCase("KO_WAIT")){
			state=KO_WAIT;
			changingstate=new Thread(this);
			changingstate.start();
		}
	}
	
	/**
	 * Restart after a stop
	 */
	private void run_stop(){
		stop=true;
		if(changingstate!=null) changingstate.stop();
		qcsystem.setConveyor_velocity(0);
		//qcsystem.deleteObserver(this);
		//qcsystem.setRobotMoving(false);
		//state=START;
	}
	
	/**
	 * Manage the control and synchronize messages received from the MasterAutomaton
	 * @param msg Message received from the MasterAutomaton
	 */
	@Override
	public synchronized void newMsg(String msg) {
		String[] content= msg.split(":");
		//Emergencies work for any state
		if(content[0].equals("EMERGENCY")) run_stop();
		else if (content[0].equalsIgnoreCase("STOP")) stop=true;
		else if (content[0].equalsIgnoreCase("RESET")) 
			run_failure(content[1]);
		else if (content[0].equalsIgnoreCase("RESTART")) {
			//String pars[] = content[1].split("\\$");
			run_failure(content[1]);
		}else
		switch(state){
		case START: if(content[0].equalsIgnoreCase("init")){
			stop=false;
			String[] pars=content[1].split("#");
			run_start(Integer.parseInt(pars[0]),
					Integer.parseInt(pars[1]),
					Integer.parseInt(pars[2]),
					Integer.parseInt(pars[3]),
					Integer.parseInt(pars[4])
					);
			}
					break;
		case INIT: //a blister arrives
			break;
		case QC: break;
		case QC_STAMP: break;
		case STAMP: break;
		case STAMP_WAIT: break;
		case KO_MOV: break;
		case OK_WAIT: break;
		case KO_WAIT: break;
		case FAILURE: break;	
		
		}
	}
	
	/**
	 * Run the QualityAutomaton
	 */
	public void run() {
		switch (state) {
		case QC:
			run_QC();
			break;
		case STAMP:
			run_stamp();
			break;
		case OK_WAIT:
			run_ok_wait();
			break;
		case KO_WAIT:
			run_ko_wait();
			break;
		}
	}
	
	/**
	 * Produce state transition depending on the signals received from the sensors
	 * @param arg Sensor in the conveyor belt which detects the position and quality of the blister
	 */
	@Override
	public void update(Observable o, Object arg) {
		// which sensor?
		if (arg instanceof LightSensor) {
			if (((LightSensor) arg).getName().equals("QualitySensor1")) {

				if (((Sensor) arg).isActived()) {
					state = QC;
					changingstate=new Thread(this);
					changingstate.start();
				}
			} else if (((LightSensor) arg).getName().equals("QualitySensor2")) {
				if (state == QC_STAMP) {
					if (((Sensor) arg).isActived()) {
						state = STAMP;
						changingstate=new Thread(this);
						changingstate.start();
					}
				}
			}

		} else if (arg instanceof TouchSensor) {
			if (((Sensor) arg).isActived())
				if (state == STAMP_WAIT) {
					state = OK_WAIT;
					changingstate=new Thread(this);
					changingstate.start();
				} else {
					state = KO_WAIT;
					changingstate=new Thread(this);
					changingstate.start();

				}
		} else if (o instanceof QualitySubsystemState && ((QualitySubsystemState) o).isChanged_CS()) {
			//qcsystem.deleteObserver(this);
			if (!qcsystem.getRobotIfMoving()) {
				if (qcsystem.getRobotCurrentState() == qcsystem.PICKUPPACKET) {
					if (state == OK_WAIT) {
						qcsystem.setRobotGoToState(qcsystem.DROPGOODBOX);
						qcsystem.setConveyor_velocity(speed);
						state = INIT;
						run_init();
					} else if (state == KO_WAIT) {
						qcsystem.setRobotGoToState(qcsystem.DROPBADBOX);
						qcsystem.setConveyor_velocity(speed);
						state = INIT;
						run_init();
					}
				}
			}
			//qcsystem.addObserver(this);
		}

	}
	
	/**
	 * Stop the QCAutomaton's communication channel (mailbox)
	 * Deprecated stop method is employed for an "unclean" sudden stop
	 */
	@SuppressWarnings("deprecation")
	public void destroyAutomaton(){
		if(changingstate!=null) changingstate.stop();
		mbox_thread.stop();
		mbox.end();
		try {
			sout.close();
			dout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		qcsystem.deleteObserver(this);
		qcsystem.setConveyor_velocity(0);
		qcsystem.setRobotMoving(false);
		qcsystem.setQualityCheck(false);
	}
	
	/**
	 * Execute the QCAutomaton independently
	 * @param args The command line arguments
	 */
	public static void main(String args[]){
		QCAutomaton aut=new QCAutomaton(Integer.parseInt(args[0]),Integer.parseInt(args[1]),args[2]);
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
