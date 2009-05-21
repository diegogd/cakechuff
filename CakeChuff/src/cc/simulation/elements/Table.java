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

public class Table extends Node{

	List<Spatial> objectsInTable;
	
	public Table(){
	//	loadBox();
		loadModel();
		objectsInTable = new Vector<Spatial>();
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

	private void loadModel(){
		URL model=getClass().getClassLoader().getResource("model/table.obj");
		this.attachChild(ModelLoader.loadOBJ(model, false));
	}

	public void addBlister(Spatial takenObject) {
		objectsInTable.add(takenObject);
	}
	
	public List<Spatial> getObjects(){
		return objectsInTable;
	}
	
}
