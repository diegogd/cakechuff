package cc.simulation.elements;

import java.io.IOException;
import java.net.URL;

import cc.simulation.utils.ModelLoader;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
/**
 * Implementation and definition of the Blister, one of
 * the simulation elements that compose CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Blister extends Node {

	public boolean[] spaces = { false, false, false, false };
	public boolean wrapperIsPlaced = false;

	Node pivot;

	Node hole1, hole2, hole3, hole4,wrapper;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4426672713022189512L;

	// public Blister() {
	// // loadModel();
	// // loadBox();
	// }

	/**
	 * Constructor
	 * Assigns an id and initializes the holes, the wrapper and the
	 * pivot that compose the blister.
	 * @param id Blister identification 
	 */
	public Blister(int id) {
		pivot = new Node();
		this.attachChild(pivot);

		hole1 = new Node();
		hole2 = new Node();
		hole3 = new Node();
		hole4 = new Node();
		
		wrapper = new Node();

		
//		Box base = new Box("base", new Vector3f(-0.5f,-0.5f,-0.5f),
//				new Vector3f(0.5f, 0.5f, 0.5f));
//		base.setDefaultColor(ColorRGBA.cyan);
//		pivot.attachChild(base);
		
		pivot.attachChild(hole1);
		pivot.attachChild(hole2);
		pivot.attachChild(hole3);
		pivot.attachChild(hole4);
		
		pivot.attachChild(wrapper);
//		pivot.setLocalTranslation(-5.2f,0f,7.4f);
		
//		hole1.setLocalTranslation(-1, 0, -1);
		hole1.setLocalTranslation(-6.45f,4.7f,-8.3f);
//		hole1.setLocalTranslation(-1f, 0.1f, );
//		Box box1 = new Box("box1", new Vector3f(-0.5f,-0.5f,-0.5f),
//				new Vector3f(0.5f, 0.5f, 0.5f));
//		box1.setDefaultColor(ColorRGBA.red);
//		hole1.attachChild(box1);
		
//		hole2.setLocalTranslation(1, 0, -1);
		hole2.setLocalTranslation(-4.5f,4.7f,-8.3f);
//		Box box2 = new Box("box2", new Vector3f(-0.5f,-0.5f,-0.5f),
//				new Vector3f(0.5f, 0.5f, 0.5f));
//		hole2.attachChild(box2);
//		box2.setDefaultColor(ColorRGBA.yellow);
		
//		hole3.setLocalTranslation(-1, 0, 1);
		hole3.setLocalTranslation(-6.45f,4.7f,-6.8f);
//		Box box3 = new Box("box3", new Vector3f(-0.5f,-0.5f,-0.5f),
//				new Vector3f(0.5f, 0.5f, 0.5f));
//		hole3.attachChild(box3);
//		box3.setDefaultColor(ColorRGBA.green);
		
		
//		hole4.setLocalTranslation(1, 0, 1);
		hole4.setLocalTranslation(-4.5f,4.7f,-6.8f);
//		Box box4 = new Box("box4", new Vector3f(-0.5f,-0.5f,-0.5f),
//				new Vector3f(0.5f, 0.5f, 0.5f));
//		hole4.attachChild(box4);
//		box4.setDefaultColor(ColorRGBA.orange);
				

		loadModel();
		// loadBox(id);
		this.setName("Blister" + id);

	}
	/**
	 * Specific blister is graphically displayed as a box
	 * @param id Blister identification
	 */
	private void loadBox(int id) {

		Box blister = new Box("blister" + id, new Vector3f(-2f, 0.99f, -2f),
				new Vector3f(2f, 1f, 2f));

		blister.setModelBound(new BoundingBox());
		blister.updateModelBound();
		blister.setDefaultColor(ColorRGBA.brown);
		blister.updateRenderState();
		pivot.attachChild(blister);
	}
	/**
	 * Loads the graphical model of the blister
	 */
	private void loadModel() {
		URL model = getClass().getClassLoader()
				.getResource("model/blister.obj");
		pivot.attachChild(ModelLoader.loadOBJ(model, true));
	}
	/**
	 * Returns the hole 1 of the blister 
	 * @return hole1 One of the holes of the blister
	 */
	public Spatial getHole1() {
		return hole1;
	}
	/**
	 * Returns the hole 2 of the blister 
	 * @return hole2 One of the holes of the blister
	 */
	public Spatial getHole2() {
		return hole2;
	}
	/**
	 * Returns the hole 3 of the blister 
	 * @return hole3 One of the holes of the blister
	 */
	public Spatial getHole3() {
		return hole3;
	}
	/**
	 * Returns the hole 4 of the blister 
	 * @return hole4 One of the holes of the blister
	 */
	public Spatial getHole4() {
		return hole4;
	}
	/**
	 * Places a cake in the first empty hole of the
	 * blister found.
	 * @param cake The cake to be placed in the hole
	 */
	public void placeCake(Spatial cake) {
		if (cake.removeFromParent()) {
			if(!spaces[0]){
				hole1.attachChild(cake);
				spaces[0]=true;
			}else if(!spaces[1]){
				hole2.attachChild(cake);
				spaces[1]=true;
			}else if(!spaces[2]){
				hole3.attachChild(cake);
				spaces[2]=true;
			}else if(!spaces[3]){
				hole4.attachChild(cake);
				spaces[3]=true;
			}
		}
	}
	/**
	 * Places the wrapper that covers the blister
	 * @param wrapper The wrapper of the blister 
	 */
	public void placeWrapper(Spatial wrapper) {
		//if (wrapper.removeFromParent()) {
				this.wrapper.attachChild(wrapper);
				this.wrapper.updateRenderState();
//				this.wrapper.setLocalScale(-1f);
//				wrapper.setLocalTranslation(10.9f, -9.55f, 15.2f);
				//wrapper.setLocalScale(-1.1f);
				wrapper.updateRenderState();
				
				wrapperIsPlaced = true;			
		//}
	}
}
