package cc.simulation.elements;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import cc.simulation.subsystems.Factory;
import cc.simulation.utils.Rotations;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.system.DisplaySystem;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.model.md5.controller.MD5Controller;
import com.model.md5.importer.MD5Importer;
import com.model.md5.interfaces.IMD5Animation;
import com.model.md5.interfaces.IMD5Node;

/**
 * Implementation and definition of the mouse, one of the simulation elements
 * that compose CakeChuff system
 * 
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Mouse extends Node {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6490750570905100575L;
	/**
	 * The <code>MD5Importer</code> instance.
	 */
	private final MD5Importer importer;
	/**
	 * The loaded model mesh <code>IMD5Node</code>.
	 */
	private IMD5Node modelMesh;
	/**
	 * The base <code>IMD5Animation</code>.
	 */
	private IMD5Animation baseAnimation;
	/**
	 * The <code>MD5Controller</code> instance set on the model mesh.
	 */
	private MD5Controller controller;
	/**
	 * Main display
	 */
	private DisplaySystem display;

	public Vector3f direction;
	private float angledirection = 0f;

	private float counterMoving = 2;
	
	private float leftRadians = 0;

	/**
	 * Constructor Loads the graphical model of the mouse of the system
	 */
	public Mouse(DisplaySystem mainDisplay) {

		this.importer = MD5Importer.getInstance();
		this.display = mainDisplay;
		this.direction = new Vector3f(1f, 0, 0);

		loadModel();
		this.setLocalScale(0.03f);
		this.setLocalRotation(Rotations.rotateY(1));
	}

	/**
	 * Loads the graphical model of the mouse
	 */
	private void loadModel() {

		try {
			// File file = new File(this.dir + this.hierarchy.get(0) +
			// ".md5mesh");
			URL url = this.getClass().getResource("/model/mouse/mouse.md5mesh");
			ResourceLocatorTool.addResourceLocator(
					ResourceLocatorTool.TYPE_TEXTURE,
					new SimpleResourceLocator(url));
			this.loadModelMesh();
			this.loadAnimations();
			this.setupController();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		this.updateGeometricState(0, true);
	}

	public void simpleUpdate(float timeps, Factory father) {
		counterMoving += timeps;
		if (counterMoving > 1) {
			//float radians = (float) (FastMath.rand.nextFloat() * 0.5f) - 0.25f;
			leftRadians = (float) ((FastMath.rand.nextFloat() * 0.5f) - 0.25f)*FastMath.PI;
			// angledirection += radians * FastMath.PI;
			counterMoving = 0;
						
		}
		
		angledirection -= leftRadians*timeps;
		this.setLocalRotation(this.getWorldRotation().fromAngleAxis(
				angledirection, Vector3f.UNIT_Y));
		direction.x = (float) FastMath.cos(angledirection);
		direction.z = (float) -FastMath.sin(angledirection);
		
		this.updateAutomatic(timeps);

		this.getLocalTranslation().addLocal(direction.mult(8 * timeps));
		
		if (this.getLocalTranslation().x > 60 || this.getLocalTranslation().x < -40 || 
			this.getLocalTranslation().z > 30 || this.getLocalTranslation().z < -40){
			direction.z *= -1;
			float radians = (float) (FastMath.rand.nextFloat() * 0.5f) - 0.25f;			
			angledirection += radians*FastMath.PI + FastMath.PI;
			this.setLocalRotation(this.getWorldRotation().fromAngleAxis(
					angledirection, Vector3f.UNIT_Y));
			direction.x = (float) FastMath.cos(angledirection);
			direction.z = (float) -FastMath.sin(angledirection);
			this.getLocalTranslation().addLocal(direction.mult(8 * timeps));
		}
	}

	/**
	 * Update the automatic control.
	 */
	private void updateAutomatic(float timeps) {
		this.controller.update(timeps);
		if (this.controller.getActiveAnimation().isCyleComplete())
			this.controller.getActiveAnimation().reset();
	}

	/**
	 * Reset the animation back to the base animation.
	 */
	private void resetAnimation() {
		this.controller.setRepeatType(Controller.RT_WRAP);
		this.controller.fadeTo(this.baseAnimation, 0.2f, false);
	}

	/**
	 * Load the mesh of the model based on the hierarchy.
	 * 
	 * @throws IOException
	 *             If the loading process is interrupted.
	 */
	private void loadModelMesh() throws IOException {
		// Load the base model mesh.
		URL url = this.getClass().getResource("/model/mouse/mouse.md5mesh");
		this.importer.loadMesh(url, "Mesh0");
		this.modelMesh = this.importer.getMD5Node();
		this.importer.cleanup();

		// Add blend state.
		BlendState blend = this.display.getRenderer().createBlendState();
		blend.setBlendEnabled(true);
		blend.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		blend
				.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		blend.setTestEnabled(true);
		blend.setTestFunction(BlendState.TestFunction.GreaterThan);
		blend.setEnabled(true);
		((Spatial) this.modelMesh).setRenderState(blend);
		// Attach to root node.
		((Spatial) this.modelMesh).setLocalRotation(Rotations.rotateY(1));
		this.attachChild((Spatial) this.modelMesh);
	}

	/**
	 * Load the animations from the URL links.
	 * 
	 * @throws IOException
	 *             If the loading process is interrupted.
	 */
	private void loadAnimations() throws IOException {
		// Load the base animation.
		this.importer.loadAnim(this.getClass().getResource(
				"/model/mouse/mouse.md5anim"), "BaseAnimation");
		this.baseAnimation = this.importer.getAnimation();
		this.importer.cleanup();
	}

	/**
	 * Set the controller on the loaded model mesh.
	 */
	private void setupController() {
		this.controller = new MD5Controller(this.modelMesh);
		this.controller.setActive(true);
		this.modelMesh.addController(this.controller);
		// Set proper starting animation.

		if (this.baseAnimation != null)
			this.resetAnimation();
	}

}
