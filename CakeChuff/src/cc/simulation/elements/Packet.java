package cc.simulation.elements;

import java.net.URL;

import cc.simulation.utils.ModelLoader;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

public class Packet extends Node {
	
	
	Node pivot;
	
	
//	public Blister() {
////		loadModel();
////		loadBox();
//	}
	
	public Packet(int id) {
		pivot  = new Node();
		this.attachChild(pivot);
		
		loadModel();
		//loadBox(id);
		this.setName("Packet"+id);
		
	}
	
	private void loadBox(int id){
		
		Box blister = new Box("packet"+id,new Vector3f(-2f,0.99f,-2f), new Vector3f(2f, 1f, 2f));
		
		blister.setModelBound(new BoundingBox());
		blister.updateModelBound();
		blister.setDefaultColor(ColorRGBA.brown);
		blister.updateRenderState();
		pivot.attachChild(blister);
	}
	
	private void loadModel(){
		URL model=getClass().getClassLoader().getResource("model/blister.obj");
		pivot.attachChild(ModelLoader.loadOBJ(model));
//		URL model2=getClass().getClassLoader().getResource("model/blister.obj");
//		Spatial aux = ModelLoader.loadOBJ(model2);
//		aux.setLocalScale(new Vector3f(0,-1,0));
//		aux.setLocalTranslation(0,0.5f,0);
//		pivot.attachChild(aux);
	}
}
