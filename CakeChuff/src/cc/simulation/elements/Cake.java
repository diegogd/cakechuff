package cc.simulation.elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import cc.simulation.utils.ModelLoader;
import cc.simulation.utils.Rotations;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;

public class Cake extends Node{
	
	public boolean falling=true;
	public DisplaySystem mainDisplay = null;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4426672713022189512L;
	
	public Cake(DisplaySystem mainDisplay) {
		this.mainDisplay = mainDisplay;
		loadModel();		
		//loadCylinder();
	}
	
	public Cake(int id, DisplaySystem mainDisplay) {
		this.mainDisplay = mainDisplay;
		loadModel();		
		this.setName("Cake"+id);
	}
	
	public void changeTextureToChocolate(){
		TextureState ts = mainDisplay.getRenderer().createTextureState();
		ts.setTexture(TextureManager.loadTexture(this.getClass().getResource("/model/texture/cake/chocolate.jpg"),
				MinificationFilter.BilinearNearestMipMap, MagnificationFilter.Bilinear));
		this.setRenderState(ts);
	}
	
	public void changeTextureToCaramel(){
		TextureState ts = mainDisplay.getRenderer().createTextureState();
		ts.setTexture(TextureManager.loadTexture(this.getClass().getResource("/model/texture/cake/caramel.jpg"),
				MinificationFilter.BilinearNearestMipMap, MagnificationFilter.Bilinear));
		this.setRenderState(ts);
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
		TextureState ts = mainDisplay.getRenderer().createTextureState();
		ts.setTexture(TextureManager.loadTexture(this.getClass().getResource("/model/texture/cake/normal.jpg"),
				MinificationFilter.BilinearNearestMipMap, MagnificationFilter.Bilinear));
		this.setRenderState(ts);
	}
	

}
