package cc.automatons;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import cc.communications.Mailbox;
import cc.simulation.elements.LightSensor;
import cc.simulation.elements.Sensor;
import cc.simulation.elements.TouchSensor;
import cc.simulation.state.CakeSubsystemState;
import cc.simulation.state.QualitySubsystemState;
import cc.simulation.state.SystemState;

public class QCAutomaton extends Automaton {
	

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

	//simulation
	QualitySubsystemState qcsystem;
	boolean passed;
	public QCAutomaton(int portin, int portout, String master){
		state=START;
		try{
			mbox= new Mailbox(this, portin);
			(new Thread(mbox)).start();
			boolean connected=false;
			while(!connected){
				try{
					sout= new Socket(master, portout);
					connected=true;
				}catch(Exception e){
					
				}
			}
			//dout = new DataOutputStream(sout.getOutputStream());
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
	private void run_start(int speed,int belt_lg,int f_rate,int t_stamp,int t_rob){
		this.speed = (float)speed/(belt_lg*3);
		this.belt_lg=belt_lg;
		this.t_stamp=t_stamp;
		this.t_rob=t_rob;
		this.f_chance=f_rate;
		state=START;

		//send("A3:START");
		//now wait for a cake pack
		run_init();
		
	}
	private void run_init(){
		if(!stop){
			System.out.println("Starting A2...");
			qcsystem.setConveyor_velocity(speed);
			state=INIT;
			send("A3:init");
		}
	}
	private void run_QC(){
		qcsystem.setConveyor_velocity(0);
		state=QC;
		send("A3:qc");
		qcsystem.setQualityCheck(true);
		try{
			Thread.sleep(3*1000);
		}catch(InterruptedException ie){
			System.out.println("Interrumpido");
			ie.printStackTrace();
		}
		qcsystem.setQualityCheck(false);
		if(Math.random()*100<f_chance){
			passed=false;
			run_ko_mov();
		}else{
			passed=true;
			run_qc_stamp();
		}
		
	}
	private void run_qc_stamp(){
		qcsystem.setConveyor_velocity(speed);
		state=QC_STAMP;
		send("A3:qc_stamp");		
	}
	private void run_stamp(){
		qcsystem.setConveyor_velocity(0);
		qcsystem.setWrapper_secs(2);
		try{
			Thread.sleep(3*1000);
		}catch(InterruptedException ie){
			System.out.println("Interrumpido");
			ie.printStackTrace();
		}
		run_stamp_wait();
	}
	private void run_stamp_wait(){
		qcsystem.setConveyor_velocity(speed);
		state=STAMP_WAIT;
		send("A3:stamp_wait");
		
	}
	private void run_ko_mov(){
		qcsystem.setConveyor_velocity(speed);
		state=KO_MOV;
		send("A3:ko_mov");
	}
	private void run_ok_wait(){
		qcsystem.setConveyor_velocity(0);
		state=OK_WAIT;
		send("A3:ok_wait");
		//pick and box
		qcsystem.setRobot_velocity(4f);
		qcsystem.setRobotGoToState(qcsystem.PICKUPPACKET);
	}
	private void run_ko_wait(){
		qcsystem.setConveyor_velocity(0);
		state=KO_WAIT;
		send("A3:ko_wait");
		//pick and dispose
		qcsystem.setRobotGoToState(qcsystem.PICKUPPACKET);
	}
	private void run_failure(){
		
	}
	private void run_stop(){
		qcsystem.setConveyor_velocity(0);
		qcsystem.deleteObserver(this);
		qcsystem.setRobotMoving(false);
		state=START;
	}
	@Override
	public synchronized void newMsg(String msg) {
		System.out.println("QCAutomaton receives:"+msg);
		String[] content= msg.split(":");
		//Emergencies work for any state
		if(content[0].equals("EMERGENCY")) run_stop();
		else if (content[0].equalsIgnoreCase("STOP")) stop=true;
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
	@Override
	public void update(Observable o, Object arg) {
		// which sensor?
		if (arg instanceof LightSensor) {
			if(((LightSensor)arg).getName().equals("QualitySensor1")){
				
			if (((Sensor) arg).isActived()){
				state=QC;
				(new Thread(this)).start();
			}
			}else if(((LightSensor)arg).getName().equals("QualitySensor2")){
				if(state==QC_STAMP){
					if (((Sensor) arg).isActived()){
						state=STAMP;
						(new Thread(this)).start();
					}
				}
			}

		} else if (arg instanceof TouchSensor) {
			if (((Sensor) arg).isActived())
				if(state==STAMP_WAIT){
					state=OK_WAIT;
					(new Thread(this)).start();
				}
				else{
					state=KO_WAIT;
					(new Thread(this)).start();
				
				}
		}else if(o instanceof QualitySubsystemState){
			qcsystem.deleteObserver(this);
			if (!qcsystem.getRobotIfMoving()) {
				if (qcsystem.getRobotCurrentState()==qcsystem.PICKUPPACKET){
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
			qcsystem.addObserver(this);
		}

	}
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
