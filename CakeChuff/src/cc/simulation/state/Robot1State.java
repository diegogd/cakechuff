package cc.simulation.state;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Set;
import java.util.Vector;

import cc.simulation.elements.Sensor;
/**
 * Implementation and definition of the state of the robot 1
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Robot1State extends Observable {

	private static Robot1State _instance = null;

	// private HashMap<Float, Boolean> position_angles;

	// private float current_angle;

	private float robot_velocity = 0;

	final int INIT = 0;
	final int SUBSYSTEM1 = 1;
	final int SUBSYSTEM2 = 2;
	final int SUBSYSTEM3 = 3;
	final int TABLE = 4;
	final int PICKUPCAKE = 5;
	final int DROPINTABLE = 6;
	final int PICKUPBLISTER = 7;
	// final int DROPBLISTER = 8;
	final int PICKUPPACKET = 9;
	final int DROPINSUB3 = 10;

	int current_state, goToState;
	boolean changed_CS = false, changed_GTS = false;
	boolean _moving;// _finished = true;
	/**
	 * Constructor
	 * Initializes the state of the robot
	 */
	public Robot1State() {
		// position_angles = new HashMap<Float, Boolean>();
		// current_angle = 0;
		current_state = 0;
		_moving = false;
	}
	/**
	 * Provides the singleton class
	 * @return The unique instance of the robot
	 */
	public static synchronized Robot1State getInstance() {
		if (_instance == null) {
			_instance = new Robot1State();
		}
		return _instance;
	}
	/**
	 * Returns the velocity of the robot
	 * @return The velocity of the robot
	 */
	public float getRobot_velocity() {
		return robot_velocity;
	}
	/**
	 * Modifies the velocity of the robot
	 * @param robot_velocity The new velocity of the robot
	 */
	public void setRobot_velocity(float robot_velocity) {
		if (!getIfMoving()) {
			this.robot_velocity = robot_velocity;
			setChanged();
			notifyObservers();
		}
	}

	// public void addPosition(float angle) {
	// position_angles.put((Float) angle, false);
	// }

	// LLamada para cambiar los estados!!
	/**
	 * Makes the robot change to another state
	 * @param goToState New state of the robot
	 */
	public void setGoToState(int goToState) {
		if (!getIfMoving()) {
			//if (this.goToState != goToState) {
				this.goToState = goToState;
				this.changed_GTS = true;
				this.setMoving(true);
				// this.setFinished(false);
			//}
		}
	}
	/**
	 * Return the next state of the robot
	 * @return Next state of the robot
	 */
	public int getGoToState() {
		return goToState;
	}
	/**
	 * Makes the robot change its current state
	 * @param currentState New state of the robot
	 */
	public void setCurrentState(int currentState) {
		//if (this.current_state != currentState) {
			System.out.println("Changing State...");
			this.current_state = currentState;
			this.changed_CS = true;
			this.setMoving(false);
			// this.setFinished(true);
			setChanged();
			notifyObservers();
		//}
	}
	/**
	 * Return the current state of the robot
	 * @return Current state of the robot
	 */
	public int getCurrentState() {
		return current_state;
	}
	/**
	 * Return if the robot is moving
	 * @return True if the robot is moving, false if it is not.
	 */
	public boolean getIfMoving() {
		return _moving;
	}
	/**
	 * Modifies the variable that tells if the robot is moving
	 * @param moving New value. True:is moving;false:it is not
	 */
	public void setMoving(boolean moving) {
		if (_moving != moving) {
			_moving = moving;
		}
	}
	/**
	 * Return if the robot changes CS
	 * @return if the robot changes CS
	 */
	public boolean isChanged_CS() {
		boolean value = changed_CS;
		changed_CS = false;
		return value;
	}
	/**
	 * Return if the robot changes GTS
	 * @return if the robot changes GTS
	 */
	public boolean isChanged_GTS() {
		boolean value = changed_GTS;
		changed_GTS = false;
		return value;
	}

	// public boolean getFinished() {
	// return _finished;
	// }
	//
	// public void setFinished(boolean finished) {
	// _finished = finished;
	// }

}
