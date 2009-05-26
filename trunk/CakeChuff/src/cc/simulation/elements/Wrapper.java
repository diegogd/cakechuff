package cc.simulation.elements;

import cc.simulation.utils.ModelLoader;
import cc.simulation.utils.Rotations;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * Implementation and definition of the wrapper, one of the simulation elements
 * that compose CakeChuff system
 * 
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Wrapper extends Node {

	private float speed;
	private boolean direction;

	public boolean finished = true;

	Node pivot;

	/**
	 * Constructor Initializes the speed, direction and pivot of the wrapper. It
	 * also loads its graphical model
	 */
	public Wrapper() {

		speed = 0;
		direction = false;
		pivot = new Node();
		loadShape();
		this.attachChild(pivot);
		this.setName("Wrapper");
		this.setLocalRotation(Rotations.rotateY(1));
	}

	/**
	 * Returns the speed of the wrapper
	 * 
	 * @return The speed of the wrapper
	 */
	public float getSpeed() {
		return this.speed;
	}

	/**
	 * Modifies the speed of the wrapper
	 * 
	 * @param speed
	 *            The new speed of the wrapper
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * Loads the graphical shape of the wrapper
	 */
	public void loadShape() {
		pivot.attachChild(ModelLoader.loadOBJ(getClass().getClassLoader()
				.getResource("model/engraver2.obj"), true));
	}

	/**
	 * Updates the state of the wrapper when an element wants to be wrapped
	 * 
	 * @param timeperframes
	 *            Parameter used in the translation of the wrapper
	 * @param element
	 *            Element (a blister) that want to be wrapped up
	 */
	public void update(float timeperframes, Spatial element) {
		System.out.println("Wrapper called:" + pivot.getLocalTranslation().y
				+ "# Speed:" + speed + " Timer: " + timeperframes+ " Desplazamiento:" + speed
				* timeperframes );
		if (speed > 0) {
			System.out.println("Wrapper moving:"
					+ pivot.getLocalTranslation().y + "# Speed:" + speed+ " Timer: " + timeperframes
					+ " Desplazamiento:" + speed * timeperframes);
			if (direction) {
				if (pivot.getLocalTranslation().y >= 0.0f) {
					pivot.getLocalTranslation().y = 0.0f;
					direction = false;

					// Approach 2
					finished = true;
					speed = 0;
					// System.out.println("Finished!!");
				} else {
					if (timeperframes < 1 && timeperframes > 0) {
						pivot.getLocalTranslation().y += speed * timeperframes;
					}
				}
			} else {

				// if (pivot.getLocalTranslation().y <= -1.6f) {
				if (pivot.hasCollision(element, false)
						&& (pivot.getLocalTranslation().y <= -2.6f)) {
					// pivot.getLocalTranslation().y = -1.6f;
					direction = true;

					WrapUp(element);
					// Cargar Blister?

				} else if (pivot.getLocalTranslation().y <= -2.6f) {
					direction = true;
				} else {
					if (timeperframes < 1 && timeperframes > 0) {
						pivot.getLocalTranslation().y -= speed * timeperframes;
					}
				}
				finished = false;
			}
		}
	}

	/**
	 * Wrap an element (a blister)
	 * 
	 * @param element
	 *            Element that want to be wrapped up
	 */
	public void WrapUp(Spatial element) {

		if (element instanceof Blister) {
			System.out.println("Wrapped!!");
			Packet wrapper = new Packet(((Blister) element).getName());
			((Blister) element).placeWrapper(wrapper);
		}
	}
}
