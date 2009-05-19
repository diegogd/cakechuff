package cc.simulation.elements;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

public class Conveyor extends Node {
	
	private Spatial _conveyor;
	protected float final_velocity, current_velocity;
	private float acceleration;
	
	// If we want to add physics - stopping and starting leasing
	private boolean starting = true;
	private boolean stopping = false;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4528261348415945355L;
	
	public Conveyor(){
		this.current_velocity = 3f;
		this.acceleration = 4f;
	}
	
	public Conveyor(String id) {
		// TODO Auto-generated constructor stub
		Box conveyor = new Box(id, new Vector3f(-10,0,-2), 
				new Vector3f(10,5,2));
		this._conveyor = conveyor;
		conveyor.setDefaultColor(ColorRGBA.white);
		conveyor.updateRenderState();
		conveyor.setModelBound(new BoundingBox());
		conveyor.updateModelBound();
		
		this.attachChild(conveyor);
		this.current_velocity = 3f;
		this.acceleration = 4f;
	}
	
	public void setAcceleration(float acceleration){
		this.acceleration = acceleration;
	}
	
	public void translate(Vector3f newPosition){
		_conveyor.setLocalTranslation(newPosition);
	}

	public float getVelocity() {
		return current_velocity;
	}

	public void setVelocity(float velocity) {
//this.final_velocity = velocity;		
		this.current_velocity = velocity;
	}
	
	/**
	 * This methods is implemented to reduce the velocity 
	 * in the animation.
	 */
	public void updateParameters(float tps){
//		float increment = 0; 
//		
//		if(current_velocity > final_velocity){
//			increment = -1*(acceleration*tps);
//		} else {
//			increment = (acceleration*tps);
//		}
//		
//		current_velocity += increment; 
	}

}
