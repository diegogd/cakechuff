package cc.simulation.elements;

import java.net.URL;
import java.util.List;
import java.util.Vector;

import cc.simulation.utils.ModelLoader;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

public class PacketBox extends Node{
	
	private Node pivot;
	
	private Vector<Spatial> packets; 	
	
	public PacketBox(String name){
		
		pivot = new Node();
		this.attachChild(pivot);
		
		packets = new Vector<Spatial>();
		//loadBox();
		loadModel();
		this.setName(name);
//		this.blister = blister;
//		this.cakes = cakes;
//		pivot = new Node();
//		pivot.attachChild(this.blister);
//		for(int i = 0; i<cakes.length;i++)
//			pivot.attachChild(this.cakes[i]);
	}
	
	private void loadBox(){
		Box table = new Box("PacketBox",new Vector3f(-1,-1,-1),new Vector3f(1,1,1));
		table.setModelBound(new BoundingBox());
		table.updateModelBound();
		table.setDefaultColor(ColorRGBA.brown);
		
		//table.setLocalScale(new Vector3f(1.5f,1.85f,2f));
		
		table.updateRenderState();
		pivot.attachChild(table);
	}
	
	private void loadModel(){
		URL model=getClass().getClassLoader().getResource("model/box.obj");
		pivot.attachChild(ModelLoader.loadOBJ(model));
//		URL model2=getClass().getClassLoader().getResource("model/blister.obj");
//		Spatial aux = ModelLoader.loadOBJ(model2);
//		aux.setLocalScale(new Vector3f(0,-1,0));
//		aux.setLocalTranslation(0,0.5f,0);
//		pivot.attachChild(aux);
	}
	
	public void addInBox(Spatial element){
		packets.add(element);
		element.removeFromParent();
		pivot.attachChild(element);
		element.setLocalTranslation(0, (float)numOfPackets()-1, 0);
	}
	
	public int numOfPackets(){
		return packets.size();
	}
	
}
