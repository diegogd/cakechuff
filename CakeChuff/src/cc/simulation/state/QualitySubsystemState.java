package cc.simulation.state;

import java.util.Observable;
import java.util.Vector;

import cc.simulation.elements.Sensor;

public class QualitySubsystemState extends Observable {

	private static QualitySubsystemState _instance = null;

	private Vector<Sensor> sensors;

	private float conveyor_velocity = 0;
	private boolean quality_check = false, quality_passed = false;
	private float wrapper_secs = 0;
	private boolean wrapup = false;

	public final int INIT = 0;
	public final int SUBSYSTEM = 1;
	public final int GOODBOX = 2;
	public final int BADBOX = 3;
	public final int PICKUPPACKET = 4;
	public final int DROPGOODBOX = 5;
	public final int DROPBADBOX = 6;

	int robot_current_state, robot_goToState;
	boolean robot_moving;
	private float robot_velocity;

	public QualitySubsystemState() {
		sensors = new Vector<Sensor>();
		robot_current_state = 0;
		robot_moving = false;
		robot_velocity = 0;
	}

	public static synchronized QualitySubsystemState getInstance() {
		if (_instance == null) {
			_instance = new QualitySubsystemState();
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

	public boolean getQualityCheck() {
		return quality_check;
	}

	public void setQualityCheck(boolean quality) {
		this.quality_check = quality;
		setChanged();
		notifyObservers(quality);
	}

	public void resetQualityCheck() {
		this.quality_check = false;

	}

	public boolean getIfQualityPassed() {
		return quality_passed;
	}

	// Esta funcion es interna (no se deberia de usar en la logica)
	public void setIfQualityPassed(boolean quality) {
		quality_passed = true;
	}

	// *+++++++++++++++

	public boolean getWrappedUp() {
		return wrapup;
	}

	public void setWrappedUp(boolean wrapup) {
		this.wrapup = wrapup;
		setChanged();
		if (wrapup)
			notifyObservers(1);
		else
			notifyObservers(0);
	}

	public void resetWrappedUp() {
		this.wrapup = false;

	}

	// /*+++++++++++

	public float getWrapper_secs() {
		return wrapper_secs;
	}

	public void setWrapper_secs(float wrapper_secs) {
		this.wrapper_secs = wrapper_secs;
		setChanged();
		notifyObservers();
	}

	public void resetWrapper_secs() {
		this.wrapper_secs = 0;
	}

	public float getRobot_velocity() {
		return robot_velocity;
	}

	public void setRobot_velocity(float robot_velocity) {
		this.robot_velocity = robot_velocity;
		setChanged();
		notifyObservers();
	}

	public void setRobotGoToState(int goToState) {
		setChanged();
		notifyObservers();
		this.robot_goToState = goToState;
		// if (this.current_state != this.goToState) {
		// _moving = true;
		// }
	}

	public int getRobotGoToState() {
		return robot_goToState;
	}

	public void setRobotCurrentState(int currentState) {
		setChanged();
		notifyObservers();
		this.robot_current_state = currentState;
		// if (this.current_state != this.goToState) {
		// _moving = true;
		// }
	}

	public int getRobotCurrentState() {
		return robot_current_state;
	}

	public boolean getRobotIfMoving() {
		if (robot_current_state == robot_goToState) {
			robot_moving = false;
		} else {
			robot_moving = true;
		}
		setChanged();
		notifyObservers();
		return robot_moving;
	}

	public void setRobotMoving(boolean moving) {
		setChanged();
		notifyObservers();
		robot_moving = moving;
	}

	public void addSensor(Sensor s) {
		sensors.add(s);
	}

	public void checkSensorsChanges() {
		for (int i = 0; i < sensors.size(); i++) {
			if (sensors.get(i).isModified()) {
				setChanged();
				notifyObservers(sensors.get(i));
			}
		}
	}
}
