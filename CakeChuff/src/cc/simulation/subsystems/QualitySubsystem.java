package cc.simulation.subsystems;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import cc.simulation.elements.Blister;
import cc.simulation.elements.Cake;
import cc.simulation.elements.ConveyorQuality;
import cc.simulation.elements.LightSensor;
import cc.simulation.elements.Packet;
import cc.simulation.elements.PacketBox;
import cc.simulation.elements.Robot;
import cc.simulation.elements.Table;
import cc.simulation.elements.TouchSensor;
import cc.simulation.elements.Wrapper;
import cc.simulation.state.QualitySubsystemState;
import cc.simulation.utils.Rotations;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

public class QualitySubsystem extends Node implements Observer {

	public final int INIT = 0;
	public final int SUBSYSTEM = 1;
	public final int GOODBOX = 2;
	public final int BADBOX = 3;
	public final int PICKUPPACKET = 4;
	public final int DROPGOODBOX = 5;
	public final int DROPBADBOX = 6;

	float angleSubsystem = (FastMath.PI / 2) + FastMath.PI,
			angleGoodBox = (FastMath.PI / 4) + (FastMath.PI / 2),
			angleBadBox = FastMath.PI / 4;

	// Conveyor
	ConveyorQuality conv;

	// Sensors
	LightSensor qa1, qa2, qa3, qa4, sensor1, sensor2;
	TouchSensor sensor3;
	// State
	QualitySubsystemState _state;

	// Robot
	Robot robot2;

	// Boxes
	PacketBox goodBox, badBox;

	// Wrapper

	Wrapper wrapper;

	private int phase;

	public QualitySubsystem() {
		_state = QualitySubsystemState.getInstance();
		_state.addObserver(this);
		initElements();
		phase = 0;
	}

	private void initElements() {
		conv = new ConveyorQuality();
		conv.setVelocity(0);
		this.attachChild(conv);

		qa1 = new LightSensor("quality1");
		qa1.setLocalRotation(Rotations.rotateX(0.5f));
		qa1.setLocalTranslation(-1, 0, -1);
		qa2 = new LightSensor("quality2");
		qa2.setLocalRotation(Rotations.rotateX(0.5f));
		qa2.setLocalTranslation(-1, 0, 1);
		qa3 = new LightSensor("quality3");
		qa3.setLocalRotation(Rotations.rotateX(0.5f));
		qa3.setLocalTranslation(1, 0, -1);
		qa4 = new LightSensor("quality4");
		qa4.setLocalRotation(Rotations.rotateX(0.5f));
		qa4.setLocalTranslation(1, 0, 1);
		Box top = new Box("", new Vector3f(-1.2f, 1, -1.2f), new Vector3f(1.2f,
				1.1f, 1.2f));
		top.setDefaultColor(ColorRGBA.darkGray);
		Node sensors = new Node();
		sensors.attachChild(qa1);
		sensors.attachChild(qa2);
		sensors.attachChild(qa3);
		sensors.attachChild(qa4);
		sensors.attachChild(top);

		sensors.setLocalTranslation(-5, 6, 0);
		this.attachChild(sensors);

		sensor1 = new LightSensor("QualitySensor1");
		sensor1.setLocalTranslation(-2.5f, 5.1f, 0f);
		this.attachChild(sensor1);
		_state.addSensor(sensor1);

		sensor2 = new LightSensor("QualitySensor2");
		sensor2.setLocalTranslation(5f, 5.1f, 0);
		this.attachChild(sensor2);
		_state.addSensor(sensor2);

		sensor3 = new TouchSensor("QualitySensor3");
		sensor3.setLocalTranslation(9f, 5.1f, 0);
		this.attachChild(sensor3);
		_state.addSensor(sensor3);

		robot2 = new Robot(new Vector3f(15f, 0, 0));
		this.attachChild(robot2);

		wrapper = new Wrapper();
		wrapper.setLocalTranslation(3f, 7f, 0f);
		this.attachChild(wrapper);

		goodBox = new PacketBox("GoodBox");
		goodBox.setLocalRotation(Rotations.rotateY(0.25f));
		goodBox.setLocalTranslation(20f, 0f, 5.5f);
		// goodBox.setLocalScale(1.5f);
		this.attachChild(goodBox);

		badBox = new PacketBox("BadBox");
		badBox.setLocalTranslation(20f, 0f, -5.5f);
		badBox.setLocalRotation(Rotations.rotateY(-0.25f));
		// badBox.setLocalScale(1.5f);
		this.attachChild(badBox);
	}

	public void update(Vector<Spatial> elements, float timePerFrame) {
		boolean sen1 = false, sen2 = false, sen3 = false;

		for (int i = 0; i < elements.size(); i++) {
			Spatial element = elements.get(elements.size() - i - 1);

			if ((conv.hasCollision(element, false))
					&& (element instanceof Blister)) {
				element.getLocalTranslation().x += conv.getVelocity()
						* timePerFrame;
				if (_state.getWrappedUp()) {
					// System.out.println("Getting inside!");
					if (!wrapper.finished)
						wrapper.update(timePerFrame, element);
					else
						_state.setWrappedUp(false);
				}
			}

			// Sensors detection
			if (!sen1 && sensor1.hasCollision(element, false)) {
				sen1 = true;
			}

			if (!sen2 && sensor2.hasCollision(element, false)) {
				sen2 = true;
			}

			if (!sen3 && sensor3.hasCollision(element, false)) {
				sen3 = true;
			}
		}

		if (!sen1)
			sensor1.setOff();
		else
			sensor1.setOn();
		if (!sen2)
			sensor2.setOff();
		else
			sensor2.setOn();

		sensor3.setActived(sen3);

		qualityCheck(elements, timePerFrame);

		robotUpdate(elements, timePerFrame);

		_state.checkSensorsChanges();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 == null) {
			conv.setVelocity(_state.getConveyor_velocity());
			if (_state.getWrapper_secs() > 0) {
				wrapper.setSpeed(_state.getWrapper_secs());
				_state.resetWrapper_secs();
			}
			if (_state.getRobot_velocity() > 0) {
				robot2.setSpeed(_state.getRobot_velocity());
			}

		} else {
			if (arg1 instanceof Boolean) {
				if ((Boolean) arg1 == false) {
					qa1.setOff();
					qa2.setOff();
					qa3.setOff();
					qa4.setOff();
				}
			} else if (arg1 instanceof Integer) {
				if ((Integer) arg1 == 1) {
					wrapper.finished = false;
				} else {
					wrapper.finished = true;
				}
			}
		}
	}

	private void qualityCheck(Vector<Spatial> elements, float timePerFrame) {
		boolean sen1 = false, sen2 = false, sen3 = false, sen4 = false;
		if (this._state.getQualityCheck()) {

			for (int i = 0; i < elements.size(); i++) {
				Spatial element = elements.get(i);
				// Sensors detection
				if (element instanceof Blister) {
					// if (element instanceof Cake) {
					if (!sen1
							&& qa1.hasCollision(((Blister) element).getHole1(),
									false)) {
						sen1 = true;
					}
					if (!sen2
							&& qa2.hasCollision(((Blister) element).getHole2(),
									false)) {
						sen2 = true;
					}
					if (!sen3
							&& qa3.hasCollision(((Blister) element).getHole3(),
									false)) {
						sen3 = true;
					}
					if (!sen4
							&& qa4.hasCollision(((Blister) element).getHole4(),
									false)) {
						sen4 = true;
					}
				}
			}

			if (!sen1)
				qa1.setOff();
			else
				qa1.setOn();
			if (!sen2)
				qa2.setOff();
			else
				qa2.setOn();
			if (!sen3)
				qa3.setOff();
			else
				qa3.setOn();
			if (!sen4)
				qa4.setOff();
			else
				qa4.setOn();

			if ((sen1) && (sen2) && (sen3) && (sen4))
				_state.setIfQualityPassed(true);
			else
				_state.setIfQualityPassed(false);

			_state.resetQualityCheck();
		}
	}

	private void robotUpdate(List<Spatial> element, float time) {
		Iterator<Spatial> elem;
		if (_state.getRobotIfMoving()) {
			switch (_state.getRobotGoToState()) {

			case INIT:
				if (moveToPlace(0, time)) {
					_state.setRobotCurrentState(INIT);
				}
				break;
			case SUBSYSTEM:
				if (moveToPlace(angleSubsystem, time)) {
					_state.setRobotCurrentState(SUBSYSTEM);
				}
				break;
			case GOODBOX:
				if (moveToPlace(angleGoodBox, time)) {
					_state.setRobotCurrentState(GOODBOX);
				}
				break;
			case BADBOX:
				if (moveToPlace(angleBadBox, time))
					_state.setRobotCurrentState(BADBOX);
				break;

			case PICKUPPACKET:
				elem = element.iterator();
				while (elem.hasNext()) {
					Spatial aux = elem.next();
					if ((aux instanceof Blister)
							&& (conv.hasCollision(aux, false))) {
						if (pickUpPacket(time, aux)) {
							_state.setRobotCurrentState(PICKUPPACKET);
						}
					}
				}
				break;
			case DROPGOODBOX:
				// elem = element.iterator();
				// while (elem.hasNext()) {
				// Spatial aux = elem.next();
				// if (aux instanceof PacketBox) {
				if (dropPacket(time, goodBox, true)) {
					_state.setRobotCurrentState(DROPGOODBOX);
				}
				// }
				// }

				break;

			case DROPBADBOX:
				// elem = element.iterator();
				// while (elem.hasNext()) {
				// Spatial aux = elem.next();
				// if (aux instanceof PacketBox) {
				if (dropPacket(time, badBox, false)) {
					_state.setRobotCurrentState(DROPBADBOX);
				}
				// }
				// }
				break;

			default:
				if (moveToPlace(0, time))
					_state.setRobotCurrentState(INIT);
				break;
			}
			_state.setRobotMoving(false); // It has finished moving
		}
	}

	private boolean moveToPlace(float angle, float time) {
		if (robot2.moveTo(angle, time))
			return true;
		return false;
	}

	private boolean pickUpPacket(float time, Spatial element) {

		switch (this.phase) {
		case 0:
			if (robot2.bendBody(1.5f, time))
				this.phase++;
			break;
		case 1:
			if (robot2.moveTo(angleSubsystem, time))
				this.phase++;
			break;
		case 2:
			if (robot2.openHand(-0.785f, time))
				this.phase++;
			break;
		case 3:
			if (robot2.bendBody(2.005f, time))
				this.phase++;
			break;
		case 4:
			if (robot2.openHandObject(0f, time, element)) {
				this.phase++;
			}
			break;
		case 5:
			System.out.println("Im in 1!");
			if (robot2.bendBody(0.5f, time))
				System.out.println("Im in 2!");
			this.phase++;
			break;
		case 6:
			System.out.println("I finished!!!");
			phase = 0;
			return true;
		}
		return false;
	}

	private boolean dropPacket(float time, Spatial element, boolean goodBox) {
		switch (this.phase) {
		case 0:
			if (robot2.bendBody(1.5f, time))
				this.phase++;
			break;
		case 1:
			if (goodBox) {
				if (robot2.moveTo(angleGoodBox, time))
					this.phase++;
			} else {
				if (robot2.moveTo(angleBadBox, time))
					this.phase++;
			}

			break;
		case 2:
			if (robot2.bendBody(2.005f, time))
				this.phase++;
			break;
		case 3:
			if (robot2.leaveHandObject(-0.785f, time, element))
				this.phase++;
			break;
		case 4:
			if (robot2.bendBody(1.5f, time))
				this.phase++;
			break;
		case 5:
			phase = 0;
			return true;
		}
		return false;
	}

}
