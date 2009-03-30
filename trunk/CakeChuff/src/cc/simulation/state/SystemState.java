package cc.simulation.state;

import java.util.Observable;
import java.util.Vector;

import cc.simulation.elements.Sensor;

public class SystemState extends Observable {
	
	private static SystemState _instance = null;
	
	private int id_camera=0;	
	
	public static synchronized SystemState getInstance()
	{
		if( _instance == null ){
			_instance = new SystemState();
		}
		return _instance;
	}

	public int getId_camera() {
		return id_camera;
	}

	public void setId_camera(int id_camera) {
		this.id_camera = id_camera;
		setChanged();
		notifyObservers();
	}			
}
