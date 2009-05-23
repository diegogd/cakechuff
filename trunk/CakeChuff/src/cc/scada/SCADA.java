//TODO: NullPointer in newMsg??
package cc.scada;

import cc.simulation.state.ControlInterface;
import java.io.*;
import java.net.Socket;
import java.util.*;
import cc.communications.ScadaMailbox;

/**
 * Implementation of the Supervisory Control And Data Acquisition of the CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class SCADA {
	
	private ScadaMailbox _mailbox;
	private Socket sout;
	//private DataOutputStream dout;
	private PrintWriter dout;
	
	//The master keeps listening on port 9009
	private int portout = 9009;
	//Scada keeps listening on port 9008
	private int portscada = 9008;
	private String masterAddress;
	
	private ControlInterface _ci;

	/**
	 * Constructor
	 * Establish the communication through a mailbox with the Master Automaton
	 * Instantiates the Graphical User Interface
	 * @param ci Graphical User Interface 
	 * @exception IOException Error establishing the communication through the mailbox
	 */
	public SCADA (ControlInterface ci){
		_ci= ci;
		
		masterAddress="localhost";
		boolean connected=false;
		while (!connected) {
			try {

				sout = new Socket(masterAddress, portout);
				_mailbox = new ScadaMailbox(this, portscada);
				(new Thread(_mailbox)).start();
				// dout = new DataOutputStream(sout.getOutputStream());
				dout = new PrintWriter(sout.getOutputStream(), true);
				connected = true;
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("Connecting...");
			}
		}
	}
	
	/**
	 * Recover information from the subsystems
	 * @param key Name of the tag in the .xml file to be recover 
	 * (defaultSpeed, conveyorSpeed, defaultv1, defaultv2, v1, v2, state) 
	 * @param subsystem Name of the subsystem (ss1Info, ss2Info, ss3Info, robot)
	 * @return value Value of the tag
	 */
	public String getValue(String key, String subsystem){

		//Obtain the file
		File f = new File ("database/"+ subsystem+ ".xml");
		LinkedHashMap h = XMLManager.getInstance().read(f);
		String value = h.get(key).toString();

		return value;
	}
	
	/**
	 * Recover information from the statistics file.
	 * @param key Name of the tag in the .xml file to be recovered 
	 * (procesedPackages, faultyPackages, totalCakes)
	 * @return value Value of the tag
	 */
	public String getStatistics(String key){

		File f = new File ("database/statistics.xml");
		LinkedHashMap h = XMLManager.getInstance().read(f);
		String value = h.get(key).toString();

		return value;
	}
	
	/**
	 * Recover the information from the subsystems
	 * @param subsystem Name of the subsystem (ss1Info, ss2Info, ss3Info, robot)
	 * @param key Name of the tag in the .xml file to be recover 
	 * (defaultSpeed, conveyorSpeed, defaultv1, defaultv2, v1, v2, state, robot:pickblister, robot:move_place_cake) 
	 * @param value Value of the tag
	 */
	public void setValues(String subsystem, String key, String value){
		File f = new File ("database/"+ subsystem+ ".xml");
		LinkedHashMap h = XMLManager.getInstance().read(f);
		//the value is replaced in the hashmap
		if (h.containsKey(key)==false){
			System.out.println("Error: there is no key " + key + "in the statistics file");
		}else{
			h.put(key, value);
			XMLManager.getInstance().generate(subsystem, "database/"+ subsystem+ ".xml", h);
		}
	}
	
	/**
	 * Set the information in the statistics file
	 * @param key Name of the tag in the .xml file to be set 
	 * (procesedPackages, faultyPackages, total_ko_cakes, total_ok_cakes,
	 *  start, emergency_stops, stops)
	 * @param value Value of the tag to be set
	 */
	public void setStatistics(String key, String value){
		File f = new File ("database/statistics.xml");
		LinkedHashMap h = XMLManager.getInstance().read(f);
		//the value is replaced in the hashmap
		if (h.containsKey(key)==false){
			System.out.println("Error: there is no key " + key + "in the statistics file");
		}else{
			h.put(key, value);
			XMLManager.getInstance().generate("statistics", "database/statistics.xml", h);
		}
	}
	
	/**
	 * Compress all the information of one automaton into a string message, 
	 * to be sent to the automaton
	 * @return send String with all the data split by the symbol "#"
	 */
	public String sendAutomatonInfo1(){
		String send = "";

		send = this.getValue("cakesCapacity", "ss1Info") + "#" +
				this.getValue("conveyorSpeed", "ss1Info") + "#" +
				this.getValue("conveyorLenght", "ss1Info") + "#" +
				this.getValue("v1", "ss1Info")+ "#"+
				this.getValue("v2", "ss1Info");

		return send;
	}
	
	/**
	 * Inform about the speed of the conveyor belt for the automaton 2
	 * @return String containing the speed value 
	 */
	public String sendAutomatonInfo2(){
		return this.getValue("conveyorSpeed", "ss2Info") + "#" +
		this.getValue("conveyorLenght", "ss2Info");
	}

	/**
	 * Inform about the state of the robot 
	 * @return String containing the information of the robot 
	 */	
	private String sendRobotInfo() {
		return this.getValue("pick_blister", "robot") + "#" +  this.getValue("move_place_cake", "robot");
	}
	
    /**
	 * Inform about the data of the automaton 3
     * @return send String containing the information of the automaton 3
     */
	public String sendAutomatonInfo3(){
		String send = "";

		send = this.getValue("conveyorSpeed", "ss3Info") + "#" +
    	this.getValue("conveyorLenght", "ss3Info") + "#" +
    	this.getValue("failure_range", "ss3Info")+ "#"+
    	this.getValue("time_pack", "ss3Info")+ "#"+
    	this.getValue("pick_blister", "ss3Info");

    	return send;
	}
	
	/**
	 * Compress all the initial info needed by the CakeChuff system
	 * Begin with the word INIT, follow by ":" and all the data of the subsystems,
	 * separed by "$"
	 * The different field for each subsystem are divided by "#"
	 * The order is A1(numcakes#speed#lenght#choc#caramel), A2(speed#lenght),
	 * A3(speed#lenght#failurerate#timepack#robot), R1 (pick#move)
	 * @return s String containing all the initial info
	 */
	public String sendInitInfo(){
		String s = "INIT" + ":" + this.sendAutomatonInfo1() + "$" + 
		this.sendAutomatonInfo2() + "$" + 
		this.sendAutomatonInfo3() + "$" +
		this.sendRobotInfo();
		return s;
	}
  
	/**
	 * Compress all the info needed to recover the CakeChuff system
	 * Begin with the word RESET, follow by ":" and all the data of the subsystems,
	 * separed by "$"
	 * The different field for each subsystem are divided by "#", and the 
	 * first one is the state where the subsystem was stopped
	 * The order is A1(state#numcakes#speed#length#choc#caramel), A2(state#speed#length),
	 * A3(state#s_robot#speed#length#failurerate#timepack#robot), R1 (state#pick#move) 
	 * @return s String containing all the recovery info
	 */
	public String sendResetInfo(){
		String s = "RESET" + ":" + this.getValue("state", "ss1Info") + "#" + this.sendAutomatonInfo1() + "$" + 
		this.getValue("state", "ss2Info") + "#" + this.sendAutomatonInfo2() + "$" + 
		this.getValue("state", "ss3Info") + "#" + this.getValue("robot_state", "ss3Info") + "#" +this.sendAutomatonInfo3() + "$" +
		this.getValue("state", "robot") + "#" + this.sendRobotInfo();
		return s;
	}
        
    /**
     * Compress all the info needed to restart one subsystem, 
     * in response a petition from the Master automaton
     * @return s String containing the info needed to restart one subsystem
     */
    public String sendRestartInfo(String subsystem_name){
    	String s = "RESTART" + ":" + subsystem_name + "$";
       	if (subsystem_name.compareTo("A1") ==0){
       		s = s +this.getValue("state", "ss1Info") + "#" + this.sendAutomatonInfo1();
        }else if(subsystem_name.compareTo("A2") ==0){
        	s = s + this.getValue("state", "ss2Info") + "#" + this.sendAutomatonInfo2();
        }else if (subsystem_name.compareTo("A3") ==0){
        	s = s + this.getValue("state", "ss3Info") + "#" +  
        	this.getValue("robot_state", "ss3Info") + "#"+ this.sendAutomatonInfo3();
        }else if(subsystem_name.compareTo("R1") ==0){
        	s = s +this.getValue("state", "robot") + "#" + this.sendRobotInfo() ;
        }
        return s;
    }
        
    /**
      * Compress a STOP message to be sent to the master automaton
      * @return A The stop message
      */
    public String stop(){
    	return "STOP";  
    }
        
    /**
     * Compress a EMERGENCY STOP message to be sent to the master automaton
     * @return A The emergency stop message
     */
    public String emergencyStop(){
    	return "EMERGENCY";  
    }
    
    /**
     * Receive a message from the master automaton with the info about the
     * change in the state of a subsystem
     * Check the subsystem which belongs the info
     * Change the state in the database 
     * @param info Subsystem_name +  ":" + state
     * @return false If the info does not correspond to any subsystem
     */
    public boolean stateChanged(String info){     
    	boolean ok = true;
        String [] array = info.split(":");   
        if (array[0].compareTo("A1") ==0){
        	this.setValues("ss1Info", "state", array[1]);
        }else if(array[0].compareTo("A2") ==0){
        	this.setValues("ss2Info", "state", array[1]); 
        }else if (array[0].compareTo("A3") ==0){
        	this.setValues("ss3Info", "state", array[1]);
            //When we have the result of the quality control
            //we have to modify the statistics
            if(array[1].compareTo("ko_mov") ==0){
            	//it's a faulty blister
            	int prevValue = Integer.parseInt(this.getStatistics("faultyPackages"));
                prevValue++;
                this.setStatistics("faultyPackages", prevValue + "");
                prevValue = Integer.parseInt(this.getStatistics("total_ko_cakes"));
                this.setStatistics("total_ko_cakes", (prevValue +Integer.parseInt(array[2])) + "");
                _ci.updateStadistics();
            }else if (array[1].compareTo("qc_stamp") ==0){
            	//it's a correct one
                int prevValue = Integer.parseInt(this.getStatistics("procesedPackages"));
                prevValue++;
                this.setStatistics("procesedPackages", prevValue + "");
                prevValue = Integer.parseInt(this.getStatistics("total_ok_cakes"));
                this.setStatistics("total_ok_cakes", (prevValue +4) + "");
                _ci.updateStadistics();
            }
        }else if(array[0].compareTo("R1") ==0){
        	this.setValues("robot", "state", array[1]); 
        }else{
            ok = false; 
        }
        return ok;
    }

    /**
     * Invoke by the mailbox when a new message arrives
     * @param msg Message arrived
     */
    public synchronized void newMsg(String msg) {
    	String [] array = msg.split(":");
		//The only thing that Scada receives from the master 
		// is a change in the state, or a petition to restart.
		if (array[0].compareTo("RECOVER") ==0){
			this.sendtoMaster(sendRestartInfo(array[1]));
        }else{
        	if (!this.stateChanged(msg)){
        		System.out.println("Something wrong changing the state for " + array[1]);
            } 	
        }	
    }

    /**
     * Send a message to the master
	 * @param msg Message to be sent
	 * @exception IOException Communication failure
	 */
	public void sendtoMaster(String msg){
		try{
			dout.println(msg);
		}catch(Exception e){
			e.printStackTrace();
			//restart the receiving connection
			if(!_mailbox.isFailure())_mailbox.setFailure(true);
			//connection failure
				boolean success=false;
				while(!success){
					try{
						sout= new Socket("master", portout);
						dout = new PrintWriter(sout.getOutputStream(),true);
					}catch(IOException ioe2){
						//keep trying
				}
			}
		}
	}
		
	/**
	 * Reset all the default values of the CakeChuff system
     */
	public void resetDB(){
		String defaultval = null;

		//Automaton 1
        defaultval = this.getValue("defaultSpeed", "ss1Info");
        this.setValues("ss1Info", "conveyorSpeed", defaultval);

        defaultval = this.getValue("defaultLenght","ss1Info");
        this.setValues("ss1Info", "conveyorLenght", defaultval);

        defaultval = this.getValue("defaultv1", "ss1Info");
        this.setValues("ss1Info", "v1", defaultval);

        defaultval = this.getValue("defaultv2", "ss1Info");
        this.setValues("ss1Info", "v2", defaultval);

        defaultval = this.getValue("defaultCapacity", "ss1Info");
        this.setValues("ss1Info", "cakesCapacity", defaultval);

        //Automaton 2
        defaultval = this.getValue("defaultSpeed", "ss2Info");
        this.setValues("ss2Info", "conveyorSpeed", defaultval);

        defaultval = this.getValue("defaultLenght", "ss2Info");
        this.setValues("ss2Info", "conveyorLenght", defaultval);

        //Automaton 3
        defaultval = this.getValue("defaultSpeed", "ss3Info");
        this.setValues("ss3Info", "conveyorSpeed", defaultval);

        defaultval = this.getValue("defaultLenght", "ss3Info");
        this.setValues("ss3Info", "conveyorLenght", defaultval);

        defaultval = this.getValue("defaultTime", "ss3Info");
        this.setValues("ss3Info", "time_pack", defaultval);

        defaultval = this.getValue("defaultRange", "ss3Info");
        this.setValues("ss3Info", "failure_range", defaultval);

        defaultval = this.getValue("defaultTimeRobot", "ss3Info");
        this.setValues("ss3Info", "pick_blister", defaultval);

        //Robot
        defaultval = this.getValue("defaultPick", "robot");
        this.setValues("robot", "pick_blister", defaultval);

        defaultval = this.getValue("defaultMove", "robot");
        this.setValues("robot", "move_place_cake", defaultval);
      
	}
	
}
