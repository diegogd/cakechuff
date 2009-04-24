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

	private float angleSubsystem1 = 3.752f;
	private float angleSubsystem2 = 5.847f;
	private float angleSubsystem3 = FastMath.PI / 2;
	private float angleTable = 2.705f;

	private int phase;

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
		this.attachChild(robot);
		_state = Robot1State.getInstance();
		_state.addObserver(this);

		phase = 0;
	}

	public boolean moveToInit(float time) {
		if (robot.moveTo(0, time))
			return true;
		return false;
	}

	public boolean moveToSub1(float time) {
		if (robot.moveTo(angleSubsystem1, time))
			return true;
		return false;
	}

	public boolean moveToSub2(float time) {
		if (robot.moveTo(angleSubsystem2, time))
			return true;
		return false;
	}

	public boolean moveToSub3(float time) {
		if (robot.moveTo(angleSubsystem3, time))
			return true;
		return false;
	}

	public boolean moveToTable(float time) {
		if (robot.moveTo(angleTable, time))
			return true;
		return false;
	}

	public boolean pickUpCake(float time) {

		switch (this.phase) {
		case 0:
			if (robot.bendBody(1.5f, time))
				this.phase++;
			break;
		case 1:
			if (robot.moveTo(angleSubsystem1, time))
				this.phase++;
			break;
		case 2:
			if (robot.openHand(-0.785f, time))
				this.phase++;
			break;
		case 3:
			if (robot.bendBody(2.005f, time))
				this.phase++;
			break;
		case 4:
			if (robot.openHand(0f, time))
				this.phase++;
			break;
		case 5:
			if (robot.bendBody(1.5f, time))
				this.phase++;
			break;
		case 6:
			phase = 0;
			return true;
		}
		return false;

	}

	public boolean dropCake(float time) {

		switch (this.phase) {
		case 0:
			if (robot.bendBody(1.5f, time))
				this.phase++;
			break;
		case 1:
			if (robot.moveTo(angleTable, time))
				this.phase++;
			break;
		case 2:
			if (robot.bendBody(2.005f, time))
				this.phase++;
			break;
		case 3:
			if (robot.openHand(-0.785f, time))
				this.phase++;
			break;
		case 4:
			if (robot.bendBody(1.5f, time))
				this.phase++;
			break;
		case 5:
			phase = 0;
			return true;
		}
		return false;
	}

	public boolean pickUpBlister(float time) {

		switch (this.phase) {
		case 0:
			if (robot.bendBody(1.5f, time))
				this.phase++;
			break;
		case 1:
			if (robot.moveTo(angleSubsystem2, time))
				this.phase++;
			break;
		case 2:
			if (robot.openHand(-0.785f, time))
				this.phase++;
			break;
		case 3:
			if (robot.bendBody(2.005f, time))
				this.phase++;
			break;
		case 4:
			if (robot.openHand(0f, time))
				this.phase++;
			break;
		case 5:
			if (robot.bendBody(1.5f, time))
				this.phase++;
			break;
		case 6:
			phase = 0;
			return true;
		}
		return false;
	}

	public boolean dropBlister(float time) {
		switch (this.phase) {
		case 0:
			if (robot.bendBody(1.5f, time))
				this.phase++;
			break;
		case 1:
			if (robot.moveTo(angleTable, time))
				this.phase++;
			break;
		case 2:
			if (robot.bendBody(2.005f, time))
				this.phase++;
			break;
		case 3:
			if (robot.openHand(-0.785f, time))
				this.phase++;
			break;
		case 4:
			if (robot.bendBody(1.5f, time))
				this.phase++;
			break;
		case 5:
			phase = 0;
			return true;
		}
		return false;
	}

	public boolean pickUpPacket(float time) {
		switch (this.phase) {
		case 0:
			if (robot.bendBody(1.5f, time))
				this.phase++;
			break;
		case 1:
			if (robot.moveTo(angleTable, time))
				this.phase++;
			break;
		case 2:
			if (robot.openHand(-0.785f, time))
				this.phase++;
			break;
		case 3:
			if (robot.bendBody(2.005f, time))
				this.phase++;
			break;
		case 4:
			if (robot.openHand(0f, time))
				this.phase++;
			break;
		case 5:
			if (robot.bendBody(1.5f, time))
				this.phase++;
			break;
		case 6:
			phase = 0;
			return true;
		}
		return false;
	}

	public boolean dropPacket(float time) {
		switch (this.phase) {
		case 0:
			if (robot.bendBody(1.5f, time))
				this.phase++;
			break;
		case 1:
			if (robot.moveTo(angleSubsystem3, time))
				this.phase++;
			break;
		case 2:
			if (robot.bendBody(2.005f, time))
				this.phase++;
			break;
		case 3:
			if (robot.openHand(-0.785f, time))
				this.phase++;
			break;
		case 4:
			if (robot.bendBody(1.5f, time))
				this.phase++;
			break;
		case 5:
			phase = 0;
			return true;
		}
		return false;
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
				if (moveToInit(time)) {
					_state.setCurrentState(INIT);
				}
				break;
			case SUBSYSTEM1:
				if (moveToSub1(time)) {
					_state.setCurrentState(SUBSYSTEM1);
				}
				break;
			case SUBSYSTEM2:
				if (moveToSub2(time)) {
					_state.setCurrentState(SUBSYSTEM2);
				}
				break;
			case SUBSYSTEM3:
				if (moveToSub3(time))
					_state.setCurrentState(SUBSYSTEM3);
				break;
			case TABLE:
				if (moveToTable(time)) {
					_state.setCurrentState(TABLE);
				}
				break;
			case PICKUPCAKE:
				if (pickUpCake(time)) {
					_state.setCurrentState(PICKUPCAKE);
				}
				break;
			case DROPCAKE:
				if (dropCake(time))
					_state.setCurrentState(DROPCAKE);
				break;
			case PICKUPBLISTER:
				if (pickUpBlister(time))
					_state.setCurrentState(PICKUPBLISTER);
				break;
			case DROPBLISTER:
				if (dropBlister(time))
					_state.setCurrentState(DROPBLISTER);
				break;
			case PICKUPPACKET:
				if (pickUpPacket(time))
					_state.setCurrentState(PICKUPPACKET);
				break;
			case DROPPACKET:
				if (dropPacket(time))
					_state.setCurrentState(DROPPACKET);
				break;
			default:
				if (moveToInit(time))
					_state.setCurrentState(INIT);
				break;
			}
			_state.setMoving(false); // It has finished moving
		}
	}
}
