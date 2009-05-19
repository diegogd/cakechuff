package cc.simulation.state;

import java.util.Observable;
import java.util.Vector;

import cc.simulation.elements.Sensor;

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
	boolean robot_moving,robot_changed_GTS,robot_changed_CS;
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
//		numcakes_passed = 0;
	}

	public int getIfQualityPassed() {
		return numcakes_passed;
	}

	// Esta funcion es interna (no se deberia de usar en la logica)
	public void setIfQualityPassed(int numcakes) {
		numcakes_passed = numcakes;
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
		if (!getRobotIfMoving()) {
			this.robot_velocity = robot_velocity;
			setChanged();
			notifyObservers();
		}
	}

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

	public int getRobotGoToState() {
		return robot_goToState;
	}

	public void setRobotCurrentState(int currentState) {
		if (this.robot_current_state != currentState) {
			//System.out.println("Changing State...");
			this.robot_current_state = currentState;
			this.robot_changed_CS = true;
			this.setRobotMoving(false);
			// this.setFinished(true);
			setChanged();
			notifyObservers();
		}
	}

	public int getRobotCurrentState() {
		return robot_current_state;
	}

	public boolean getRobotIfMoving() {
			return robot_moving;
	}

	public void setRobotMoving(boolean moving) {
		if (robot_moving != moving) {
			robot_moving = moving;
		}
	}
	
	public boolean isChanged_CS() {
		boolean value = robot_changed_CS;
		robot_changed_CS = false;
		return value;
	}

	public boolean isChanged_GTS() {
		boolean value = robot_changed_GTS;
		robot_changed_GTS = false;
		return value;
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
