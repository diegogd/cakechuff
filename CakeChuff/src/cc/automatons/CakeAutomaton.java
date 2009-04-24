package cc.automatons;

import java.util.Observable;

import cc.communications.Mailbox;

public class CakeAutomaton extends Automaton {
	
	int state;
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
	
	public CakeAutomaton(int port){
		state=0;
		//mbox= new Mailbox(this, port);
		//mbox.run();
	}
	private void run_start(int speed, int belt_lg, int cake_cap, int vt1, int vt2){
		this.speed = speed;
		this.belt_lg = belt_lg;
		this.cake_cap = cake_cap;
		this.vt1=vt1;
		this.vt2 = vt2;
		state=INIT;
		//send new state
	}
	@Override
	public void newMsg(String[] msg) {
		
		//Emergencies work for any state
		if(msg[0].equals("ER"))
		switch(state){
		case START: if(msg[0].equals("init")) run_start(Integer.getInteger(msg[1]),
													Integer.getInteger(msg[2]),
													Integer.getInteger(msg[3]),
													Integer.getInteger(msg[4]),
													Integer.getInteger(msg[5]));
					break;
		case INIT: break;
		case CHOC: break;
		case CHOC_CAR: break;
		case CAR: break;
		case CAR_WAIT: break;
		case WAIT: break;
		case FAILURE: break;	
		
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

}
