package cc.simulation.subsystems;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import cc.simulation.elements.Cake;
import cc.simulation.elements.ConveyorCake;
import cc.simulation.elements.LightSensor;
import cc.simulation.elements.TouchSensor;
import cc.simulation.elements.Valve;
import cc.simulation.state.CakeSubsystemState;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
/**
 * Implementation of the cake simulation subsystem
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class CakeSubsystem extends Node implements Observer {
	
	private static final long serialVersionUID = -4388993500227829190L;
	
	// Sensors
	LightSensor s1, s2;
	TouchSensor s3;

	// Conveyor
	ConveyorCake conv;
	float lastVelocity;

	// Valves
	Valve chocolate;
	Valve caramel;

	// State interface
	CakeSubsystemState _state;
	
	// Produced cakes
	public List<Spatial> cakes;
	/**
	 * Constructor
	 * Initializes the attributes of the cake subsystem and instantiates its state
	 */
	public CakeSubsystem() {
		_state = CakeSubsystemState.getInstance();
		_state.addObserver(this);
		initElements();
		cakes = new Vector<Spatial>();
	}
	/**
	 * Initializes the attributes of the cake subsystem such as the velocity and
	 * instantiates its elements (the three sensors and the chocolate and the caramel valve)
	 */
	private void initElements() {
		conv = new ConveyorCake();
		conv.setVelocity(0f);
		this.attachChild(conv);

		s1 = new LightSensor("sensor1");
		s1.setLocalTranslation(-1.5f, 4.3f, 0f);
		this.attachChild(s1);
		_state.addSensor(s1);

		s2 = new LightSensor("sensor2");
		s2.setLocalTranslation(3.5f, 4.3f, 0f);
		this.attachChild(s2);
		_state.addSensor(s2);

		s3 = new TouchSensor("sensor3");
		s3.setLocalTranslation(7.5f, 4.2f, 0);
		this.attachChild(s3);
		//_state.addSensor(s3);
		_state.addTouchSensor(s3);

		chocolate = new Valve("chocolate", new ColorRGBA(0.31f, 0.192f,
				0.0705f, 1f));
		chocolate.setLocalTranslation(-2, 8, 0);
		this.attachChild(chocolate);

		caramel = new Valve("caramel", new ColorRGBA(0.6941f, 0.6196f,
				0.02745f, 0.5f));
		caramel.setLocalTranslation(3, 8, 0);
		this.attachChild(caramel);
	}
	/**
	 * Updates the state of the sensors of the cake subsystem 
	 * @param timePerFrame Parameter used in the modification of the position of the cakes
	 */
	public void update(float timePerFrame) {
		boolean sen1 = false, sen2 = false, sen3 = false;

		// For implementing deceleration
		conv.updateParameters(timePerFrame);

		for (int i = 0; i < cakes.size(); i++) {
			Spatial element = cakes.get(i);

			if (conv.hasCollision(element, false)) {
				// if (element.getLocalTranslation().x < -1.8f) {
				element.getLocalTranslation().y = 4.3f;
				element.getLocalTranslation().x += conv.getVelocity()
						* timePerFrame;

				// System.out.println(element.getLocalTranslation().x);
				// }

			} else if ((element.getLocalTranslation().y > 0)
					&& (element.getLocalTranslation().x == -18f)) {
				element.getLocalTranslation().y -= 3 * timePerFrame;

			} else {
				if (element.getLocalTranslation().y > 0) {
					element.getLocalTranslation().y -= 3 * timePerFrame;
				}else{
					element.getLocalTranslation().y=0;
					cakes.remove(element);
					i--;
				}
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

				//opcion1
				//Codigo para cambiar el bounding box
//				System.out.println(" x: " + element.getLocalTranslation().x
//						+ " y: " + element.getLocalTranslation().y + " z: "
//						+ element.getLocalTranslation().z);
//				BoundingBox bound = new BoundingBox(element.getLocalTranslation(),-1f,-1f,-1f);
//				
				//
//				element.setModelBound(bound);
				
				//opcion2
//				bound.transform(element.getLocalRotation(), 
//						element.getWorldBound().getCenter(), new Vector3f(2f, 1f,
//						2f), bound);
//				
//				bound.transform(element.getLocalRotation(), new Vector3f(
//						0,0,0), new Vector3f(2f, 1f,
//						2f), bound);
			
//				element.setModelBound(bound);

			}

			// Check Valves
			if (element instanceof Cake) {
				if (!((Cake) element).isWithChocolate()) {
					if (chocolate.checkCollision(element)) {
						((Cake) element).changeTextureToChocolate();
					} else if (!((Cake) element).isWithCaramel()
							&& caramel.checkCollision(element)) {
						((Cake) element).changeTextureToCaramel();
					}
				} else if (!((Cake) element).isWithCaramel()
						&& caramel.checkCollision(element)) {
					((Cake) element).changeTextureToCaramelAndChocolate();
				}
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
	/**
	 * Updates the state of the valves (opens them x seconds) of the cake subsystem 
	 * @param arg0 Observable object
	 * @param arg1 Has to be null for the modifications to take place
	 */
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
