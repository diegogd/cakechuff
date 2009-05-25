package cc.simulation.state;

import java.util.Observable;
import java.util.Vector;

import cc.simulation.elements.Sensor;

/**
 * Implementation and definition of the state of the quality subsystem
 * 
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class QualitySubsystemState extends Observable {

	private static QualitySubsystemState _instance = null;

	private Vector<Sensor> sensors;

	private float conveyor_velocity = 0;
	private boolean quality_check = false;
	private float wrapper_secs = 0;
	private boolean wrapup = false;
	private int numcakes_passed = 0;

	public final int INIT = 0;
	public final int SUBSYSTEM = 1;
	public final int GOODBOX = 2;
	public final int BADBOX = 3;
	public final int PICKUPPACKET = 4;
	public final int DROPGOODBOX = 5;
	public final int DROPBADBOX = 6;

	int robot_current_state, robot_goToState;
	boolean robot_moving, robot_changed_GTS, robot_changed_CS;
	private float robot_velocity;

	private float wrapper_pause_secs; 

	/**
	 * Constructor It creates the vector of sensors and initializes the state,
	 * movement and velocity of the robot.
	 */
	public QualitySubsystemState() {
		sensors = new Vector<Sensor>();
		robot_current_state = 0;
		robot_moving = false;
		robot_velocity = 0;
		wrapper_pause_secs = 0;
	}

	/**
	 * Provides the singleton class
	 * 
	 * @return The unique instance of the quality Subsystem
	 */
	public static synchronized QualitySubsystemState getInstance() {
		if (_instance == null) {
			_instance = new QualitySubsystemState();
		}
		return _instance;
	}

	/**
	 * Returns the current conveyor velocity.
	 * 
	 * @return Current conveyor velocity.
	 */
	public float getConveyor_velocity() {
		return conveyor_velocity;
	}

	/**
	 * Set a New Velocity to the conveyor.
	 * 
	 * @param conveyor_velocity
	 *            Velocity of the conveyor
	 */
	public void setConveyor_velocity(float conveyor_velocity) {
		this.conveyor_velocity = conveyor_velocity;
		setChanged();
		notifyObservers();
	}

	/**
	 * Returns if the quality check has been made
	 * 
	 * @return True if the quality check has been made, false if it has not
	 */
	public boolean getQualityCheck() {
		return quality_check;
	}

	/**
	 * Modifies if the quality check has been made
	 * 
	 * @param quality
	 *            True to set that the quality check has been made
	 */
	public void setQualityCheck(boolean quality) {
		// if (quality != quality_check) {
		this.quality_check = quality;
		numcakes_passed = 0;
		setChanged();
		notifyObservers(quality);
		// }
	}

	/**
	 * Sets that the quality check has not been made
	 */
	public void resetQualityCheck() {
		this.quality_check = false;
		// numcakes_passed = 0;
	}

	/**
	 * Returns the number of cakes that passed the quality check
	 * 
	 * @return The number of cakes that passed the quality check
	 */
	public int getIfQualityPassed() {
		return numcakes_passed;
	}

	// Esta funcion es interna (no se deberia de usar en la logica)
	/**
	 * Auxiliary function (and not used). Sets the number of cakes that passed
	 * the quality check
	 * 
	 * @param numcakes
	 *            The number of cakes that passed the quality check
	 */
	public void setIfQualityPassed(int numcakes) {
		numcakes_passed = numcakes;
	}

	// *+++++++++++++++
	/**
	 * Returns if the wrapping has been made
	 * 
	 * @return True: Wrapping done; False: Wrapping not done
	 */
	public boolean getWrappedUp() {
		return wrapup;
	}

	/**
	 * Modifies if the wrapping has been made and notifies the observers
	 * 
	 * @param wrapup
	 *            True to set that wrapping has been made
	 */
	public void setWrappedUp(boolean wrapup) {
		this.wrapup = wrapup;
		setChanged();
		if (wrapup)
			notifyObservers(1);
		else
			notifyObservers(0);
	}

	/**
	 * Sets that the wrapping has not been made
	 */
	public void resetWrappedUp() {
		this.wrapup = false;

	}

	// /*+++++++++++
	/**
	 * Return the seconds of the wrapper
	 * 
	 * @return The seconds of the wrapper
	 */
	public float getWrapper_secs() {
		return wrapper_secs;
	}

	/**
	 * Sets the seconds of the wrapper
	 * 
	 * @param wrapper_secs
	 *            The new time elapsed of the wrapper
	 */
	public void setWrapper_secs(float wrapper_secs) {
		this.wrapper_secs = wrapper_secs;
		setChanged();
		notifyObservers();
	}

	/**
	 * Sets the seconds of the wrapper to zero
	 */
	public void resetWrapper_secs() {
		this.wrapper_secs = 0;
	}

	/**
	 * 
	 */
	public void setPause(boolean on) {
//		if (on) {
//			this.wrapper_pause_secs = this.wrapper_secs;
//			this.wrapper_secs = 0;
////			setChanged();
////			notifyObservers(2);
//		} else {
//			this.wrapper_secs = this.wrapper_pause_secs;
////			setChanged();
////			notifyObservers(3);
//		}
		///if(on){
			this.wrapup=on;
			setChanged();
			if (wrapup)
				notifyObservers(1);
			else
				notifyObservers(0);
		//(}
	}

	/**
	 * Returns the robots velocity.
	 * 
	 * @return Current robots velocity.
	 */
	public float getRobot_velocity() {
		return robot_velocity;
	}

	/**
	 * Modifies the robots velocity.
	 * 
	 * @param robot_velocity
	 *            New robots velocity.
	 */
	public void setRobot_velocity(float robot_velocity) {
		if (!getRobotIfMoving()) {
			this.robot_velocity = robot_velocity;
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Makes the robot change to another state
	 * 
	 * @param goToState
	 *            New state of the robot
	 */
	public void setRobotGoToState(int goToState) {
		if (!getRobotIfMoving()) {
			if (this.robot_goToState != goToState) {
				this.robot_goToState = goToState;
				this.robot_changed_GTS = true;
				this.setRobotMoving(true);
				// this.setFinished(false);
			}
		}
	}

	/**
	 * Return the next state of the robot
	 * 
	 * @return Next state of the robot
	 */
	public int getRobotGoToState() {
		return robot_goToState;
	}

	/**
	 * Makes the robot change its current state
	 * 
	 * @param currentState
	 *            New state of the robot
	 */
	public void setRobotCurrentState(int currentState) {
		if (this.robot_current_state != currentState) {
			System.out.println("Changing State Robot2..." + currentState);
			this.robot_current_state = currentState;
			this.robot_changed_CS = true;
			this.setRobotMoving(false);
			// this.setFinished(true);
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Return the current state of the robot
	 * 
	 * @return Current state of the robot
	 */
	public int getRobotCurrentState() {
		return robot_current_state;
	}

	/**
	 * Return if the robot is moving
	 * 
	 * @return True if the robot is moving, false if it is not.
	 */
	public boolean getRobotIfMoving() {
		return robot_moving;
	}

	/**
	 * Modifies the variable that tells if the robot is moving
	 * 
	 * @param moving
	 *            New value. True:is moving;false:it is not
	 */
	public void setRobotMoving(boolean moving) {
		if (robot_moving != moving) {
			robot_moving = moving;
		}
	}

	/**
	 * Return if the robot changes CS
	 * 
	 * @return if the robot changes CS
	 */
	public boolean isChanged_CS() {
		boolean value = robot_changed_CS;
		robot_changed_CS = false;
		return value;
	}

	/**
	 * Return if the robot changes GTS
	 * 
	 * @return if the robot changes GTS
	 */
	public boolean isChanged_GTS() {
		boolean value = robot_changed_GTS;
		robot_changed_GTS = false;
		return value;
	}

	/**
	 * Inserts a new sensor in the quality subsystem
	 * 
	 * @param s
	 *            Sensor to be inserted
	 */
	public void addSensor(Sensor s) {
		sensors.add(s);
	}

	/**
	 * Checks for any changes in the sensors the subsystem has. In case a change
	 * is detected observers are notified.
	 */
	public void checkSensorsChanges() {
		for (int i = 0; i < sensors.size(); i++) {
			if (sensors.get(i).isModified()) {
				setChanged();
				notifyObservers(sensors.get(i));
			}
		}
	}

	// public boolean getEmptyBox() {
	// return emptyBox;
	// }
	//
	// public void setEmptyBox(boolean empty) {
	// if(emptyBox!=empty){
	// this.emptyBox = empty;
	// // setChanged();
	// // notifyObservers();
	// }
	// }

}
