package cc.simulation.elements;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;

public class PacketBox extends Node{
	
	private Blister blister;
	private Cake[] cakes;
	private Node pivot;
	
	public PacketBox(){
		
		pivot = new Node();
		this.attachChild(pivot);
		loadBox();
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
}
