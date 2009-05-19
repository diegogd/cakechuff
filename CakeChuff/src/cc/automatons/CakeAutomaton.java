package cc.automatons;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import cc.communications.Mailbox;
import cc.simulation.state.CakeSubsystemState;
import cc.simulation.state.SystemState;

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
	private int belt_lg, cake_cap, vt1, vt2;
	private float speed;
	private int ncakes;
		
	//simulation
	private CakeSubsystemState cakesystem;
	private SystemState sys;
	public CakeAutomaton(int portin, int portout, String master){
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
		}catch(UnknownHostException uhe){
			
		}catch(IOException ioe){
			
		}catch(SecurityException se){
			
		}
		
	}
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
		ncakes--;
		System.out.println("The Cake producing process will start now");
		run_init();
	}
	private void run_init(){
		
		//drop cake
		
		//start conveyor
		cakesystem.setConveyor_velocity(speed);
		state= INIT;
		send("A1:init");
	}
	private void run_choc(){
		//stop conveyor
		cakesystem.setConveyor_velocity(0);
		//open chocolate valve
		cakesystem.setValve1_open_secs(vt1);
		state=CHOC;
		send("A1:choc");
		//change to choc_car here??
		//while(cakesystem.getValve1_open_secs()>0);
		
		try{
			Thread.sleep(vt1*1000);
		}catch(InterruptedException ie){
			System.out.println("Interrumpido");
			ie.printStackTrace();
		}
		cakesystem.setValve1_open_secs(0);
		//close chocolate valve ¿?
		//run conveyor
		cakesystem.setConveyor_velocity(speed);
		
	}
	private void run_choc_car(){
		state=CHOC_CAR;
		/*There are cakes left
		 * &
		 * if the automaton is going to stop, the number of cakes dropped is nx4
		 * (to complete n blisters)
		 */		
		if(ncakes>0 && (!stop||(cake_cap-ncakes)%4!=0)){
			sys.setDropCake();
			ncakes--;
		}
		send("A1:choc_car");
	}
	private void run_car(){
		//stop conveyor
		cakesystem.setConveyor_velocity(0);
		//open caramel valve
		cakesystem.setValve2_open_secs(vt2);
		state=CHOC;
		
		send("A1:car");
		
		//change to car_wait here??
		try{
			Thread.sleep(vt2*1000);
		}catch(InterruptedException ie){}
		//close caramel valve
		cakesystem.setValve2_open_secs(0);
		//run conveyor
		cakesystem.setConveyor_velocity(speed);
	}
	private void run_car_wait(){
		state=CAR_WAIT;
		send("A1:car_wait");
	}
	private void run_wait(){
		cakesystem.setConveyor_velocity(0);
		//stop conveyor
		state=WAIT;
		send("A1:wait");
	}
	/*
	 * Recover form a failure
	 */
	private void run_failure(String data){
		String pars[]=data.split("#");
		//INIT,CHOC,CHOC_CAR,CAR,CAR_WAIT,WAIT
		//int cake_cap, int speed, int belt_lg, int vt1, int vt2
		
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
			(new Thread(this)).start();
		}else if(pars[0].equalsIgnoreCase("CHOC_CAR")){
			state=CHOC_CAR;
			(new Thread(this)).start();
		}else if(pars[0].equalsIgnoreCase("CAR")){
			state=CAR;
			(new Thread(this)).start();
		}else if(pars[0].equalsIgnoreCase("CAR_WAIT")){
			run_car_wait();
		}else if(pars[0].equalsIgnoreCase("WAIT")){
			run_wait();
		}
		
	}
	private void run_stop(){
		cakesystem.setConveyor_velocity(0);
		cakesystem.setValve1_open_secs(0);
		cakesystem.setValve2_open_secs(0);
		state=START;
	}
	@Override
	public synchronized void newMsg(String msg) {
		while (treatingupdate){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {

			}
		}
		System.out.println("CakeAutomaton: processing message");
		String[] content = msg.split(":");
		// Emergencies work for any state
		if (content[0].equals("EMERGENCY"))
			run_stop();
		else if (content[0].equalsIgnoreCase("STOP"))
			stop = true;
		else if (content[0].equalsIgnoreCase("RESTART")) {
			String pars[] = content[1].split("\\$");
			run_failure(pars[1]);
		} else
			switch (state) {
			case START:
				if (content[0].equalsIgnoreCase("INIT")) {
					System.out.println("CakeAutomaton: Received init signal");
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
				if (content[0].equalsIgnoreCase("R1")
						&& content[1].substring(0, 4).equalsIgnoreCase("cake")) {
					run_init();
				}
				break;
			case FAILURE:
				break;

			}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		treatingupdate=true;
		// which sensor?
		if (arg1 instanceof Sensor) {
			if (((Sensor) arg1).getName().equalsIgnoreCase("sensor1")) {
				if (((Sensor) arg1).isActived()){
					state=CHOC;
					(new Thread(this)).start();
				}
				else{
					state = CHOC_CAR;
					(new Thread(this)).start();
				}
					
			} else if (((Sensor) arg1).getName().equalsIgnoreCase("sensor2")) {
				if (((Sensor) arg1).isActived()){
					state=CAR;
					(new Thread(this)).start();
				}
				else
					run_car_wait();
			} else if (((Sensor) arg1).getName().equalsIgnoreCase("sensor3")) {
				
				if (((Sensor) arg1).isActived() && state!=WAIT){
					System.out.println("A1: TOCADO SENSOR DE FINAL DE LA CINTA POR UNA TARTA QUE PASABA POR AHÍ.");
					state=WAIT;
					(new Thread(this)).start();
				}
				// else -> no action, cake's pickup is received as a message
			}
			treatingupdate=false;;
		}
	}
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
	public static void main(String args[]){
		CakeAutomaton aut=new CakeAutomaton(Integer.parseInt(args[0]),Integer.parseInt(args[1]),args[2]);
		//CakeAutomaton aut=new CakeAutomaton(9001,9000,"localhost");
		
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
