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
	int speed, belt_lg, t_stamp, t_rob,f_chance;

	//simulation
	QualitySubsystemState qcsystem;
	boolean passed;
	public QCAutomaton(int portin, int portout, String master){
		state=0;
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
		}catch(UnknownHostException uhe){
			
		}catch(IOException ioe){
			
		}catch(SecurityException se){
			
		}
	}
	private void run_start(int speed,int belt_lg,int t_stamp,int t_rob){
		this.speed=speed;
		this.belt_lg=belt_lg;
		this.t_stamp=t_stamp;
		this.t_rob=t_rob;
		this.f_chance=0;
		state=START;
		/*try{
			dout.writeChars("A3:START");
		}catch(IOException ioe){
			//connection failure
			
		}*/
		//send("A3:START");
		//now wait for a cake pack
		
	}
	private void run_init(){
		qcsystem.setConveyor_velocity(speed);
		state=INIT;
		send("A3:INIT");
	}
	private void run_QC(){
		qcsystem.setConveyor_velocity(0);
		state=QC;
		send("A3:QC");
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
		send("A3:QC_STAMP");		
	}
	private void run_stamp(){
		qcsystem.setConveyor_velocity(0);
		qcsystem.setWrapper_secs(8);
		run_stamp_wait();
	}
	private void run_stamp_wait(){
		qcsystem.setConveyor_velocity(speed);
		state=STAMP_WAIT;
		send("A3:STAMP_WAIT");
		
	}
	private void run_ko_mov(){
		qcsystem.setConveyor_velocity(speed);
		state=KO_MOV;
		send("A3:KO_MOV");
	}
	private void run_ok_wait(){
		qcsystem.setConveyor_velocity(0);
		state=OK_WAIT;
		send("A3:OK_WAIT");
		//pick and box
	}
	private void run_ko_wait(){
		qcsystem.setConveyor_velocity(0);
		state=KO_WAIT;
		send("A3:KO_WAIT");
		//pick and dispose
	}
	private void run_failure(){
		
	}
	private void run_stop(){
		
	}
	@Override
	public synchronized void newMsg(String msg) {
		String[] content= msg.split("#");
		//Emergencies work for any state
		if(content[0].equals("ER")) run_stop();
		switch(state){
		case START: if(content[0].equalsIgnoreCase("init")) run_start(Integer.parseInt(content[1]),
													Integer.parseInt(content[2]),
													Integer.parseInt(content[3]),
													Integer.parseInt(content[4]));
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

	@Override
	public void update(Observable o, Object arg) {
		// which sensor?
		if (arg instanceof LightSensor) {
			if (((Sensor) arg).isActived())
				run_QC();
			// else run_choc_car();

		} else if (arg instanceof TouchSensor) {
			if (((Sensor) arg).isActived())
				if(state==STAMP_WAIT)run_ok_wait();
				else run_ko_wait();
		}

	}
	public static void main(String args[]){
		QCAutomaton aut=new QCAutomaton(Integer.parseInt(args[0]),Integer.parseInt(args[1]),args[2]);
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
