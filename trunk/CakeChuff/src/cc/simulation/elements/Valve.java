package cc.simulation.elements;

import java.io.File;
import java.net.URL;

import javax.swing.Box;

import cc.simulation.subsystems.Factory;
import cc.simulation.utils.ModelLoader;
import cc.simulation.utils.Rotations;

import com.jme.animation.SpatialTransformer;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Cylinder;

public class Valve extends Node{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -157688181210068308L;
	/**
	 * 
	 */
	private Cylinder content;
	private Node pivot;
	private Cylinder liquid;
	
	private float timeOpen = 0;
	private float totalAmount = 100;
	
	public Valve(String id, ColorRGBA color) {
		this.setName(id);
		loadShape();
		float size = 0.5f;
		content = new Cylinder("contentValve", 10, 20, 1f, 2.5f, true );
		
		content.getLocalTranslation().z -= 1f;
		content.setDefaultColor(color);
		pivot = new Node();
		pivot.attachChild(content);
		pivot.getLocalRotation().fromAngles(new float[]{FastMath.PI/2,0,0});
		this.attachChild(pivot);
	}

	public void loadShape(){
		
		URL path = getClass().getClassLoader().getResource("model/valve.obj");
		
		this.attachChild(ModelLoader.loadOBJ(path));
	}
	
	public void update(float timeperframes)
	{
		if(pivot.getLocalScale().z < 0.02f) pivot.getLocalScale().z = 1;
//		if(pivot.getLocalScale().y < 0.2f) pivot.getLocalScale().y = 1;
		totalAmount--;
		if(timeOpen > 0){
			timeOpen -= timeperframes;
//			pivot.getLocalScale().z -= timeperframes*0.3;
			pivot.getLocalScale().z -= timeperframes*0.05;
		}	
	}
	
	public void open(float seconds)
	{
		timeOpen = seconds;
	}
	
	public void close(float timeperframe)
	{
		
	}
}
