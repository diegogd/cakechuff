package cc.scada;
import java.io.*;
import java.util.*;

public class SCADA {
	

	public SCADA (){

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
		File f = new File ("docs/"+ subsystem+ ".xml");
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

		File f = new File ("docs/statistics.xml");
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
		File f = new File ("docs/"+ subsystem+ ".xml");
		LinkedHashMap h = XMLManager.getInstance().read(f);
		//the value is replaced in the hashmap
		if (h.containsKey(key)==false){
			System.out.println("Error: there is no key " + key + "in the statistics file");
		}else{
			h.put(key, value);
			XMLManager.getInstance().generate(subsystem, "docs/"+ subsystem+ ".xml", h);
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
		File f = new File ("docs/statistics.xml");
		LinkedHashMap h = XMLManager.getInstance().read(f);
		//the value is replaced in the hashmap
		if (h.containsKey(key)==false){
			System.out.println("Error: there is no key " + key + "in the statistics file");
		}else{
			h.put(key, value);
			XMLManager.getInstance().generate("statistics", "docs/statistics.xml", h);
		}
	}
	
	/**
	 * Method that put all the information of one automaton into a string 
	 * message, to be sent to this automaton.
	 * @return A string with all the date split by the symbol "#"
	 */
	public String sendAutomatonInfo1(){
		String send = "";
		
		send = this.getValue("conveyorSpeed", "ss1Info") + "#" +
		       this.getValue("v1", "ss1Info")+ "#"+
		       this.getValue("v2", "ss1Info");
		
		return send;
	}
	
	/**
	 * Method that inform about the speed of the conveyor
	 * for automaton 2. 
	 * @return A string with the speed
	 */
	public String sendAutomatonInfo2(){
 		return this.getValue("conveyorSpeed", "ss2Info");
	}
	
	/**
	 * Method that inform about the speed of the conveyor
	 * for automaton 3. 
	 * @return A string with the speed
	 */
	public String sendAutomatonInfo3(){
 		return this.getValue("conveyorSpeed", "ss3Info");
	}
	
	public String sendInitInfo(){
		String s = this.sendAutomatonInfo1() + "$" + 
				this.sendAutomatonInfo2() + "$" + 
				this.sendAutomatonInfo3();
		return s;
	}
	
}
