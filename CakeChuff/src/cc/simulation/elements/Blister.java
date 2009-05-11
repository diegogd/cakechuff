package cc.simulation.elements;

import java.net.URL;

import cc.simulation.utils.ModelLoader;
import cc.simulation.utils.Rotations;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Cylinder;

public class Blister extends Node {

	public boolean[] spaces = { false, false, false, false };

	Node pivot;

	Node hole1, hole2, hole3, hole4;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4426672713022189512L;

	// public Blister() {
	// // loadModel();
	// // loadBox();
	// }

	public Blister(int id) {
		pivot = new Node();
		this.attachChild(pivot);

		hole1 = new Node();
		hole2 = new Node();
		hole3 = new Node();
		hole4 = new Node();

		hole1.setLocalTranslation(-1, 0, -1);
		hole2.setLocalTranslation(1, 0, -1);
		hole3.setLocalTranslation(-1, 0, 1);
		hole4.setLocalTranslation(1, 0, 1);

		pivot.attachChild(hole1);
		pivot.attachChild(hole2);
		pivot.attachChild(hole3);
		pivot.attachChild(hole4);

		loadModel();
		// loadBox(id);
		this.setName("Blister" + id);

	}

	private void loadBox(int id) {

		Box blister = new Box("blister" + id, new Vector3f(-2f, 0.99f, -2f),
				new Vector3f(2f, 1f, 2f));

		blister.setModelBound(new BoundingBox());
		blister.updateModelBound();
		blister.setDefaultColor(ColorRGBA.brown);
		blister.updateRenderState();
		pivot.attachChild(blister);
	}

	private void loadModel() {
		URL model = getClass().getClassLoader()
				.getResource("model/blister.obj");
		pivot.attachChild(ModelLoader.loadOBJ(model));
	}

	public void placeCake(int hole, Spatial cake) {
		if (cake.removeFromParent()) {
			switch (hole) {
			case 1:
				hole1.attachChild(cake);
				spaces[hole-1]=true;
				break;
			case 2:
				hole2.attachChild(cake);
				spaces[hole-1]=true;
				break;
			case 3:
				hole3.attachChild(cake);
				spaces[hole-1]=true;
				break;
			case 4:
				hole4.attachChild(cake);
				spaces[hole-1]=true;
				break;
			default:
				break;
			}
		}
	}
}
