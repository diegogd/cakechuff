package cc.communications;
import java.util.Hashtable;

public class Packet {
	
	private Hashtable ht; //Hashtable to fill with 0s or 1s depending on the state

	private String transmitter;

	private String receiver;

	

	//Depending of the automaton or if its the SCADA, the hashtable will be initialized in different ways

	public Packet(int option){

		

		ht=new Hashtable();

		transmitter = "";

		receiver = "";

		//example if(option==) ht.put("stop","0");ht.put("emergency_stop","0");

	}



	public String  getValue(String key){

		return(ht.get(key).toString());

	}

	

	public void setValue(String key, String value){

		ht.put(key,value);

	}



	
	//Depending on the option (automaton number or SCADA) it will print different things 
	public void show(int option) {


		 

	 	

	 }

	 

	 public String getTransmitter() {

		return transmitter;

	}



	public void setTransmitter(String trans) {

		transmitter = trans;

	}

	

	public String getReceiver() {

		return receiver;

	}



	public void setReceiver(String rec) {

		receiver = rec;

	}

}
