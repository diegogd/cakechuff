package cc.simulation.state;

import java.util.Observable;
import java.util.Vector;

import cc.simulation.elements.Sensor;

public class BlisterSubsystemState extends Observable {
	
	private static BlisterSubsystemState _instance = null;
	
	private Vector<Sensor> sensors;
	
	private float conveyor_velocity = 0;
	private float engraver_secs = 0;
	private float cutter_secs = 0;
	
	public BlisterSubsystemState() {
		sensors = new Vector<Sensor>();
	}
	
	public static synchronized BlisterSubsystemState getInstance()
	{
		if( _instance == null ){
			_instance = new BlisterSubsystemState();
		}
		return _instance;
	}
	
	public float getConveyor_velocity() {
		return conveyor_velocity;
	}
	
	public void setConveyor_velocity(float conveyor_velocity) {
		this.conveyor_velocity = conveyor_velocity;
		
		//Approach 1: automático segun la velocidad del conveyor
		//this.engraver_secs = conveyor_velocity;
		
		setChanged();
		notifyObservers();
	}
	
	
	public float getEngraver_secs() {
		return engraver_secs;
	}
	
	//Approach 2: según se pulse el botón del engraver 
	public void setEngraver_secs(float engraver_secs) {
		this.engraver_secs = engraver_secs;
		setChanged();
		notifyObservers();
	}
	public void resetEngraver_secs() {
		this.engraver_secs = 0;
	}
	
	public float getCutter_secs() {
		return cutter_secs;
	}
	public void setCutter_secs(float cutter_secs) {
		this.cutter_secs = cutter_secs;
		setChanged();
		notifyObservers();
	}
	public void resetCutter_secs() {
		this.cutter_secs = 0;
	}
	
	
	public void addSensor(Sensor s){
		sensors.add(s);
	}
	
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
