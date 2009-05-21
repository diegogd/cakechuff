package cc.simulation.elements;

import java.net.URL;

import cc.simulation.utils.ModelLoader;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;

public class Cutter extends Node {

	
	private static final long serialVersionUID = 1256342121415489725L;
	
	private float speed;
	
	Node pivot;
	
	private boolean direction;

	public Cutter() {
//		loadModel();

		pivot = new Node();
		this.attachChild(pivot);
		loadBox();
		direction = false;
		speed = 0;
	}
	
	public float getSpeed(){
		return this.speed;
	}
	public void setSpeed(float speed){
		this.speed = speed;
	}
	
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
