package cc.simulation.subsystems;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import cc.simulation.elements.Cake;
import cc.simulation.elements.ConveyorCake;
import cc.simulation.elements.LightSensor;
import cc.simulation.elements.TouchSensor;
import cc.simulation.elements.Valve;
import cc.simulation.state.CakeSubsystemState;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class CakeSubsystem extends Node implements Observer {

	// Sensors
	LightSensor s1, s2;
	TouchSensor s3;

	// Conveyor
	ConveyorCake conv;

	// Valves
	Valve chocolate;
	Valve caramel;

	// State interface
	CakeSubsystemState _state;

	public CakeSubsystem() {
		_state = CakeSubsystemState.getInstance();
		_state.addObserver(this);
		initElements();
	}

	private void initElements() {
		conv = new ConveyorCake();
		conv.setVelocity(0f);
		this.attachChild(conv);

		s1 = new LightSensor("sensor1");
		s1.setLocalTranslation(-2, 4.3f, 0f);
		this.attachChild(s1);
		_state.addSensor(s1);

		s2 = new LightSensor("sensor2");
		s2.setLocalTranslation(3, 4.3f, 0f);
		this.attachChild(s2);
		_state.addSensor(s2);

		s3 = new TouchSensor("sensor3");
		s3.setLocalTranslation(7.5f, 4.2f, 0);
		this.attachChild(s3);
		_state.addSensor(s3);

		chocolate = new Valve("chocolate");
		chocolate.setLocalTranslation(-2, 8, 0);
		this.attachChild(chocolate);

		caramel = new Valve("caramel");
		caramel.setLocalTranslation(3, 8, 0);
		this.attachChild(caramel);
	}

	public void update(Vector<Spatial> elements, float timePerFrame) {
		boolean sen1 = false, sen2 = false, sen3 = false;

		for (int i = 0; i < elements.size(); i++) {
			Spatial element = elements.get(i);
			if (((Cake) element).falling) {
				if (conv.hasCollision(element, false)) {
					// if (element.getLocalTranslation().x < -1.8f) {
					element.getLocalTranslation().y = 4.3f;
					element.getLocalTranslation().x += conv.getVelocity()
							* timePerFrame;
					((Cake) element).falling = false;
					// System.out.println(element.getLocalTranslation().x);
					// }

				} else if ((element.getLocalTranslation().y > 0)
						&& (element.getLocalTranslation().x == -18f)) {
					element.getLocalTranslation().y -= 3 * timePerFrame;
				} else if ((element.getLocalTranslation().y > 0)
						&& (element.getLocalTranslation().x == 0f)) {
					element.setLocalTranslation(-18, 10f, -8.5f);
				}
			} else {
				
				if (conv.hasCollision(element, false)) {
					element.getLocalTranslation().y = 4.3f;
					element.getLocalTranslation().x += conv.getVelocity()
							* timePerFrame;
				}	
//				} else if ((element.getLocalTranslation().y > 0)&&(element.getLocalTranslation().x < -3f)) {
//						element.getLocalTranslation().y -= 3 * timePerFrame;
//					 
//				}

			}

			// Sensors detection
			if (!sen1 && s1.hasCollision(element, false)) {
				sen1 = true;
			}

			if (!sen2 && s2.hasCollision(element, false)) {
				sen2 = true;
			}

			if (!sen3 && s3.hasCollision(element, false)) {
				sen3 = true;
			}
		}

		if (!sen1)
			s1.setOff();
		else
			s1.setOn();
		if (!sen2)
			s2.setOff();
		else
			s2.setOn();
		s3.setActived(sen3);

		caramel.update(timePerFrame);
		chocolate.update(timePerFrame);

		_state.checkSensorsChanges();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// Dont treat sensors changes.
		if (arg1 == null) {
			conv.setVelocity(_state.getConveyor_velocity());
			if (_state.getValve1_open_secs() > 0) {
				chocolate.open(_state.getValve1_open_secs());
				_state.resetValve1_open_secs();
			}
			if (_state.getValve2_open_secs() > 0) {
				caramel.open(_state.getValve2_open_secs());
				_state.resetValve2_open_secs();
			}
		}
	}
}
