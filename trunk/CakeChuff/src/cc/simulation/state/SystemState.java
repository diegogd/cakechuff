package cc.simulation.state;

import java.util.Observable;
import java.util.Vector;

import cc.simulation.elements.Sensor;
/**
 * Implementation and definition of the state of the system (control of cameras, blisters, cakes etc.)
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class SystemState extends Observable {
	
	private static SystemState _instance = null;
	
	private int id_camera=-1;
	private boolean dropCake = false;
	private boolean makeBlister = false;
	private boolean makePacket = false;
	/**
	 * Provides the singleton class
	 * @return The unique instance of the system state
	 */
	public static synchronized SystemState getInstance()
	{
		if( _instance == null ){
			_instance = new SystemState();
		}
		return _instance;
	}
	/**
	 * Returns the identification of the camera
	 * @return The identification of the camera
	 */
	public int getId_camera() {
		return id_camera;
	}
	/**
	 * Sets that no camera is being used
	 */
	public void resetCamera(){
		id_camera = -1;
	}
	/**
	 * Sets the camera to be used
	 * @param id_camera Identification of the camera
	 */
	public void setId_camera(int id_camera) {
		this.id_camera = id_camera;
		setChanged();
		notifyObservers();
	}			
	/**
	 * Sets that a cake is dropped and notifies the observers
	 */
	public void setDropCake() {
		this.dropCake = true;
		setChanged();
		notifyObservers();
	}
	/**
	 * Returns if a cake has been dropped.
	 * @return True if a cake has been dropped, false if not.
	 */
	public boolean dropCake() {
		if(dropCake){
			dropCake = false;
			return true;
		}
		else
			return dropCake;			
	}
	/**
	 * Sets that the blister has been made
	 */
	public void setMakeBlister() {
		this.makeBlister = true;
		setChanged();
		notifyObservers();
	}
	/**
	 * Returns if a blister has been made.
	 * @return True if a blister has been made, false if not.
	 */
	public boolean makeBlister() {
		if(makeBlister){
			makeBlister = false;
			return true;
		}
		else
			return makeBlister;			
	}
	
//	public void setMakePacket() {
//		this.makePacket = true;
//		setChanged();
//		notifyObservers();
//	}
//	
//	public boolean makePacket() {
//		if(makePacket){
//			makePacket = false;
//			return true;
//		}
//		else
//			return makePacket;			
//	}
}
