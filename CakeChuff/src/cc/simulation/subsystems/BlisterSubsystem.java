package cc.simulation.subsystems;

import java.util.Observable;
import java.util.Observer;

import cc.simulation.elements.ConveyorBlister;
import cc.simulation.elements.Engraver;
import cc.simulation.elements.PlasticSupplier;
import cc.simulation.state.BlisterSubsystemState;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class BlisterSubsystem extends Node implements Observer{
	
	// Conveyor
	ConveyorBlister conv;
	
	// PlasticSupplier
	PlasticSupplier supplier;
	
	// Engraver
	Engraver engraver;
	
	// State
	BlisterSubsystemState _state;
	
	
	public BlisterSubsystem() {
		_state = BlisterSubsystemState.getInstance();
		_state.addObserver(this);
		initElements();
	}
	
	private void initElements(){
		conv = new ConveyorBlister();
		conv.setVelocity(0);
		this.attachChild(conv);
		supplier = new PlasticSupplier();
		supplier.setLocalTranslation(-12, 4.1f, 0);
		this.attachChild(supplier);
		engraver = new Engraver();
		engraver.setLocalTranslation(-8, 7f, 0f);		
		this.attachChild(engraver);
	}
	
	public void update(float timePerFrame){
		supplier.grow(timePerFrame*conv.getVelocity());
		
		// s3.setActived(s3.hasCollision(element, false));
		
		_state.checkSensorsChanges();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		conv.setVelocity(_state.getConveyor_velocity());
	}
}
