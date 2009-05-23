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
/**
 * Implementation and definition of the tables, one of
 * the simulation elements that compose CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Table extends Node{

	List<Spatial> objectsInTable;
	/**
	 * Constructor
	 * Initializes the table and loads the graphic model
	 */
	public Table(){
	//	loadBox();
		loadModel();
		objectsInTable = new Vector<Spatial>();
	}
	/**
	 * Table is graphically displayed as a box
	 */
	private void loadBox(){
		Box table = new Box("table",new Vector3f(-1,-1,-1),new Vector3f(1,1,1));
		table.setModelBound(new BoundingBox());
		table.updateModelBound();
		table.setDefaultColor(ColorRGBA.brown);
		
		table.setLocalScale(new Vector3f(1.5f,1.85f,2f));
		
		table.updateRenderState();
		this.attachChild(table);
	}
	/**
	 * Loads the graphical model of the table
	 */
	private void loadModel(){
		URL model=getClass().getClassLoader().getResource("model/table.obj");
		this.attachChild(ModelLoader.loadOBJ(model, false));
	}
	/**
	 * Puts a blister in the table
	 * @param takenObject Blister that wants to be put in the table
	 */
	public void addBlister(Spatial takenObject) {
		objectsInTable.add(takenObject);
	}
	/**
	 * Returns a list of the objects in the table
	 * @return List of the objects in the table
	 */
	public List<Spatial> getObjects(){
		return objectsInTable;
	}
	
}
