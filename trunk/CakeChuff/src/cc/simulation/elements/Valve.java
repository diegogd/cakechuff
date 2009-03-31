package cc.simulation.elements;

import java.io.File;
import java.net.URL;

import javax.swing.Box;

import cc.simulation.subsystems.Factory;
import cc.simulation.utils.ModelLoader;
import cc.simulation.utils.Rotations;

import com.jme.animation.SpatialTransformer;
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
	
	public Valve(String id) {
		this.setName(id);
		loadShape();
		content = new Cylinder("contentValve", 10, 20, 1f, 1.5f, true );
		content.setLocalRotation(Rotations.rotateX(-0.5f));
		content.getLocalTranslation().y += 1f;
		content.setDefaultColor(ColorRGBA.brown);
		pivot = new Node();
		// pivot2.setLocalTranslation(0, -5, 0);
		pivot.attachChild(content);
		// pivot.attachChild(pivot2);
		// pivot.getController(0).
		this.attachChild(pivot);
	}

	public void loadShape(){
		
		URL path = getClass().getClassLoader().getResource("model/valve.obj");
		
		this.attachChild(ModelLoader.loadOBJ(path));
	}
	
	public void update(float timeperframes)
	{
		if(pivot.getLocalScale().z < 0.2f) pivot.getLocalScale().z = 1;
		totalAmount--;
		if(timeOpen > 0){
			timeOpen -= timeperframes;
			pivot.getLocalScale().z -= timeperframes*0.5;		
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
