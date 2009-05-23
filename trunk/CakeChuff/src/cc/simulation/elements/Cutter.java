package cc.simulation.elements;

import java.net.URL;

import cc.simulation.utils.ModelLoader;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
/**
 * Implementation and definition of the cutter, one of
 * the simulation elements that compose CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Cutter extends Node {

	
	private static final long serialVersionUID = 1256342121415489725L;
	
	private float speed;
	
	Node pivot;
	
	private boolean direction;
	/**
	 * Constructor
	 * Initializes cutters direction and speed. Also loads it in the graphic interface.
	 */
	public Cutter() {
//		loadModel();

		pivot = new Node();
		this.attachChild(pivot);
		loadBox();
		direction = false;
		speed = 0;
	}
	/**
	 * Returns the speed of the cutterr
	 * @return The speed of the cutter
	 */
	public float getSpeed(){
		return this.speed;
	}
	/**
	 * Modifies the speed of the cutter
	 * @param speed The new speed of the cutter
	 */
	public void setSpeed(float speed){
		this.speed = speed;
	}
	/**
	 * Cutter is graphically displayed as a box
	 */
	private void loadBox(){
		
		Box cutter = new Box("cutter",new Vector3f(-2f,0.99f,-2f), new Vector3f(2f, 1f, 2f));
		
		cutter.setModelBound(new BoundingBox());
		cutter.updateModelBound();
		cutter.setDefaultColor(ColorRGBA.gray);
		cutter.updateRenderState();
		float angles[] = new float[]{0,0,FastMath.PI/2};
		cutter.getLocalRotation().fromAngles(angles);
		pivot.attachChild(cutter);
	}
	/**
	 * Updates the position, direction and speed of the cutter depending on the time
	 * per frames
	 * @param timeperframes Depending of this parameter the translation of the cutter will be different
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
				if(pivot.getLocalTranslation().y <= -2f){
					pivot.getLocalTranslation().y  = -2f;
					direction = true;
					
				}
				else {
					pivot.getLocalTranslation().y -= speed*timeperframes;
				}				
			}	
		}
	}
	
}
