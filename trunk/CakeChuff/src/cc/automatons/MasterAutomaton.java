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
	/* state for robot*/
	
	private MasterMailbox mboxScada;
	private MasterMailbox mboxCake;
	private MasterMailbox mboxBlister;
	private MasterMailbox mboxQC;
	
	private Robot1State robot;
	public MasterAutomaton(String scada, int scada_in, int scada_out,
			String cake, int cake_in, int cake_out, String blister,
			int blister_in, int blister_out, String qc, int qc_in, int qc_out) {
		state = -1;

		// comm
		mboxScada = new MasterMailbox(this, scada_in, scada_out, scada);
		mboxCake = new MasterMailbox(this, cake_in, cake_out, cake);
		mboxBlister = new MasterMailbox(this, blister_in, blister_out, blister);
		mboxQC = new MasterMailbox(this, qc_in, qc_out, qc);

		// subscribe
		robot = Robot1State.getInstance();
		robot.addObserver(this);

	}
	@Override
	public void newMsg(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
