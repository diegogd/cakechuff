package cc.simulation.elements;

import cc.simulation.subsystems.Factory;
import cc.simulation.utils.ModelLoader;
import cc.simulation.utils.Rotations;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;


/**
 * Implementation and definition of the Cake's Container, one of
 * the simulation elements that compose CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */

public class CakesContainer extends Node {

	/**
	 * Constructor Initializes creates and loads the graphical model object.
	 */
	public CakesContainer(DisplaySystem display) {

		loadShape();
		this.setName("CakeContainer");
		this.setLocalRotation(Rotations.rotateY(1));

		Box plasticBox = new Box("CakeContainerBox", new Vector3f(-0.75f, -2f,
				-0.75f), new Vector3f(0.75f, 2f, 0.75f));
		plasticBox.setLocalTranslation(0f, -6f, 0f);

		float opacityAmount = 0.3f;
		// the sphere material taht will be modified to make the sphere
		// look opaque then transparent then opaque and so on
		MaterialState materialState = display.getRenderer()
				.createMaterialState();
//		materialState
//				.setAmbient(new ColorRGBA(0.0f, 0.0f, 0.0f, opacityAmount));
		materialState
				.setDiffuse(new ColorRGBA(1f, 1f, 1f, opacityAmount));
//		materialState
//				.setSpecular(new ColorRGBA(1.0f, 1.0f, 1.0f, opacityAmount));
//		materialState.setShininess(128.0f);
//		materialState
//				.setEmissive(new ColorRGBA(0.0f, 0.0f, 0.0f, opacityAmount));
		materialState.setEnabled(true);

		// IMPORTANT: this is used to handle the internal sphere faces when
		// setting them to transparent, try commenting this line to see what
		// happens
		materialState.setMaterialFace(MaterialState.MaterialFace.FrontAndBack);

		plasticBox.setRenderState(materialState);
		plasticBox.updateRenderState();

		BlendState alphaState = display.getRenderer().createBlendState();
		alphaState.setBlendEnabled(true);
		alphaState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alphaState
				.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alphaState.setTestEnabled(true);
		alphaState.setTestFunction(BlendState.TestFunction.GreaterThan);
		alphaState.setEnabled(true);

		plasticBox.setRenderState(alphaState);
		plasticBox.updateRenderState();

		plasticBox.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);

		this.attachChild(plasticBox);

	}

	/**
	 * Loads the graphical shape of the wrapper
	 */
	public void loadShape() {

		Spatial model = ModelLoader.loadOBJ(getClass().getClassLoader()
				.getResource("model/engraver2.obj"), false);
		model.setLocalScale(new Vector3f(1.2f, -1.2f, 1.2f));
		this.attachChild(model);
	}

}
