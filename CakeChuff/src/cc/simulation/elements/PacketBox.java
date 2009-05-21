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
	
	private Node pivot,packetsNode;
	
	private Vector<Spatial> packets; 	
	
	public PacketBox(String name){
		
		pivot = new Node();
		this.attachChild(pivot);
		packetsNode = new Node();
		this.attachChild(packetsNode);
		packets = new Vector<Spatial>();
		//loadBox();
		loadModel();
		this.setName(name);
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
		pivot.attachChild(ModelLoader.loadOBJ(model, false));
//		URL model2=getClass().getClassLoader().getResource("model/blister.obj");
//		Spatial aux = ModelLoader.loadOBJ(model2);
//		aux.setLocalScale(new Vector3f(0,-1,0));
//		aux.setLocalTranslation(0,0.5f,0);
//		pivot.attachChild(aux);
	}
	
	public void addInBox(Spatial element){
		packets.add(element);
		element.removeFromParent();
		packetsNode.attachChild(element);
		
		if(this.getLocalRotation().y > 0)	{
			//System.out.println("GoodBox: "+this.getLocalRotation().y);
			element.setLocalTranslation(14f, -5f+((float)numOfPackets()-1), 0);
		}else {
			//System.out.println("BadBox: "+this.getLocalRotation().y);
			element.setLocalTranslation(14f, -5f+((float)numOfPackets()-1),16f);
		}
			
	}
	
	public void emptyBox(){
		packets.clear();
		packetsNode.detachAllChildren();
//		this.setLocalTranslation(f, 0f, 0f);
	}
	
	public int numOfPackets(){
		return packets.size();
	}
	
}
