package cc.simulation.elements;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;

public class Table extends Node{

	public Table(){
		loadBox();
	}
	
	private void loadBox(){
		Box table = new Box("table",new Vector3f(-1,-1,-1),new Vector3f(1,1,1));
		table.setModelBound(new BoundingBox());
		table.updateModelBound();
		table.setDefaultColor(ColorRGBA.brown);
		
		table.setLocalScale(new Vector3f(1.5f,1.85f,2f));
		
		table.updateRenderState();
		this.attachChild(table);
	}

}
