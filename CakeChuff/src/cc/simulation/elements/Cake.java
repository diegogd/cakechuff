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
import com.jme.bounding.BoundingVolume;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;
/**
 * Implementation and definition of the cake, one of
 * the simulation elements that compose CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Cake extends Node{
	
	public boolean falling=true;
	public DisplaySystem mainDisplay = null;
	public boolean withChocolate = false;
	public boolean withCaramel = false;
	
	
	private static final long serialVersionUID = 4426672713022189512L;
	/**
	 * Constructor
	 * Loads the graphical model of the cake of the system
	 * @param mainDisplay The principal display of the simulation
	 */
	public Cake(DisplaySystem mainDisplay) {
		this.mainDisplay = mainDisplay;
		loadModel();		
		//loadCylinder();
	}
	/**
	 * Constructor
	 * Loads the graphical model of the cake of the system
	 * and assigns an identification to the cake
	 * @param id The identification of the cake 
	 * @param mainDisplay The principal display of the simulation
	 */
	public Cake(int id, DisplaySystem mainDisplay) {
		this.mainDisplay = mainDisplay;
		loadModel();		
		this.setName("Cake"+id);
	}
	/**
	 * Modifies the texture of the liquid that drops
	 * in the cake to chocolate
	 */
	public void changeTextureToChocolate(){
		TextureState ts = mainDisplay.getRenderer().createTextureState();
		ts.setTexture(TextureManager.loadTexture(this.getClass().getResource("/model/texture/cake/chocolate.jpg"),
				MinificationFilter.BilinearNearestMipMap, MagnificationFilter.Bilinear));
		this.setRenderState(ts);
		this.updateRenderState();
		this.withChocolate = true;
	}
	/**
	 * Modifies the texture of the liquid that drops
	 * in the cake to caramel and chocolate
	 */
	public void changeTextureToCaramelAndChocolate(){
		TextureState ts = mainDisplay.getRenderer().createTextureState();
		ts.setTexture(TextureManager.loadTexture(this.getClass().getResource("/model/texture/cake/choccaram.jpg"),
				MinificationFilter.BilinearNearestMipMap, MagnificationFilter.Bilinear));
		this.setRenderState(ts);
		this.updateRenderState();
		this.withCaramel = true;
		this.withChocolate = true;
	}
	/**
	 * Modifies the texture of the liquid that drops
	 * in the cake to caramel
	 */
	public void changeTextureToCaramel(){
		TextureState ts = mainDisplay.getRenderer().createTextureState();
		ts.setTexture(TextureManager.loadTexture(this.getClass().getResource("/model/texture/cake/caramel.jpg"),
				MinificationFilter.BilinearNearestMipMap, MagnificationFilter.Bilinear));
		this.setRenderState(ts);
		this.updateRenderState();
		this.withCaramel = true;
	}
	/**
	 * Cake is graphically displayed as a cylinder
	 */
	private void loadCylinder(){
		Cylinder cake = new Cylinder("cake",4, 20, 0.7f, 0.5f, true);
		cake.setLocalRotation(Rotations.rotateX(0.5f));
		cake.setModelBound(new BoundingBox());
		cake.updateModelBound();
		cake.setDefaultColor(ColorRGBA.brown);
		cake.updateRenderState();
		this.attachChild(cake);
	}
	/**
	 * Loads the graphical model of the cake
	 */
	private void loadModel(){
		URL model=getClass().getClassLoader().getResource("model/cake.obj");
		Spatial cake = ModelLoader.loadOBJ(model, true);
		this.attachChild(cake);
		TextureState ts = mainDisplay.getRenderer().createTextureState();
		ts.setTexture(TextureManager.loadTexture(this.getClass().getResource("/model/texture/cake/normal.jpg"),
				MinificationFilter.BilinearNearestMipMap, MagnificationFilter.Bilinear));
		this.setRenderState(ts);
		
		
	}
	/**
	 * Returns if the cake has chocolate
	 * @return withChocolate True if the cake has chocolate, false if it does not
	 */
	public boolean isWithChocolate() {
		return withChocolate;
	}
	/**
	 * Returns if the cake has caramel
	 * @return withCaramel True if the cake has caramel, false if it does not
	 */
	public boolean isWithCaramel() {
		return withCaramel;
	}
}
