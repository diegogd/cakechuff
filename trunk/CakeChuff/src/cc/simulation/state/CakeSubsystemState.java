package cc.simulation.state;

import java.util.Observable;
import java.util.Vector;

import cc.simulation.elements.Sensor;
/**
 * Implementation and definition of the state of the cake subsystem
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class CakeSubsystemState extends Observable {
	
	private static CakeSubsystemState _instance = null;
	
	private Vector<Sensor> sensors;
	
	private float conveyor_velocity = 0;
	private float valve1_open_secs = 0;
	private float valve2_open_secs = 0;
	
	/**
	 * Constructor of the class, it creates the vector of sensors.
	 */
	public CakeSubsystemState() {
		sensors = new Vector<Sensor>();
	}
	
	/**
	 * Provides the singleton class
	 * @return The unique instance of CakeSubsystem
	 */
	public static synchronized CakeSubsystemState getInstance()
	{
		if( _instance == null ){
			_instance = new CakeSubsystemState();
		}
		return _instance;
	}
	
	/**
	 * Returns the current conveyor velocity.
	 * @return Current conveyor velocity.
	 */
	public float getConveyor_velocity() {
		return conveyor_velocity;
	}
	
	/**
	 * Set a New Velocity to the conveyor.
	 * @param conveyor_velocity Velocity of the conveyor
	 */
	public void setConveyor_velocity(float conveyor_velocity) {
		this.conveyor_velocity = conveyor_velocity;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Return the seconds that the conveyor is still opened.
	 * @return The seconds that the conveyor is still opened.
	 */
	public float getValve1_open_secs() {
		return valve1_open_secs;
	}
	/**
	 * Modifies the seconds that the valve 1 is still opened.
	 * @param valve1_open_secs The new value for the seconds that the valve 1 is still opened.
	 */
	public void setValve1_open_secs(float valve1_open_secs) {
		this.valve1_open_secs = valve1_open_secs;
		setChanged();
		notifyObservers();
	}
	/**
	 * Modifies the seconds that the valve 1 is still opened to zero.
	 */
	public void resetValve1_open_secs() {
		this.valve1_open_secs = 0;
	}
	/**
	 * Return the seconds that the valve 2 is still opened.
	 * @return The seconds that the valve 2 is still opened.
	 */
	public float getValve2_open_secs() {
		return valve2_open_secs;
	}
	/**
	 * Modifies the seconds that the valve 2 is still opened.
	 * @param valve2_open_secs The new value for the seconds that the valve 2 is still opened.
	 */
	public void setValve2_open_secs(float valve2_open_secs) {
		this.valve2_open_secs = valve2_open_secs;
		setChanged();
		notifyObservers();
	}
	/**
	 * Modifies the seconds that the valve 1 is still opened to zero.
	 */
	public void resetValve2_open_secs() {
		this.valve2_open_secs = 0;
	}
	/**
	 * Inserts a new sensor in the cake subsystem
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
