package cc.simulation.subsystems;

import java.util.Observable;
import java.util.Observer;

import cc.simulation.elements.Robot;
import cc.simulation.state.Robot1State;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

public class Robot1 extends Node implements Observer {

	private Robot robot;

	Robot1State _state;

	private Vector3f positionSubsystem1 = new Vector3f(4, 9f, 0f);
	private Vector3f positionSubsystem2;
	private Vector3f positionSubsystem3;
	private Vector3f positionTable;

	private float angleSubsystem1 = 0.785f;
	private float angleSubsystem2;
	private float angleSubsystem3;
	private float angleTable;

	final int INIT = 0;
	final int SUBSYSTEM1 = 1;
	final int SUBSYSTEM2 = 2;
	final int SUBSYSTEM3 = 3;
	final int TABLE = 4;
	final int PICKUPCAKE = 5;
	final int DROPCAKE = 6;
	final int PICKUPBLISTER = 7;
	final int DROPBLISTER = 8;
	final int PICKUPPACKET = 9;
	final int DROPPACKET = 10;

	public Robot1() {
		// super(new Vector3f(0,0,0));

		robot = new Robot(new Vector3f(0, 0, 0)); // Modificar valores
		_state = Robot1State.getInstance();
		_state.addObserver(this);
	}

	public void moveToInit(float time) {
		// System.out.println();
		// super.moveTo(positionSubsystem1,time);
		robot.moveTo(0, time);
	}

	public void moveToSub1(float time) {
		// System.out.println();
		// super.moveTo(positionSubsystem1,time);
		robot.moveTo(angleSubsystem1, time);
	}

	public void moveToSub2(float time) {
		robot.moveTo(positionSubsystem2, time);
	}

	public void moveToSub3(float time) {
		robot.moveTo(positionSubsystem3, time);
	}

	public void moveToTable(float time) {
		robot.moveTo(positionTable, time);
	}

	public void pickUpCake(float time) {

		// Hacer que se ejecuten de manera unitaria todas estas acciones -->
		// problema: se ejecutaran todas en cada instancia de tiempo

		// moveToSub1();
		// while(super.bendBody(0.25f,time)){}
		if (robot.bendBody(0.785f, time)) {// insertar angulo
			// Anidar el resto
			// if()

		}
		// super.closeHand();
		// super.bendBody(-15);
	}

	public void dropCake(float time) {
		if (robot.has_object) {
			moveToTable(time);
			robot.bendBody(0.785f, time);
			robot.openHand();
			robot.bendBody(0, time);
		}
	}

	public void pickUpBlister(float time) {
		moveToSub2(time);
		robot.bendBody(0.785f, time); // insertar angulo
		robot.closeHand();
		robot.bendBody(0, time);
	}

	public void dropBlister(float time) {
		if (robot.has_object) {
			moveToTable(time);
			robot.bendBody(0.785f, time);
			robot.openHand();
			robot.bendBody(0, time);
		}
	}

	public void pickUpPacket(float time) {
		moveToTable(time);
		robot.bendBody(0.785f, time); // insertar angulo
		robot.closeHand();
		robot.bendBody(0, time);
	}

	public void dropPacket(float time) {
		if (robot.has_object) {
			moveToSub3(time);
			robot.bendBody(0.785f, time);
			robot.openHand();
			robot.bendBody(0, time);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	// //Gestionar los estados y las llamadas a las funciones en el update
	public void update(float time) {
		if (_state.getIfMoving()) {
			switch (_state.getGoToState()) {

			case INIT:
				moveToInit(time);
				_state.setCurrentState(INIT);
				break;
			case SUBSYSTEM1:
				moveToSub1(time);
				_state.setCurrentState(SUBSYSTEM1);
				break;
			case SUBSYSTEM2:
				moveToSub2(time);
				_state.setCurrentState(SUBSYSTEM2);
				break;
			case SUBSYSTEM3:
				moveToSub3(time);
				_state.setCurrentState(SUBSYSTEM3);
				break;
			case TABLE:
				moveToTable(time);
				_state.setCurrentState(TABLE);
				break;
			case PICKUPCAKE:
				pickUpCake(time);
				_state.setCurrentState(PICKUPCAKE);
				break;
			case DROPCAKE:
				dropCake(time);
				_state.setCurrentState(DROPCAKE);
				break;
			case PICKUPBLISTER:
				pickUpBlister(time);
				_state.setCurrentState(PICKUPBLISTER);
				break;
			case DROPBLISTER:
				dropBlister(time);
				_state.setCurrentState(DROPBLISTER);
				break;
			case PICKUPPACKET:
				pickUpPacket(time);
				_state.setCurrentState(PICKUPPACKET);
				break;
			case DROPPACKET:
				dropPacket(time);
				_state.setCurrentState(DROPPACKET);
				break;
			default:
				moveToInit(time);
				_state.setCurrentState(INIT);
				break;
			}
			
			_state.setMoving(false);	//It has finished moving
		}
	}
}
