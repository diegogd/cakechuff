package cc.simulation.elements;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Cylinder;

public class PlasticSupplier extends Node{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8853840882662831706L;
	Cylinder roll;
	Box plastic;
	
	public PlasticSupplier() {
		initElement();
	}
	
	public void initElement(){
		
		roll = new Cylinder("pole", 4, 20, 0.2f, 4.5f,true);
		roll.setDefaultColor(ColorRGBA.black);
		roll.setLocalTranslation(0, 0, -0.2f);
		this.attachChild(roll);
		
		roll = new Cylinder("roll", 4, 20, 1f, 3.4f,true);
		roll.setDefaultColor(ColorRGBA.lightGray);
		this.attachChild(roll);
		
		plastic = new Box("plastic", new Vector3f(0,0.99f,-1.7f), new Vector3f(2f, 1f, 1.7f));
		plastic.setDefaultColor(ColorRGBA.lightGray);
		plastic.setModelBound(new BoundingBox());
		plastic.updateModelBound();
		this.attachChild(plastic);
	}
	
	public void grow(float amount){
		plastic.setLocalScale(new Vector3f(plastic.getLocalScale().x+amount/2,1,1));
		
		if(plastic.getLocalScale().x > 3)
			plastic.setLocalScale(new Vector3f(1.2f,1,1));
	}
	
}
