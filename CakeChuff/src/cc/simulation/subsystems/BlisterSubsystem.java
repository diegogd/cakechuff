package cc.simulation.subsystems;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import cc.simulation.elements.ConveyorBlister;
import cc.simulation.elements.Cutter;
import cc.simulation.elements.Engraver;
import cc.simulation.elements.LightSensor;
import cc.simulation.elements.PlasticSupplier;
import cc.simulation.elements.TouchSensor;
import cc.simulation.state.BlisterSubsystemState;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class BlisterSubsystem extends Node implements Observer {

	private static final long serialVersionUID = 1459747352145984978L;

	// Conveyor
	ConveyorBlister conv;

	// PlasticSupplier
	PlasticSupplier supplier;

	// Engraver
	Engraver engraver;

	// Cutter
	Cutter cutter;

	// State
	BlisterSubsystemState _state;

	// Sensors
	LightSensor sensor1;
	TouchSensor sensor2;
	
	// Generated Blisters
	public List<Spatial> blisters;

	public BlisterSubsystem() {
		_state = BlisterSubsystemState.getInstance();
		_state.addObserver(this);
		initElements();
		blisters = new Vector<Spatial>();
	}

	private void initElements() {
		conv = new ConveyorBlister();
		conv.setVelocity(0);
		this.attachChild(conv);
		supplier = new PlasticSupplier();
		supplier.setLocalTranslation(-10, 3.1f, 0);
		this.attachChild(supplier);
		engraver = new Engraver();
		engraver.setLocalTranslation(-6, 7f, 0f);
		this.attachChild(engraver);
		cutter = new Cutter();
		cutter.setLocalTranslation(0, 8.6f, 0f);
		this.attachChild(cutter);

		sensor1 = new LightSensor("BlisterSensor1");
		sensor1.setLocalTranslation(2.8f, 4.3f, 0f);
		sensor1.setLocalScale(new Vector3f(1f,1f,2f));
		this.attachChild(sensor1);
		_state.addSensor(sensor1);

		sensor2 = new TouchSensor("BlisterSensor2");
		sensor2.setLocalTranslation(7.5f, 4.2f, 0);
		this.attachChild(sensor2);
		_state.addSensor(sensor2);
	}

	public void update(float timePerFrame) {
		
		supplier.grow(timePerFrame * conv.getVelocity());

		boolean sen1 = false, sen2 = false;

		
		for (int i = 0; i < blisters.size(); i++) {
			Spatial element = blisters.get(i);
			
			if(conv.hasCollision(element, false)){ 
				element.getLocalTranslation().x += conv.getVelocity()*timePerFrame;			
			} 
			
			// Sensors detection
			if (!sen1 && sensor1.hasCollision(element, false)) {
				sen1 = true;
			}

			if (!sen2 && sensor2.hasCollision(element, false)) {
				sen2 = true;
			}
		}

		if (!sen1)
			sensor1.setOff();
		else
			sensor1.setOn();
		
		sensor2.setActived(sen2);
//
//		Spatial aux = engraver.update(timePerFrame);
//		if(aux!=null)elements.add(aux);
		 engraver.update(timePerFrame);
		cutter.update(timePerFrame);

		_state.checkSensorsChanges();
	}

	@Override
	public void update(Observable arg0, Object arg1) {

		if (arg1 == null) {
			conv.setVelocity(_state.getConveyor_velocity());
			if (_state.getCutter_secs() > 0) {
				cutter.setSpeed(_state.getCutter_secs());
				_state.resetCutter_secs();
			}
			if (_state.getEngraver_secs() > 0) {
				engraver.setSpeed(_state.getEngraver_secs());
				_state.resetEngraver_secs();
			}
		}
	}
}
