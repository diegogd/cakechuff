package cc.simulation.elements;

import java.io.File;
import java.net.URL;

import javax.swing.Box;

import cc.simulation.subsystems.Factory;
import cc.simulation.utils.ModelLoader;
import cc.simulation.utils.Rotations;

import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Cylinder;
/**
 * Implementation and definition of the valves, one of
 * the simulation elements that compose CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Valve extends Node{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -157688181210068308L;
	/**
	 * 
	 */
	private Cylinder content, liquidstream;
	private Node contentPivot, liquidPivotUp, liquidPivotDown;
	
	private float timeOpen = 0;
	private float totalAmount = 100;
	boolean valveCompleteOpen = false, valveActived = false;
	/**
	 * Constructor
	 * Initializes the touch sensor and assigns an id to it
	 * @param id Identification of the touch sensor
	 */
	public Valve(String id, ColorRGBA color) {
		this.setName(id);
		loadShape();
		
		// Container liquid
		content = new Cylinder("contentValve", 10, 20, 1f, 2.5f, true );

		content.getLocalTranslation().z -= 1f;
		content.setDefaultColor(color);
		
		contentPivot = new Node();
		contentPivot.attachChild(content);
		contentPivot.getLocalRotation().fromAngles(new float[]{FastMath.PI/2,0,0});
		
		// Liquid Stream
		liquidstream = new Cylinder("liquidStream", 10, 20, 0.06f, 1.2f, true);
		liquidstream.getLocalTranslation().z -= 0.6f;
		liquidstream.setDefaultColor(color);
		liquidstream.setModelBound(new BoundingBox());
		liquidstream.updateModelBound();
		
		liquidPivotUp = new Node();
		liquidPivotUp.attachChild(liquidstream);
		
		liquidPivotDown = new Node();
		
		//liquidPivotDown.getLocalTranslation().z -= 1f;
		liquidPivotDown.attachChild(liquidPivotUp);
		
		
		
		//liquidPivotUp.getLocalTranslation().z += 3.2f;
		liquidPivotDown.getLocalRotation().fromAngles(new float[]{-FastMath.PI/2,0,0});
		liquidPivotUp.getLocalTranslation().z += 3.8f;	
		liquidPivotDown.getLocalTranslation().y -= 4f;		
		
		
		this.attachChild(contentPivot);
		//this.attachChild(liquidPivotUp);
		this.attachChild(liquidPivotDown);
	}
	/**
	 * Loads the graphical shape of the valve
	 */
	public void loadShape(){
		
		URL path = getClass().getClassLoader().getResource("model/valve.obj");
		
		this.attachChild(ModelLoader.loadOBJ(path, false));
	}
	/**
	 * Updates the state of the valve
	 * @param timeperframes Parameter used to modify the liquid pivot
	 */
	public void update(float timeperframes)
	{
		if(contentPivot.getLocalScale().z < 0.02f) contentPivot.getLocalScale().z = 1;
//		if(pivot.getLocalScale().y < 0.2f) pivot.getLocalScale().y = 1;
		totalAmount--;
		if(timeOpen > 0){
			valveActived = true;
			timeOpen -= timeperframes;
			contentPivot.getLocalScale().z -= timeperframes*0.01;
			if(!valveCompleteOpen){
				liquidPivotUp.getLocalScale().z += timeperframes*6;
				if(liquidPivotUp.getLocalScale().z > 3.2){
					valveCompleteOpen=true;
				}
			}
		} else if(valveActived){
			liquidPivotDown.getLocalScale().z -= timeperframes*3;
			valveCompleteOpen=false;
			if(liquidPivotDown.getLocalScale().z <= 0.02f){
				valveActived = false;
				liquidPivotDown.getLocalScale().z = 1f;
				liquidPivotUp.getLocalScale().z = 1f;
			}
		}
	}
	/**
	 * Checks for a collision with the liquid stream
	 * @param element The element that wants to be known if the liquid has "collapsed" with
	 * @return true If no collision occured, false if yes.
	 */
	public boolean checkCollision(Spatial element){
		if(valveCompleteOpen && liquidstream.hasCollision(element, false)){
			return true;
		}
		return false;
	}
	/**
	 * Opens the valve a specified time
	 * @param seconds Time the valve is opened
	 */
	public void open(float seconds)
	{
		timeOpen = seconds;
	}
	
}
