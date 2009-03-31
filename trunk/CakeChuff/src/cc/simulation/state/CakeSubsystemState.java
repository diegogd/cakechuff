package cc.simulation.state;

import java.util.Observable;
import java.util.Vector;

import cc.simulation.elements.Sensor;

public class CakeSubsystemState extends Observable {
	
	private static CakeSubsystemState _instance = null;
	
	private Vector<Sensor> sensors;
	
	private float conveyor_velocity = 0;
	private float valve1_open_secs = 0;
	private float valve2_open_secs = 0;
	
	public CakeSubsystemState() {
		sensors = new Vector<Sensor>();
	}
	
	public static synchronized CakeSubsystemState getInstance()
	{
		if( _instance == null ){
			_instance = new CakeSubsystemState();
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
	public float getValve1_open_secs() {
		return valve1_open_secs;
	}
	public void setValve1_open_secs(float valve1_open_secs) {
		this.valve1_open_secs = valve1_open_secs;
		setChanged();
		notifyObservers();
	}
	public void resetValve1_open_secs() {
		this.valve1_open_secs = 0;
	}
	public float getValve2_open_secs() {
		return valve2_open_secs;
	}
	public void setValve2_open_secs(float valve2_open_secs) {
		this.valve2_open_secs = valve2_open_secs;
		setChanged();
		notifyObservers();
	}
	public void resetValve2_open_secs() {
		this.valve2_open_secs = 0;
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
