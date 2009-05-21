package cc.simulation.subsystems;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cc.simulation.elements.Blister;
import cc.simulation.elements.Cake;
import cc.simulation.elements.PacketBox;
import cc.simulation.elements.Robot;
import cc.simulation.elements.Table;
import cc.simulation.state.Robot1State;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

public class Robot1 extends Node implements Observer {

	private Robot robot;

	Robot1State _state;

	private float angleSubsystem1 = 3.6f;
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
	final int DROPINTABLE = 6;
	final int PICKUPBLISTER = 7;
	// final int DROPBLISTER = 8;
	final int PICKUPPACKET = 9;
	final int DROPINSUB3 = 10;

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

	public boolean pickUpCake(float time, List<Spatial> elements) {
		
		if (robot.getHasObject()) {
			if (robot.bendBody(1.5f, time)) {
				phase = 0;
				return true;
			}
		}
		
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
			if (robot.openHand(-0.3f, time))
				this.phase++;
			break;
		case 3:
			if (robot.bendBody(2.05f, time))
				this.phase++;
			break;
		case 4:
			if (robot.openHand(-0.15f, time))
				this.phase++;
			break;
		case 5:
			if (robot.takeObject(elements))
				this.phase++;
			break;
		case 6:
			if (robot.bendBody(1.5f, time))
				this.phase++;
			break;
		case 7:
			phase = 0;
			return true;
		}
		return false;

	}

	public boolean dropCake(float time, Spatial element) {

		if (!robot.getHasObject()) {
			if (robot.bendBody(1.5f, time)) {
				phase = 0;
				return true;
			}
		}
		
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
			if (robot.bendBody(2.1f, time))
				this.phase++;
			break;
		case 3:
			if (robot.leaveHandObject(-0.785f, time, element))
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

	public boolean pickUpBlister(float time, List<Spatial> elements) {

		if (robot.getHasObject()) {
			if (robot.bendBody(1.5f, time)) {
				phase = 0;
				return true;
			}
		}

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
			if (robot.openHand(-0.3f, time))
				this.phase++;
			break;
		case 3:
			if (robot.bendBody(2.1f, time))
				this.phase++;
			break;
		case 4:
			if (robot.takeObject(elements))
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

	public boolean dropBlister(float time, Spatial target) {
		
		if (!robot.getHasObject()) {
			if (robot.bendBody(1.5f, time)) {
				phase = 0;
				return true;
			}
		}
		
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
			if (robot.bendBody(2.1f, time))
				this.phase++;
			break;
		case 3:
			if (robot.leaveHandObject(-0.785f, time, target))
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

	public boolean pickUpPacket(float time, List<Spatial> elements) {
		
		if (robot.getHasObject()) {
			if (robot.bendBody(1.5f, time)) {
				phase = 0;
				return true;
			}
		}
		
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
			if (robot.openHand(-0.3f, time))
				this.phase++;
			break;
		case 3:
			if (robot.bendBody(2.1f, time))
				this.phase++;
			break;
		case 4:
			if (robot.takeObject(elements))
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

	public boolean dropPacket(float time, Spatial target) {
		
		if (!robot.getHasObject()) {
			if (robot.bendBody(1.5f, time)) {
				phase = 0;
				return true;
			}
		}
		
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
			if (robot.bendBody(2.1f, time))
				this.phase++;
			break;
		case 3:
			if (robot.leaveHandObject(-0.785f, time, target))
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
		if (_state.getRobot_velocity() > 0) {
			robot.setSpeed(_state.getRobot_velocity());
		}
	}

	/**
	 * Gestionar los estados y las llamadas a las funciones en el update
	 */
	public void update(float time, Factory factory) {
		Iterator<Spatial> elem;
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
				if (pickUpCake(time, factory.cakeSub.cakes) && robot.getHasObject()) {
					_state.setCurrentState(PICKUPCAKE);
				}
				break;
			case DROPINTABLE:

				if (dropCake(time, factory.table)) {
					_state.setCurrentState(DROPINTABLE);

				}

				break;

			case PICKUPBLISTER:
				if (pickUpBlister(time, factory.blisterSub.blisters)) {
					_state.setCurrentState(PICKUPBLISTER);
				}
				break;

			// case DROPBLISTER:
			// elem = element.iterator();
			// while (elem.hasNext()) {
			// Spatial aux = elem.next();
			// if (aux instanceof Table) {
			// if (dropBlister(time,aux)){
			// _state.setCurrentState(DROPBLISTER);
			// }
			// }
			// }
			// break;

			case PICKUPPACKET:
				if (pickUpPacket(time, factory.table.getObjects())) {
					_state.setCurrentState(PICKUPPACKET);					
				}
				break;
			case DROPINSUB3:

				if (dropPacket(time, factory.qualitySub)) {
					_state.setCurrentState(DROPINSUB3);
					break;
				}
				break;
				
			default:
				if (moveToInit(time))
					_state.setCurrentState(INIT);
				break;
			}
			// _state.setMoving(false); // It has finished moving
		}
	}
}
