package cc.simulation.elements;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

public class Conveyor extends Node {
	
	private Spatial _conveyor;
	private float velocity;
	
	// If we want to add physics - stopping and starting leasing
	private boolean starting = true;
	private boolean stopping = false;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4528261348415945355L;
	
	public Conveyor(){
		this.velocity = 3f;
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
		this.velocity = 3f;
	}
	
	public void translate(Vector3f newPosition){
		_conveyor.setLocalTranslation(newPosition);
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

}
