package cc.simulation.elements;

import cc.simulation.utils.ModelLoader;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class Wrapper extends Node {

	private float speed;
	private boolean direction;

	public boolean finished = true;

	Node pivot;

	public Wrapper() {

		speed = 0;
		direction = false;
		pivot = new Node();
		loadShape();
		this.attachChild(pivot);
		this.setName("Wrapper");
	}

	public float getSpeed() {
		return this.speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void loadShape() {
		pivot.attachChild(ModelLoader.loadOBJ(getClass().getClassLoader()
				.getResource("model/engraver.obj"), true));
	}

	public void update(float timeperframes, Spatial element) {
		// System.out.println(pivot.getLocalTranslation().y);
		if (speed > 0) {

			if (direction) {
				if (pivot.getLocalTranslation().y >= 0.0f) {
					pivot.getLocalTranslation().y = 0.0f;
					direction = false;

					// Approach 2
					finished = true;
					speed = 0;
//					System.out.println("Finished!!");
				} else {
					pivot.getLocalTranslation().y += speed * timeperframes;
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
					pivot.getLocalTranslation().y -= speed * timeperframes;
				}
				finished = false;
			}
		}
	}

	public void WrapUp(Spatial element) {

		if (element instanceof Blister) {
			System.out.println("Wrapped!!");
			Packet wrapper = new Packet(((Blister) element).getName());
			((Blister) element).placeWrapper(wrapper);
		}
	}
}
