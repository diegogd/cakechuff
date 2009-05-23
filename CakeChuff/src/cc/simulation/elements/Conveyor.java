package cc.simulation.elements;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
/**
 * Implementation and definition of the conveyor, one of
 * the simulation elements that compose CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
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
	/**
	 * Constructor
	 * Initializes the current velocity and the acceleration of
	 * the conveyor
	 */
	public Conveyor(){
		this.current_velocity = 3f;
		this.acceleration = 4f;
	}
	/**
	 * Constructor
	 * Initializes the current velocity and the acceleration of
	 * the conveyor and assigns an identification to it
	 * @param id Identification of the conveyor
	 */
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
	/**
	 * Modifies the acceleration of the conveyor
	 * @param acceleration The new acceleration of the conveyor
	 */
	public void setAcceleration(float acceleration){
		this.acceleration = acceleration;
	}
	/**
	 * Translate the conveyor to a specific position
	 * @param newPosition The new position of the conveyor
	 */
	public void translate(Vector3f newPosition){
		_conveyor.setLocalTranslation(newPosition);
	}
	/**
	 * Returns the current velocity of the conveyor
	 * @return The current velocity
	 */
	public float getVelocity() {
		return current_velocity;
	}
	/**
	 * Modifies the velocity of the conveyor
	 * @param velocity The new velocity of the conveyor
	 */
	public void setVelocity(float velocity) {
//this.final_velocity = velocity;		
		this.current_velocity = velocity;
	}
	
	/**
	 * Auxiliary method implemented to reduce the velocity 
	 * in the animation.
	 * @param tps Factor of velocity modification
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
