package cc.automatons;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import cc.communications.MasterMailbox;
import cc.simulation.state.CakeSubsystemState;
import cc.simulation.state.Robot1State;

public class MasterAutomaton extends Automaton {
	/* state for robot (inherited)*/
	private Robot1State robot;
	// empty, blister, cake1, cake2, cake3, full
	private static final int START=0;
	private static final int EMPTY=1;
	private static final int BLISTER=2;
	private static final int CAKE1=3;
	private static final int CAKE2=4;
	private static final int CAKE3=5;
	private static final int FULL=6;
	private static final int FAILURE=7;
	//parameters for the robot
	private int rmovet;
	
	//Mailboxes
	private MasterMailbox mboxScada;
	private MasterMailbox mboxCake;
	private MasterMailbox mboxBlister;
	private MasterMailbox mboxQC;
	
	//the automatons are running (used to check restarts)
	private boolean cake_on,blister_on,qc_on;
	//a cake is waiting but there is no blister
	boolean cake_waiting;
	//blister waiting for an empty table
	boolean blister_waiting;
	public MasterAutomaton(String scada, int scada_in, int scada_out,
			String cake, int cake_in, int cake_out,
			String blister,	int blister_in, int blister_out,
			String qc, int qc_in, int qc_out) {
		state = -1;

		// comm
		mboxScada = new MasterMailbox(this, scada_in, scada_out, scada);
		mboxCake = new MasterMailbox(this, cake_in, cake_out, cake);
		mboxBlister = new MasterMailbox(this, blister_in, blister_out, blister);
		mboxQC = new MasterMailbox(this, qc_in, qc_out, qc);

		// subscribe
		robot = Robot1State.getInstance();
		robot.addObserver(this);
		
		//
		cake_on=false;
		blister_on=false;
		qc_on=false;
		
		cake_waiting=false;
		blister_waiting=false;
	}
	private void run_robot_start(int movet){
		rmovet=movet;
	}
	private void run_robot_blister(){
		//move blister to the packing table
		//tell the blister automaton
	}
	private void run_robot_cake1(){
		//put cake in the blister
		//tell the cake automaton
	}
	private void run_robot_cake2(){
		//put cake in the blister
		//tell the cake automaton
	}
	private void run_robot_cake3(){
		//put cake in the blister
		//tell the cake automaton
	}
	private void run_robot_full(){
		//put cake in the blister
		//tell the cake automaton
		//move full blister to the qc conveyor
		//tell the qc automaton
		//blisters waiting?
	}
	@Override
	public synchronized void newMsg(String msg) {
		String[] content=msg.split(":");
		if(content[0].equalsIgnoreCase("A1")){
			if(content[1].equalsIgnoreCase("ON")){
				if(cake_on==false)cake_on=true;
				else mboxScada.send("RECOVER:A1"); //was already running so it must be a new instance
			}else{
				//state change
				mboxScada.send(msg);
				if(content[1].equalsIgnoreCase("WAIT")){
					if(state==BLISTER){
						run_robot_cake1();
					}else if(state==CAKE1){
						run_robot_cake2();
					}else if(state==CAKE2){
						run_robot_cake3();
					}else if(state==CAKE3){
						run_robot_full();
					}else{
						//the cake must wait for a blister
						cake_waiting=true;
					}
						
				}
				
			}
		}else if(content[0].equalsIgnoreCase("A2")){
			if(content[1].equalsIgnoreCase("ON")){
				if(cake_on==false)cake_on=true;
				else mboxScada.send("RECOVER:A2"); //was already running so it must be a new instance
			}else{
				//state change
				mboxScada.send(msg);
				if(content[1].equalsIgnoreCase("BLISTER_READY")){
					if(state==EMPTY){
						run_robot_blister();
					}else blister_waiting=true;
				}
			}
		}else if(content[0].equalsIgnoreCase("A3")){
			if(content[1].equalsIgnoreCase("ON")){
				if(cake_on==false)cake_on=true;
				else mboxScada.send("RECOVER:A3"); //was already running so it must be a new instance
			}else{
				//state change
				mboxScada.send(msg);
			}
		}else if(content[0].equalsIgnoreCase("INIT")){
			String[] pars=content[1].split("$");
			mboxCake.send(pars[0]);
			mboxBlister.send(pars[1]);
			mboxQC.send(pars[2]);
			run_robot_start(Integer.getInteger(pars[3]));
		}else if(content[0].equalsIgnoreCase("RESTART")){
			String[] pars= content[1].split("$");
			if(pars[1].equalsIgnoreCase("A1")){
				mboxCake.send(pars[1]);
			}else if(pars[1].equalsIgnoreCase("A2")){
				mboxBlister.send(pars[1]);
			}if(pars[1].equalsIgnoreCase("A3")){
				mboxQC.send(pars[1]);
			}
		}else if(content[0].equalsIgnoreCase("STOP")){
			mboxCake.send(msg);
			mboxBlister.send(msg);
			mboxQC.send(msg);
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
