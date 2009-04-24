package cc.simulation.state;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Set;
import java.util.Vector;

import cc.simulation.elements.Sensor;

public class Robot1State extends Observable {

	private static Robot1State _instance = null;

//	private HashMap<Float, Boolean> position_angles;

//	private float current_angle;

	private float robot_velocity = 0;

	public final int INIT = 0;
	public final int SUBSYSTEM1 = 1;
	public final int SUBSYSTEM2 = 2;
	public final int SUBSYSTEM3 = 3;
	public final int TABLE = 4;
	public final int PICKUPCAKE = 5;
	public final int DROPCAKE = 6;
	public final int PICKUPBLISTER = 7;
	public final int DROPBLISTER = 8;
	public final int PICKUPPACKET = 9;
	public final int DROPPACKET = 10;

	int current_state, goToState;
	boolean _moving;

	public Robot1State() {
//		position_angles = new HashMap<Float, Boolean>();
//		current_angle = 0;
		current_state = 0;
		_moving = false;
	}

	public static synchronized Robot1State getInstance() {
		if (_instance == null) {
			_instance = new Robot1State();
		}
		return _instance;
	}

	public float getRobot_velocity() {
		return robot_velocity;
	}

	public void setRobot_velocity(float robot_velocity) {
		this.robot_velocity = robot_velocity;
		setChanged();
		notifyObservers();
	}

//	public void addPosition(float angle) {
//		position_angles.put((Float) angle, false);
//	}

	
	//LLamada para cambiar los estados!!
	public void setGoToState(int goToState) {
		setChanged();
		notifyObservers();
		this.goToState = goToState;
//		if (this.current_state != this.goToState) {
//			_moving = true;
//		}
	}

	public int getGoToState() {
		return goToState;
	}

	
	public void setCurrentState(int currentState) {
		setChanged();
		notifyObservers();
		this.current_state = currentState;
//		if (this.current_state != this.goToState) {
//			_moving = true;
//		}
	}

	public int getCurrentState() {
		return current_state;
	}

	public boolean getIfMoving() {
		if (current_state == goToState) {
			_moving = false;
		} else {
			_moving = true;
		}
		setChanged();
		notifyObservers();
		return _moving;
	}
	
	public void setMoving(boolean moving){
		setChanged();
		notifyObservers();
		_moving = moving;
	}

//	public void checkPositionChanges() {
//
//		Set<Float> angles = position_angles.keySet();
//		Iterator<Float> iterator = angles.iterator();
//		while (iterator.hasNext()) {
//			float angle = iterator.next();
//			if ((position_angles.get(angle) == true)
//					&& (this.current_angle == angle)) {
//				setChanged();
//				notifyObservers(position_angles.get(angle));
//			}
//		}
//	}


}
