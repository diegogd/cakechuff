package cc.communications;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import cc.automatons.MasterAutomaton;
import cc.simulation.state.CakeSubsystemState;

public class MasterMailbox implements Runnable{

	private Mailbox mbox;
	private Thread receiving;
	private MasterAutomaton owner;
	private Socket sout;
	private DataOutputStream dout;
	private int portin, portout;
	private String destination;
	Vector<String> msgs;
	public MasterMailbox(MasterAutomaton owner, int portin, int portout, String address){
		this.portin=portin;
		this.portout=portout;
		this.destination=address;
		try{
			mbox= new Mailbox(owner, portin);
			(new Thread(mbox)).start();
			//outgoing connection will be opened when the first message is sent
		}catch(UnknownHostException uhe){
			
		}catch(IOException ioe){
			
		}catch(SecurityException se){
			
		}
		
	}
	public void run(){
		if(!sout.isConnected()){
			connect();
		}
		while (msgs.size()>0){
			try{
				dout.writeChars(msgs.firstElement());
				msgs.removeElementAt(0);
			}catch(IOException ioe){
				//restart the receiving connection
				if(!mbox.isFailure())mbox.setFailure(true);
				connect();
			}
		}
	}
	public synchronized void send(String msg){
		msgs.add(msg);
		(new Thread(this)).start();
	}
	private void connect(){
		boolean success=false;
		while(!success){
			try{
				sout=new Socket(destination,portout);
				success=true;
				dout=new DataOutputStream(sout.getOutputStream());
			}catch(IOException ioe){
				
			}
		}
	}
}
