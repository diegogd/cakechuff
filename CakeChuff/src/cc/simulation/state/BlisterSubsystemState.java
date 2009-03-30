package cc.simulation.state;

import java.util.Observable;
import java.util.Vector;

import cc.simulation.elements.Sensor;

public class BlisterSubsystemState extends Observable {
	
	private static BlisterSubsystemState _instance = null;
	
	private Vector<Sensor> sensors;
	
	private float conveyor_velocity = 0;
	private float valve1_open_secs = 0;
	private float valve2_open_secs = 0;
	
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
		setChanged();
		notifyObservers();
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
