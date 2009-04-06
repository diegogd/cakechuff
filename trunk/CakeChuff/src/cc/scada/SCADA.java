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
	 * @param subsystem the names can be:
	 * 		    ss1Info
	 * 			ss2Info
	 * 			ss3Info
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
	 *          totalCakes
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

}
