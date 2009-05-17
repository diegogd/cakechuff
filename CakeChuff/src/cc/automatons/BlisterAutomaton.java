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
	
	private static final int START=0;
	private static final int INIT=1;
	private static final int PRESS=2;
	private static final int CUTTING=3;
	private static final int BLISTER_READY=4;
	private static final int FAILURE=5;
	
	//param
	private int belt_lg;
	private float speed;
	
	//simulation
	private BlisterSubsystemState blistersystem;
	private SystemState sys;
	private Stamper stamper;
	public BlisterAutomaton(int portin, int portout, String master){
		state=START;
		stop=false;
		try{
			mbox= new Mailbox(this, portin);
			(new Thread(mbox)).start();
			boolean connected=false;
			while(!connected){
				try{
					sout= new Socket(master, portout);
					connected=true;
				}catch(Exception e){
					System.out.println("BLISTERAutomaton Error:");
					e.printStackTrace();
				}
			}
			System.out.println("BlisterAutomaton connected");
			dout = new PrintWriter(sout.getOutputStream(),true);
			//subscribe
			blistersystem = BlisterSubsystemState.getInstance();
			blistersystem.addObserver(this);
			sys=SystemState.getInstance();
			//tell the master the automaton is on
			send("A2:ON");
		}catch(UnknownHostException uhe){
			System.out.println("BLISTERAutomaton Error:");
			uhe.printStackTrace();
		}catch(IOException ioe){
			System.out.println("BLISTERAutomaton Error:");
			ioe.printStackTrace();
		}catch(SecurityException se){
			System.out.println("BLISTERAutomaton Error:");
			se.printStackTrace();
		}
	}
	public void run_start(int speed, int belt_lg){
		this.speed=(float)speed/(belt_lg*7);
		this.belt_lg=belt_lg;
		stamper=new Stamper(blistersystem,sys,speed);
		(new Thread(stamper)).start();
		state=START;
		//send new state
		//send("A2:START");
		run_init();
		
	}
	public void run_init(){
		//start conveyor
		blistersystem.setConveyor_velocity(speed);
		state=INIT;
		//send new state
		send("A2:init");
		//press timer...
		/*try{
			System.out.println("BListerAutomaton: time to engrave: "+(int)(60/(speed*20)));
			Thread.sleep((int)(60*1000/(speed*40)));
		}catch(InterruptedException ie){}
		//press down -> run_press() ??
		blistersystem.setEngraver_secs(5);
		sys.setMakeBlister();*/
		stamper.work();
		run_press();
		
	}
	public void run_press(){
		state=PRESS;
		//send new state
		send("A2:press");
	}
	public void run_cutting(){
		//blade down
		System.out.println("Blade speed: " +(int)(60/(speed*10)));
		blistersystem.setCutter_secs((int)(60/(speed*10)));
		state=CUTTING;
		//send new state
		send("A2:cutting");
	}
	public void run_blister_ready(){
		//stop conveyor
		blistersystem.setConveyor_velocity(0);
		stamper.stop();
		state=BLISTER_READY;
		//send new state
		send("A2:blister_ready");
		
	}
	public void run_failure(){
		
	}
	public void run_stop(){
		blistersystem.setConveyor_velocity(0);
		stamper.stop();
		state=START;
	}
	@Override
	public synchronized void newMsg(String msg) {
		System.out.println("BlisterAutomaton: processing message "+msg);
		String[] content= msg.split(":");
		//Emergencies work for any state
		if(content[0].equals("EMERGENCY")) run_stop();
		else if (content[0].equalsIgnoreCase("STOP")) stop=true;
		switch(state){
		case START: if(content[0].equalsIgnoreCase("init")){
			String[] pars=content[1].split("\\#");
			run_start(Integer.parseInt(pars[0]),Integer.parseInt(pars[1]));
		}
					break;
		case INIT: break;
		case PRESS: break;
		case CUTTING: break;
		case BLISTER_READY:
			if(content[0].equalsIgnoreCase("R1")&& content[1].equalsIgnoreCase("blister")){
			run_init();
		}
			break;
		case FAILURE: break;	
		
		}

	}
	public void run(){
		switch(state){
		case CUTTING:run_cutting();
			break;
		case BLISTER_READY:run_blister_ready();
			break;
		}
	}
	@Override
	public void update(Observable o, Object arg) {
		// which sensor?
		if (arg instanceof LightSensor) {
			if (((Sensor) arg).isActived()){
				state=CUTTING;
				(new Thread(this)).start();
			}
			// else run_choc_car();

		} else if (arg instanceof TouchSensor) {
			if (((Sensor) arg).isActived()){
				state=BLISTER_READY;
				(new Thread(this)).start();
			}
			// else -> no action, blister's pickup is received as a message
		}
	}
	public static void main(String args[]){
		BlisterAutomaton aut=new BlisterAutomaton(Integer.parseInt(args[0]),Integer.parseInt(args[1]),args[2]);
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private class Stamper implements Runnable{
		private boolean working;
		//simulation
		private BlisterSubsystemState blistersystem;
		private int freq;
		private SystemState sys;
		private Stamper(BlisterSubsystemState blisters,SystemState sys, int speed){
			blistersystem=blisters;
			this.sys=sys;
			freq=(int)(60*1000*3/(speed));
			working=false;
		}
		private void work(){
			working=true;
		}
		private void stop(){
			working=false;
		}
		public void run(){
			while(true){
				try{
					System.out.println("Duerme: " +freq);
					Thread.sleep(freq);
				}catch(InterruptedException ie){}
				if (working){
					blistersystem.setEngraver_secs(5);
					sys.setMakeBlister();
				}
			}

		}
	}

}
