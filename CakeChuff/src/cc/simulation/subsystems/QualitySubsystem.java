package cc.simulation.subsystems;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.logging.Logger;

import cc.simulation.elements.Blister;
import cc.simulation.elements.ConveyorQuality;
import cc.simulation.elements.LightSensor;
import cc.simulation.elements.PacketBox;
import cc.simulation.elements.Robot;
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
/**
 * Implementation of the quality simulation subsystem
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class QualitySubsystem extends Node implements Observer {
	
	private static final long serialVersionUID = 483908354130145983L;

	Logger logger = Logger.getLogger(QualitySubsystem.class.getName());

	public final int INIT = 0;
	public final int SUBSYSTEM = 1;
	public final int GOODBOX = 2;
	public final int BADBOX = 3;
	public final int PICKUPPACKET = 4;
	public final int DROPGOODBOX = 5;
	public final int DROPBADBOX = 6;
	
	// Generated Blisters
	public Vector<Spatial> takenBlisters;

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
	/**
	 * Constructor
	 * Initializes the attributes of the quality subsystem and instantiates the elements that compose it
	 */
	public QualitySubsystem() {
		_state = QualitySubsystemState.getInstance();
		_state.addObserver(this);
		initElements();
		phase = 0;
		takenBlisters = new Vector<Spatial>();
	}
	/**
	 * Initializes the elements of the quality subsystem such as its sensors, wrapper, robot and the two boxes as well as its attributes.
	 */
	private void initElements() {
		conv = new ConveyorQuality();
		conv.setVelocity(0);
		this.attachChild(conv);

		qa1 = new LightSensor("quality1");
		qa1.setLocalRotation(Rotations.rotateX(0.5f));
		qa1.setLocalTranslation(-1.1f, 0, -1.0f);
		qa2 = new LightSensor("quality2");
		qa2.setLocalRotation(Rotations.rotateX(0.5f));
		qa2.setLocalTranslation(-1.1f, 0, 1.0f);
		qa3 = new LightSensor("quality3");
		qa3.setLocalRotation(Rotations.rotateX(0.5f));
		qa3.setLocalTranslation(1.1f, 0, -1.0f);
		qa4 = new LightSensor("quality4");
		qa4.setLocalRotation(Rotations.rotateX(0.5f));
		qa4.setLocalTranslation(1.1f, 0, 1.0f);
		Box top = new Box("", new Vector3f(-2f, 1, -2f), new Vector3f(2f, 1.1f,
				2f));
		top.setDefaultColor(ColorRGBA.darkGray);
		Node sensors = new Node();
		sensors.attachChild(qa1);
		sensors.attachChild(qa2);
		sensors.attachChild(qa3);
		sensors.attachChild(qa4);
		sensors.attachChild(top);

		sensors.setLocalTranslation(-3, 5, 0);
		this.attachChild(sensors);

		sensor1 = new LightSensor("QualitySensor1");
		sensor1.setLocalTranslation(-1.0f, 4.3f, 0f);
		sensor1.setLocalScale(new Vector3f(1f, 1f, 2f));
		this.attachChild(sensor1);
		_state.addSensor(sensor1);

		sensor2 = new LightSensor("QualitySensor2");
		sensor2.setLocalTranslation(3.1f, 4.3f, 0);
		sensor2.setLocalScale(new Vector3f(1f, 1f, 2f));
		this.attachChild(sensor2);
		_state.addSensor(sensor2);

		sensor3 = new TouchSensor("QualitySensor3");
		sensor3.setLocalTranslation(8f, 4.2f, 0);
		this.attachChild(sensor3);
		_state.addSensor(sensor3);

		robot2 = new Robot(new Vector3f(15f, 0, 0));
		this.attachChild(robot2);

		wrapper = new Wrapper();
		wrapper.setLocalTranslation(1.1f, 7f, 0f);
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
	/**
	 * Updates the state of the elements that compose the quality subsystem (sensors, robot...)
	 * @param timePerFrame Parameter used in the modification of the position of elements
	 */
	public void update(float timePerFrame) {
		boolean sen1 = false, sen2 = false, sen3 = false;

		for (int i = 0; i < takenBlisters.size(); i++) {
			Spatial element = takenBlisters.get(takenBlisters.size() - i - 1);

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

		qualityCheck(takenBlisters, timePerFrame);

		robotUpdate(takenBlisters, timePerFrame);
		
		//emptyBoxUpdate();

		_state.checkSensorsChanges();
	}
	/**
	 * Updates the state of the wrapper and robot
	 * @param arg0 Observable object
	 * @param arg1 Has to be null for the modifications to take place
	 */
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
	/**
	 * Quality control simulation for the generated blisters with cakes.
	 * @param elements Vector composed of blisters
	 * @param timePerFrame Auxiliary parameter
	 */
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
							&& qa2.hasCollision(((Blister) element).getHole3(),
									false)) {
						sen2 = true;
					}
					if (!sen3
							&& qa3.hasCollision(((Blister) element).getHole2(),
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

			int num_cakes = 0;

			if (!sen1)
				qa1.setOff();
			else {
				qa1.setOn();
				num_cakes++;
			}
			if (!sen2)
				qa2.setOff();
			else {
				qa2.setOn();
				num_cakes++;
			}
			if (!sen3)
				qa3.setOff();
			else {
				qa3.setOn();
				num_cakes++;
			}
			if (!sen4)
				qa4.setOff();
			else {
				qa4.setOn();
				num_cakes++;
			}

			_state.setIfQualityPassed(num_cakes);

			_state.resetQualityCheck();
		}
	}
	/**
	 * Updates the state of the robot (initial state, moves blister to the good box or the bad box, picks up a packet...)
	 * @param element List of spatial elements
	 * @param time Time it takes for the change of state of the robot
	 */
	private void robotUpdate(List<Spatial> element, float time) {
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

				if (pickUpPacket(time, element)) {
					_state.setRobotCurrentState(PICKUPPACKET);
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
			// _state.setRobotMoving(false); // It has finished moving
		}
	}
	/**
	 * Moves the robot a certain angle in a specific time
	 * @param angle Angle that the robot moves
	 * @param time Time it takes the robot to move
	 */
	private boolean moveToPlace(float angle, float time) {
		if (robot2.moveTo(angle, time))
			return true;
		return false;
	}
	/**
	 * Empties the boxes if the number of packets is bigger than 4
	 */
	private void emptyBoxUpdate(){
		if(goodBox.numOfPackets()>4){
			
			goodBox.emptyBox();
//			goodBox = new PacketBox("GoodBox");
//			goodBox.setLocalRotation(Rotations.rotateY(0.25f));
//			goodBox.setLocalTranslation(20f, 0f, 5.5f);
//			this.attachChild(goodBox);
		}
		if(badBox.numOfPackets()>4){
			badBox.emptyBox();
//			badBox = new PacketBox("BadBox");
//			badBox.setLocalTranslation(20f, 0f, -5.5f);
//			badBox.setLocalRotation(Rotations.rotateY(-0.25f));
//			this.attachChild(badBox);
		}
	}
	
	/**
	 * Makes the robot pick up a packet
	 * @param time Time it takes the robot to do the movement
	 * @param elements List of packets to be taken by the robot
	 * @return True -> correct phase, no problems picking the packets; False -> Wrong/incorrect phase
	 */
	
	private boolean pickUpPacket(float time, List<Spatial> elements) {

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
			if (robot2.openHand(-0.3f, time))
				this.phase++;
			break;
		case 3:
			if (robot2.bendBody(2.1f, time))
				this.phase++;
			break;
		case 4:
			if (robot2.takeObject(elements)) {
				this.phase++;
			}
			break;
		case 5:
			// System.out.println("Im in 1!");
			if (robot2.bendBody(1.5f, time)) {
				// System.out.println("Im in 2!");
				this.phase++;
			}
			break;
		case 6:
			// System.out.println("I finished!!!");
			phase = 0;
			return true;
		}
		return false;
	}
	/**
	 * Makes the robot drop a packet
	 * @param time Time it takes the robot to do the movement
	 * @param element Packet to be dropped by the robot
	 * @param goodBox True if it has to be dropped in the box of correct packets, false if it has to be dropped in the bad packets box
	 * @return True -> correct phase, no problems picking the packets; False -> Wrong/incorrect phase
	 */
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
			if (robot2.bendBody(2.1f, time))
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
