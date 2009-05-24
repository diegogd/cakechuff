package cc.simulation.state;

import java.util.Observable;
import java.util.Vector;

import cc.simulation.elements.Sensor;
/**
 * Implementation and definition of the state of the blister subsystem
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class BlisterSubsystemState extends Observable {
	
	private static BlisterSubsystemState _instance = null;
	
	private Vector<Sensor> sensors;
	
	private float conveyor_velocity = 0;
	private float engraver_secs = 0;
	private float cutter_secs = 0;
	/**
	 * Constructor
	 * Initializes a vector with the sensors of the blister subsystem
	 */
	public BlisterSubsystemState() {
		sensors = new Vector<Sensor>();
	}
	/**
	 * Returns an instance of the blister subsystem state
	 * @return Instance of the blister subsystem state
	 */
	public static synchronized BlisterSubsystemState getInstance()
	{
		if( _instance == null ){
			_instance = new BlisterSubsystemState();
		}
		return _instance;
	}
	/**
	 * Returns the velocity of the conveyor
	 * @return Velocity of the conveyor
	 */
	public float getConveyor_velocity() {
		return conveyor_velocity;
	}
	/**
	 * Modifies the velocity of the conveyor
	 * @param conveyor_velocity New value for the velocity of the conveyor
	 */
	public void setConveyor_velocity(float conveyor_velocity) {
		this.conveyor_velocity = conveyor_velocity;
		
		//Approach 1: automático segun la velocidad del conveyor
		//this.engraver_secs = conveyor_velocity;
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Returns the time the engraver is working
	 * @return Time the engraver is working
	 */
	public float getEngraver_secs() {
		return engraver_secs;
	}
	
	//Approach 2: según se pulse el botón del engraver
	/**
	 * Modifies the time of the engraver (used when the botton of the engraver is pushed)
	 * @param engraver_secs New time value 
	 */
	public void setEngraver_secs(float engraver_secs) {
		this.engraver_secs = engraver_secs;
		setChanged();
		notifyObservers();
	}
	/**
	 * Modifies the seconds of the engraver to zero
	 */
	public void resetEngraver_secs() {
		this.engraver_secs = 0;
	}
	/**
	 * Returns the time the cutter is working
	 * @return Time the cutter is working
	 */
	public float getCutter_secs() {
		return cutter_secs;
	}
	/**
	 * Modifies the time of the cutter
	 * @param cutter_secs New time value 
	 */
	public void setCutter_secs(float cutter_secs) {
		this.cutter_secs = cutter_secs;
		setChanged();
		notifyObservers();
	}
	/**
	 * Modifies the seconds of the cutter to zero
	 */
	public void resetCutter_secs() {
		this.cutter_secs = 0;
	}
	
	/**
	 * Inserts a new sensor in the blister subsystem
	 * @param s Sensor to be inserted
	 */
	public void addSensor(Sensor s){
		sensors.add(s);
	}
	/**
	 * Checks for any changes in the sensors the subsystem has. In case a change is detected observers are notified.
	 */
	public void checkSensorsChanges(){
		for(int i=0; i< sensors.size(); i++)
		{
			if(sensors.get(i).isModified())
			{
				setChanged();
				notifyObservers(sensors.get(i));
			}
		}
	}
		
}
