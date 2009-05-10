package cc.simulation.subsystems;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import cc.simulation.elements.Cake;
import cc.simulation.elements.ConveyorQuality;
import cc.simulation.elements.LightSensor;
import cc.simulation.elements.PacketBox;
import cc.simulation.elements.Robot;
import cc.simulation.elements.TouchSensor;
import cc.simulation.elements.Wrapper;
import cc.simulation.state.QualitySubsystemState;
import cc.simulation.utils.Rotations;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

public class QualitySubsystem extends Node implements Observer {

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

	public QualitySubsystem() {
		_state = QualitySubsystemState.getInstance();
		_state.addObserver(this);
		initElements();
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

		goodBox = new PacketBox();
		goodBox.setLocalTranslation(20f, 3f, 4f);
		goodBox.setLocalScale(1.5f);
		this.attachChild(goodBox);

		badBox = new PacketBox();
		badBox.setLocalTranslation(20f, 3f, -4f);
		badBox.setLocalScale(1.5f);
		this.attachChild(badBox);
	}

	public void update(Vector<Spatial> elements, float timePerFrame) {
		boolean sen1 = false, sen2 = false, sen3 = false;

		for (int i = 0; i < elements.size(); i++) {
			Spatial element = elements.get(i);

			if (conv.hasCollision(element, false)) {
				element.getLocalTranslation().x += conv.getVelocity()
						* timePerFrame;
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

		wrapper.update(timePerFrame);
		qualityCheck(elements, timePerFrame);
		
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
		}
	}

	private void qualityCheck(Vector<Spatial> elements, float timePerFrame) {
		boolean sen1 = false, sen2 = false, sen3 = false, sen4 = false;
		if (this._state.getQualityCheck()) {

			for (int i = 0; i < elements.size(); i++) {
				Spatial element = elements.get(i);
				// Sensors detection
				if (element instanceof Cake) {
					if (!sen1 && qa1.hasCollision(element, false)) {
						sen1 = true;
					}
					if (!sen2 && qa2.hasCollision(element, false)) {
						sen2 = true;
					}
					if (!sen3 && qa3.hasCollision(element, false)) {
						sen3 = true;
					}
					if (!sen4 && qa4.hasCollision(element, false)) {
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
			
			if((sen1)&&(sen2)&&(sen3)&&(sen4)) _state.setIfQualityPassed(true);
			else _state.setIfQualityPassed(false);
		}
	}
}
