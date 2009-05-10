package cc.automatons;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import cc.communications.Mailbox;
import cc.simulation.state.CakeSubsystemState;

import cc.simulation.elements.Sensor;
import cc.simulation.elements.LightSensor;
import cc.simulation.elements.TouchSensor;
public class CakeAutomaton extends Automaton {
	
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
	int speed, belt_lg, cake_cap, vt1, vt2;
	
	//comm
	Socket sout;
	DataOutputStream dout;
	
	//simulation
	CakeSubsystemState cakesystem;
	
	public CakeAutomaton(int portin, int portout, String master){
		state=-1;
		try{
			mbox= new Mailbox(this, portin);
			mbox.run();
			sout= new Socket(master, portout);
			dout = new DataOutputStream(sout.getOutputStream());
			//subscribe
			cakesystem = CakeSubsystemState.getInstance();
			cakesystem.addObserver(this);
		}catch(UnknownHostException uhe){
			
		}catch(IOException ioe){
			
		}catch(SecurityException se){
			
		}
		
	}
	private void run_start(int speed, int belt_lg, int cake_cap, int vt1, int vt2){
		this.speed = speed;
		this.belt_lg = belt_lg;
		this.cake_cap = cake_cap;
		this.vt1=vt1;
		this.vt2 = vt2;
		state=START;
		//send new state
		try{
			dout.writeChars("A1:START");
		}catch(IOException ioe){
			//connection failure
			
		}
	}
	private void run_init(){
		//start conveyor
		//drop cake
		try{
			dout.writeChars("A1:INIT");
		}catch(IOException ioe){
			//connection failure
			
		}
		state= INIT;
	}
	private void run_choc(){
		//stop conveyor
		//open chocolate valve
		state=CHOC;
		try{
			dout.writeChars("A1:CHOC");
		}catch(IOException ioe){
			//connection failure	
		}
		
		//change to choc_car here??
		try{
			Thread.sleep(vt1*1000);
		}catch(InterruptedException ie){}
		//close chocolate valve
		//run conveyor
		
		
	}
	private void run_choc_car(){
		state=CHOC_CAR;
		try{
			dout.writeChars("A1:CHOC_CAR");
		}catch(IOException ioe){
			//connection failure	
		}
	}
	private void run_car(){
		//stop conveyor
		//open caramel valve
		state=CHOC;
		try{
			dout.writeChars("A1:CAR");
		}catch(IOException ioe){
			//connection failure	
		}
		
		//change to car_wait here??
		try{
			Thread.sleep(vt1*1000);
		}catch(InterruptedException ie){}
		//close caramel valve
		//run conveyor
	}
	private void run_car_wait(){
		state=CHOC_CAR;
		try{
			dout.writeChars("A1:CAR_WAIT");
		}catch(IOException ioe){
			//connection failure	
		}
	}
	private void run_wait(){
		//stop conveyor
		state=WAIT;
		try{
			dout.writeChars("A1:WAIT");
		}catch(IOException ioe){
			//connection failure	
		}
	}
	private void run_failure(){
		
	}
	private void run_stop(){
		
	}
	@Override
	public void newMsg(String msg) {
		String[] content= msg.split("#");
		//Emergencies work for any state
		if(content[0].equals("ER")) run_stop();
		switch(state){
		case START: if(content[0].equalsIgnoreCase("init")) run_start(Integer.getInteger(content[1]),
													Integer.getInteger(content[2]),
													Integer.getInteger(content[3]),
													Integer.getInteger(content[4]),
													Integer.getInteger(content[5]));
					break;
		case INIT: break;
		case CHOC: break;
		case CHOC_CAR: break;
		case CAR: break;
		case CAR_WAIT: break;
		case WAIT:
			if(content[0].equalsIgnoreCase("R1")&& content[1].substring(0,4).equalsIgnoreCase("cake")){
			run_init();
		}
			break;
		case FAILURE: break;	
		
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// which sensor?
		if(((Sensor)arg1).getName().equalsIgnoreCase("sensor1")){
			if(((Sensor)arg1).isActived()) run_choc();
			else run_choc_car();
		}else if(((Sensor)arg1).getName().equalsIgnoreCase("sensor2")){
			if(((Sensor)arg1).isActived()) run_car();
			else run_car_wait();
		}else if(((Sensor)arg1) instanceof TouchSensor) {
			if(((Sensor)arg1).isActived()) run_wait();
			//else -> no action, cake's pickup is received as a message  
		}
		
	}

}
