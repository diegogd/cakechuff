package cc.simulation.subsystems;

import java.util.Observable;
import java.util.Observer;

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
	
	boolean cakefalling = true;

	public CakeSubsystem() {
		_state = CakeSubsystemState.getInstance();
		_state.addObserver(this);
		initElements();
	}
	
	private void initElements(){
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
	
	public void update(Spatial element, float timePerFrame){
		
		if(conv.hasCollision(element, false)){ 
			element.getLocalTranslation().y = 4.3f;
			element.getLocalTranslation().x += conv.getVelocity()*timePerFrame;
			cakefalling=false;
		} else if( !cakefalling ) {						
			element.setLocalTranslation(-18, 10f, -8.5f);
			conv.setVelocity(3f);
			cakefalling=true;
		} else {			
			element.getLocalTranslation().y -= 3*timePerFrame;			
		}
		
		// Sensors detection
		if(s1.hasCollision(element, false)){			
			s1.setOn();
			chocolate.open(timePerFrame);
		} else {
			s1.setOff();
		}
		
		if(s2.hasCollision(element, false)){
			s2.setOn();
			caramel.open(timePerFrame);
		} else
			s2.setOff();
		
		s3.setActived(s3.hasCollision(element, false));
		
		_state.checkSensorsChanges();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		conv.setVelocity(_state.getConveyor_velocity());
	}	
}
