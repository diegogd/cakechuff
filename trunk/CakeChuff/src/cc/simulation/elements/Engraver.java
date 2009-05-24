package cc.simulation.elements;


import cc.simulation.utils.ModelLoader;
import cc.simulation.utils.Rotations;

import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
/**
 * Implementation and definition of the engraver, one of
 * the simulation elements that compose CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Engraver extends Node{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2036950731870709772L;
	private float speed;
	private boolean direction;
	Node pivot;
	/**
	 * Constructor
	 * Initializes the speed, direction and pivot of the engraver and loads its graphical shape.
	 */
	public Engraver() {
		
		speed = 0;
		direction = false;
		pivot = new Node();
		loadShape();
		this.attachChild(pivot);
		this.setLocalRotation(Rotations.rotateY(1));
	}
	/**
	 * Returns the speed of the engraver
	 * @return The speed of the engraver
	 */
	public float getSpeed(){
		return this.speed;
	}
	/**
	 * Modifies the speed of the engraver
	 * @param speed The new speed of the engraver
	 */
	public void setSpeed(float speed){
		this.speed = speed;
	}
	/**
	 * Loads the object of the graphical shape of the engraver
	 */
	public void loadShape(){
		pivot.attachChild(
		ModelLoader.loadOBJ(getClass().getClassLoader().getResource("model/engraver2.obj"), true)
		);
		// pivot.setRenderQueueMode(Renderer.QUEUE_INHERIT);
	}
	
	/**
	 * Updates the position, direction and speed of the engraver depending on the time
	 * per frames
	 * @param timeperframes Depending of this parameter the translation of the engraver will be different
	 */
	public void update(float timeperframes)
	{
//		System.out.println(pivot.getLocalTranslation().y);
		if(speed>0){
			if (direction){
				if(pivot.getLocalTranslation().y >= 0.0f){
					pivot.getLocalTranslation().y  = 0.0f;
					direction = false;
					
					//Approach 2
					speed = 0;
				}
				else {
					pivot.getLocalTranslation().y += speed*timeperframes;
				}
			}else{
				if(pivot.getLocalTranslation().y <= -1.6f){
					pivot.getLocalTranslation().y  = -1.6f;
					direction = true;
					
					//Cargar Blister?
					//return new Blister()
					
				}
				else {
					pivot.getLocalTranslation().y -= speed*timeperframes;
				}				
			}	
		}
	}
	
}
