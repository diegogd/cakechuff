package cc.simulation.elements;

import java.util.Observable;

import com.jme.scene.Node;
/**
 * Implementation and definition of the sensors, one of
 * the simulation elements that compose CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Sensor extends Node {

	private boolean isActived = false;
	private boolean modified = false;
	/**
	 * Returns if the sensor has been activated
	 * @return True if the robot has been activated, false it has not.
	 */
	public boolean isActived() {
		return isActived;
	}
	/**
	 * Modifies the state of activity of the robot
	 * @param isActived True: it is active, false: not active
	 */
	public void setActived(boolean isActived) {
		if( this.isActived == isActived )
			modified = false;
		else {
			modified = true;
			this.isActived = isActived;
		}
	}
	/**
	 * Returns if the sensor has been modified
	 * @return True if the robot has been modified, false it has not.
	 */
	public boolean isModified() {
		return modified;
	}
	/**
	 * Modifies the state of modification of the robot
	 * @param modified True: it is modified, false: not modified
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
	}
}
