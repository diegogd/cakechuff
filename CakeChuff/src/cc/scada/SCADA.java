package cc.scada;

import java.io.*;
import java.net.Socket;
import java.util.*;
import cc.communications.ScadaMailbox;

public class SCADA {
	
	private ScadaMailbox _mailbox;
	private Socket sout;
	//private DataOutputStream dout;
	private PrintWriter dout;
	
	//The masters keeps listening on port 9000
	private int portout = 9000;
	//Scada keeps listening on port 9009
	private int portscada = 9009;
	private String masterAddress;

	public SCADA (){
		masterAddress="localhost";
		boolean connected=false;
		while (!connected) {
			try {

				sout = new Socket(masterAddress, portout);
				_mailbox = new ScadaMailbox(this, portscada);
				// dout = new DataOutputStream(sout.getOutputStream());
				dout = new PrintWriter(sout.getOutputStream(), true);
				connected = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("Connecting...");
			}
		}
	}
	
	/**
	 * This method is to recover the information of the subsystems.
	 * @param key The name of the value in the xml file that we want to 
	 * recover. This can be:
	 *          defaultSpeed
	 *          conveyorSpeed
	 *          defaultv1
	 *          defaultv2
	 *          v1
	 *          v2
	 *          state (this one for the robot also)
	 * @param subsystem the names can be:
	 * 		    ss1Info
	 * 			ss2Info
	 * 			ss3Info
	 * 			robot
	 * @return the value interested in
	 */
	public String getValue(String key, String subsystem){

		//Obtain the file
		File f = new File ("database/"+ subsystem+ ".xml");
		LinkedHashMap h = XMLManager.getInstance().read(f);
		String value = h.get(key).toString();

		return value;
	}
	
	/**
	 * This method is to recover the information in the statistics file.
	 * @param key The name of the value in the xml file that we want to 
	 * recover. This can be:
	 *          procesedPackages
	 *          faultyPackages
	 *          totalCakes
	 * @return the value 
	 */
	public String getStatistics(String key){

		File f = new File ("database/statistics.xml");
		LinkedHashMap h = XMLManager.getInstance().read(f);
		String value = h.get(key).toString();

		return value;
	}
	
	/**
	 * This method is to recover the information of the subsystems.
	 * @param subsystem the names can be:
	 * 		    ss1Info
	 * 			ss2Info
	 * 			ss3Info
         *                  robot
	 * @param key The name of the value in the xml file that we want to 
	 * recover. This can be:
	 *          defaultSpeed
	 *          conveyorSpeed
	 *          defaultv1
	 *          defaultv2
	 *          v1
	 *          v2
	 * @param value
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
	 * This method is to set the information in the statistics file.
	 * @param key The name of the value in the xml file that we want to 
	 * recover. This can be:
	 *          procesedPackages
	 *          faultyPackages
	 *          total_ko_cakes
	 *          total_ok_cakes
	 *          start
	 *          emergency_stops
	 *          stops
	 * @param value 
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
	 * Method that put all the information of one automaton into a string 
	 * message, to be sent to this automaton.
	 * @return A string with all the data split by the symbol "#"
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
	 * Method that inform about the speed of the conveyor
	 * for automaton 2. 
	 * @return A string 
	 */
	public String sendAutomatonInfo2(){
		return this.getValue("conveyorSpeed", "ss2Info") + "#" +
		this.getValue("conveyorLenght", "ss2Info");
	}

	private String sendRobotInfo() {
		return this.getValue("pick_blister", "robot") + "#" +  this.getValue("move_place_cake", "robot");
	}
	
     /**
      * Method that inform about the the data for automaton 3.
      * @return A string with the speed
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
	 * Method that send the initial info for cakechuff. Begin with the word
	 * INIT, follow by ":" and all the data of the subsystems, separed by
	 * "$". The different field for each subsystem are divided by "#".
	 * The order is A1(numcakes#speed#lenght#choc#caramel), A2(speed#lenght),
	 * A3(speed#lenght#failurerate#timepack#robot), R1 (pick#move). 
	 * @return A string 
	 */
	public String sendInitInfo(){
		String s = "INIT" + ":" + this.sendAutomatonInfo1() + "$" + 
		this.sendAutomatonInfo2() + "$" + 
		this.sendAutomatonInfo3() + "$" +
		this.sendRobotInfo();
		return s;
	}
  
	/**
	 * Method that send the info to recover the system. Begin with the word
	 * RESET, follow by ":" and all the data of the subsystems, separed by
	 * "$". The different field for each subsystem are divided by "#", and the
	 * first one is the state where the subsystem was stopped.
	 * The order is A1(state#numcakes#speed#lenght#choc#caramel), 
	 * A2(state#speed#lenght),A3(state#s_robot#speed#lenght#failurerate#timepack#robot),
	 * R1 (state#pick#move). 
	 * @return A string 
	 */
	public String sendResetInfo(){
		String s = "RESET" + ":" + this.getValue("state", "ss1Info") + "#" + this.sendAutomatonInfo1() + "$" + 
		this.getValue("state", "ss2Info") + "#" + this.sendAutomatonInfo2() + "$" + 
		this.getValue("state", "ss3Info") + "#" + this.getValue("robot_state", "ss3Info") + "#" +this.sendAutomatonInfo3() + "$" +
		this.getValue("state", "robot") + "#" + this.sendRobotInfo();
		return s;
	}
        
        /**
         * Method that send the info to restart one subsystem, in response a 
         * petitions from the Master automaton. 
         * @return A string 
         */
        public String sendRestartInfo(String subsystem_name){
        	String s = "RESTART" + ":" + subsystem_name + "$";

        	if (subsystem_name.compareTo("A1") ==0){
        		s.concat(this.getValue("state", "ss1Info") + "#" + this.sendAutomatonInfo1());
        	}else if(subsystem_name.compareTo("A2") ==0){
        		s.concat(this.getValue("state", "ss2Info") + "#" + this.sendAutomatonInfo2());
        	}else if (subsystem_name.compareTo("A3") ==0){
        		s.concat(this.getValue("state", "ss3Info") + "#" +  
        				this.getValue("robot_state", "ss3Info") + "#"+ this.sendAutomatonInfo3());
        	}else if(subsystem_name.compareTo("R1") ==0){
        		s.concat(this.getValue("state", "robot") + "#" + this.sendRobotInfo());
        	}

        	return s;
        }
        
        /**
         * Method that send the message to master automaton to stop the system
         * @return A string 
         */
        public String stop(){
        	return "STOP";  
        }
        
        /**
         * Method that send the message to master automaton to stop the system
         * immediately.
         * @return A string 
         */
        public String emergencyStop(){
        	return "EMERGENCY";  
        }

        /**
         * Method to receive the message from the master with the info about
         * the change in the state of a subsystem. Checks the subsystem 
         * who belongs the info, and change the state in the database. 
         * @param info Subsystem_name +  ":" + state
         * @return false if the info do not corresponds to any subsystem
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
            }else if(array[0].compareTo("R1") ==0){
                this.setValues("robot", "state", array[1]); 
            }else{
                ok = false; 
            }
            
            return ok;
        }

        /**
         * Method invoke by the mailbox when a new message arrives
         * @param msg The new message
         */
		public synchronized void newMsg(String msg) {
			String [] array = msg.split(":");
			
			//The only thing that Scada receives from the master 
			// is a change in the state, or a petition to restart.
			if (array[0].compareTo("RESTART") ==0){
               this.sendtoMaster(sendRestartInfo(array[1]));
            }else{
            	if (!this.stateChanged(msg)){
            		System.out.println("Something wrong changing the state for " + array[1]);
            	} 	
            }
			
		}

		/**
		 * Method to send a message to the master
		 * @param msg The message to be sent
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
					//dout = new DataOutputStream(sout.getOutputStream());
					dout = new PrintWriter(sout.getOutputStream(),true);
					}catch(IOException ioe2){
						//keep trying
					}
				}
			}
			
	}
		
		/**
         * Method to reset all the default values of CakeChuff
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
