package cc.simulation.elements;

import java.net.URL;

import cc.simulation.utils.ModelLoader;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
/**
 * Implementation and definition of the packet, one of
 * the simulation elements that compose CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Packet extends Node {
	
	
	Node pivot;
	
	
//	public Blister() {
////		loadModel();
////		loadBox();
//	}
	/**
	 * Constructor
	 * Initializes the packet and assigns an id to it.
	 * @param id Identification of the packet
	 */
	public Packet(String id) {
		pivot  = new Node();
		this.attachChild(pivot);
		
		loadModel();
		//loadBox(id);
		this.setName("Packet"+id);
		
	}
	/**
	 * Packet is graphically displayed as a box
	 */
	private void loadBox(String id){
		
		Box blister = new Box("packet"+id,new Vector3f(-2f,0.99f,-2f), new Vector3f(2f, 1f, 2f));
		
		blister.setModelBound(new BoundingBox());
		blister.updateModelBound();
		blister.setDefaultColor(ColorRGBA.brown);
		blister.updateRenderState();
		pivot.attachChild(blister);
	}
	/**
	 * Loads the graphical model of the packet
	 */
	private void loadModel(){
		URL model=getClass().getClassLoader().getResource("model/wrap.obj");
		Spatial loadedModel = ModelLoader.loadOBJ(model, false);
		
		float opacityAmount = 0.6f;
		// the sphere material taht will be modified to make the sphere
		// look opaque then transparent then opaque and so on
		MaterialState materialState = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		materialState
				.setDiffuse(new ColorRGBA(1f, 1f, 1f, opacityAmount));
		materialState.setEnabled(true);

		// IMPORTANT: this is used to handle the internal sphere faces when
		// setting them to transparent, try commenting this line to see what
		// happens
		materialState.setMaterialFace(MaterialState.MaterialFace.FrontAndBack);

		loadedModel.setRenderState(materialState);
		loadedModel.updateRenderState();

		BlendState alphaState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		alphaState.setBlendEnabled(true);
		alphaState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alphaState
				.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alphaState.setTestEnabled(true);
		alphaState.setTestFunction(BlendState.TestFunction.GreaterThan);
		alphaState.setEnabled(true);

		loadedModel.setRenderState(alphaState);
		loadedModel.updateRenderState();

		loadedModel.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		
		pivot.attachChild(loadedModel);
//		URL model2=getClass().getClassLoader().getResource("model/blister.obj");
//		Spatial aux = ModelLoader.loadOBJ(model2);
//		aux.setLocalScale(new Vector3f(0,-1,0));
//		aux.setLocalTranslation(0,0.5f,0);
//		pivot.attachChild(aux);
	}
}
