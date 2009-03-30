package cc.simulation.elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import cc.simulation.utils.ModelLoader;
import cc.simulation.utils.Rotations;

import com.jme.bounding.BoundingBox;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Cylinder;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;

public class Cake extends Node{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4426672713022189512L;
	
	public Cake() {
		loadModel();
		//loadCylinder();
	}
	
	private void loadCylinder(){
		Cylinder cake = new Cylinder("cake",4, 20, 0.7f, 0.5f, true);
		cake.setLocalRotation(Rotations.rotateX(0.5f));
		cake.setModelBound(new BoundingBox());
		cake.updateModelBound();
		cake.setDefaultColor(ColorRGBA.brown);
		cake.updateRenderState();
		this.attachChild(cake);
	}
	
	private void loadModel(){
		URL model=getClass().getClassLoader().getResource("model/cake.obj");
		this.attachChild(ModelLoader.loadOBJ(model));
	}
	

}
