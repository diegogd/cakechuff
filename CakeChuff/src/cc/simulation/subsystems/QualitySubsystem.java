package cc.simulation.subsystems;

import java.util.Observable;
import java.util.Observer;

import cc.simulation.elements.ConveyorBlister;
import cc.simulation.elements.ConveyorQuality;
import cc.simulation.elements.Engraver;
import cc.simulation.elements.LightSensor;
import cc.simulation.elements.PacketBox;
import cc.simulation.elements.PlasticSupplier;
import cc.simulation.elements.Robot;
import cc.simulation.state.BlisterSubsystemState;
import cc.simulation.state.QualitySubsystemState;
import cc.simulation.utils.Rotations;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

public class QualitySubsystem extends Node implements Observer{
	
	// Conveyor
	ConveyorQuality conv;
	
	// Sensors
	LightSensor qa1, qa2, qa3, qa4;
	
	// State
	QualitySubsystemState _state;
	
	//Robot
	Robot robot2;
	
	//Boxes
	PacketBox goodBox, badBox;
	
	
	public QualitySubsystem() {
		_state = QualitySubsystemState.getInstance();
		_state.addObserver(this);
		initElements();
	}
	
	private void initElements(){
		conv = new ConveyorQuality();
		conv.setVelocity(0);
		this.attachChild(conv);
		
		qa1 = new LightSensor("quality1");
		qa1.setLocalRotation(Rotations.rotateX(0.5f));
		qa1.setLocalTranslation(-1,0,-1);
		qa2 = new LightSensor("quality2");
		qa2.setLocalRotation(Rotations.rotateX(0.5f));
		qa2.setLocalTranslation(-1,0,1);
		qa3 = new LightSensor("quality3");
		qa3.setLocalRotation(Rotations.rotateX(0.5f));
		qa3.setLocalTranslation(1,0,-1);
		qa4 = new LightSensor("quality4");
		qa4.setLocalRotation(Rotations.rotateX(0.5f));
		qa4.setLocalTranslation(1,0,1);
		Box top = new Box("", new Vector3f(-1.2f, 1, -1.2f), new Vector3f(1.2f, 1.1f, 1.2f));
		top.setDefaultColor(ColorRGBA.darkGray);
		Node sensors = new Node();
		sensors.attachChild(qa1);
		sensors.attachChild(qa2);
		sensors.attachChild(qa3);
		sensors.attachChild(qa4);
		sensors.attachChild(top);
		
		sensors.setLocalTranslation(-5, 6, 0);
		this.attachChild(sensors);
		
	}
	
	public void update(float timePerFrame){		
		
		// s3.setActived(s3.hasCollision(element, false));
		
		_state.checkSensorsChanges();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		conv.setVelocity(_state.getConveyor_velocity());
	}
}
