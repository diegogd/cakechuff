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
import cc.simulation.state.SystemState;
public class BlisterAutomaton extends Automaton {
	
	private static final int START=0;
	private static final int INIT=1;
	private static final int PRESS=2;
	private static final int CUTTING=3;
	private static final int BLISTER_READY=4;
	private static final int FAILURE=5;
	
	//param
	private int speed, belt_lg;
	
	//simulation
	private BlisterSubsystemState blistersystem;
	private SystemState sys;
	
	public BlisterAutomaton(int portin, int portout, String master){
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
			blistersystem = BlisterSubsystemState.getInstance();
			blistersystem.addObserver(this);
			sys=SystemState.getInstance();
			//tell the master the automaton is on
			send("A2:ON");
		}catch(UnknownHostException uhe){
			
		}catch(IOException ioe){
			
		}catch(SecurityException se){
			
		}
	}
	public void run_start(int speed, int belt_lg){
		this.speed=speed;
		this.belt_lg=belt_lg;
		state=START;
		//send new state
		/*try{
			dout.writeChars("A2:START");
		}catch(IOException ioe){
			//connection failure
			
		}*/
		send("A2:START");
		run_init();
		
	}
	public void run_init(){
		//start conveyor
		blistersystem.setConveyor_velocity(speed);
		state=START;
		//send new state
		/*try{
			dout.writeChars("A2:INIT");
		}catch(IOException ioe){
			//connection failure

		}*/
		send("A2:INIT");
		//press timer...
		try{
			Thread.sleep(2*1000);
		}catch(InterruptedException ie){}
		//press down -> run_press() ??
		blistersystem.setEngraver_secs(8);
		sys.setMakeBlister();
		
	}
	public void run_press(){
		state=PRESS;
		//send new state
		/*try{
			dout.writeChars("A2:PRESS");
		}catch(IOException ioe){
			//connection failure
			
		}*/
		send("A2:PRESS");
	}
	public void run_cutting(){
		//blade down
		blistersystem.setCutter_secs(4);
		state=CUTTING;
		//send new state
		/*try{
			dout.writeChars("A2:CUTTING");
		}catch(IOException ioe){
			//connection failure
			
		}*/
		send("A2:CUTTING");
	}
	public void run_blister_ready(){
		//stop conveyor
		blistersystem.setConveyor_velocity(0);
		state=BLISTER_READY;
		//send new state
		/*try{
			dout.writeChars("A2:BLISTER_READY");
		}catch(IOException ioe){
			//connection failure
			
		}*/
		send("A2:BLISTER_READY");
		
	}
	public void run_failure(){
		
	}
	public void run_stop(){
		//stop conveyor
	}
	@Override
	public synchronized void newMsg(String msg) {
		String[] content= msg.split("#");
		//Emergencies work for any state
		if(content[0].equals("ER")) run_stop();
		switch(state){
		case START: if(content[0].equalsIgnoreCase("init")) run_start(Integer.parseInt(content[1]),
													Integer.parseInt(content[2]));
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

	@Override
	public void update(Observable o, Object arg) {
		// which sensor?
		if (arg instanceof LightSensor) {
			if (((Sensor) arg).isActived())
				run_cutting();
			// else run_choc_car();

		} else if (arg instanceof TouchSensor) {
			if (((Sensor) arg).isActived())
				run_blister_ready();
			// else -> no action, blister's pickup is received as a message
		}
	}
	public static void main(String args[]){
		BlisterAutomaton aut=new BlisterAutomaton(Integer.parseInt(args[0]),Integer.parseInt(args[1]),args[2]);
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
