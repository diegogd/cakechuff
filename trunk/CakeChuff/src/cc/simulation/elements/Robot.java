package cc.simulation.elements;

import cc.simulation.utils.Rotations;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Cylinder;

public class Robot extends Node {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2973029142743347671L;
	// State to know if it has taken an object
	public boolean has_object;
	boolean _moving;

	private float speed; // modificar la velocidad

	// Draw Robot
	private Cylinder lowerBody;
	private Cylinder upperBody;
	private Cylinder base;

	private Cylinder lowerHeadLeft, lowerHeadRight;
	private Cylinder upperHeadLeft, upperHeadRight;

	private float angleBody;
	private float angleFloor;
	private float angleClaws;
	private Quaternion rotFloor = new Quaternion();
	private Quaternion rotBody = new Quaternion();
	private Quaternion rotClawLeft = new Quaternion();
	private Quaternion rotClawRight = new Quaternion();

	private Vector3f positionRobot;

	private Node pivotBase, pivotBody, pivotHead, pivotHeadLeft,
			pivotHeadRight;

	public Robot(Vector3f positionRobot) {
		has_object = false;
		_moving = false;
		this.positionRobot = new Vector3f(positionRobot);
		this.angleBody = 0;
		this.angleFloor = 0;

		loadTempModel();

		// Colocar Robot en la posicion
		this.setLocalTranslation(this.positionRobot);
	}

	private void loadTempModel() {

		base = new Cylinder("base", 5, 25, 0.2f, 0.1f, true);
		base.setLocalRotation(Rotations.rotateX(0.5f));
		base.setModelBound(new BoundingBox());
		base.updateModelBound();
		base.setDefaultColor(ColorRGBA.green);
		base.updateRenderState();
		this.attachChild(base);

		lowerBody = new Cylinder("lowerBody", 5, 25, 0.1f, 0.6f, true);
		lowerBody.setLocalRotation(Rotations.rotateX(0.5f));
		lowerBody.setLocalTranslation(0, 0.35f, 0);
		lowerBody.setDefaultColor(ColorRGBA.red);

		lowerBody.setModelBound(new BoundingBox());
		lowerBody.updateModelBound();
		lowerBody.updateRenderState();

		pivotBase = new Node();
		pivotBase.setLocalRotation(Rotations.rotateX(-0.1f));
		pivotBase.attachChild(lowerBody);
		this.attachChild(pivotBase);

		upperBody = new Cylinder("upperBody", 5, 25, 0.1f, 0.6f, true);
		upperBody.setLocalRotation(Rotations.rotateX(0.5f));
		upperBody.setLocalTranslation(0, 0.3f, 0);
		upperBody.setModelBound(new BoundingBox());
		upperBody.updateModelBound();
		// upperBody.setDefaultColor(ColorRGBA.blue);
		upperBody.setRandomColors();
		upperBody.updateRenderState();

		pivotBody = new Node();
		// pivot.setLocalRotation(Rotations.rotateX(0.5f));
		pivotBody.setLocalTranslation(0, 0.65f, 0);
		pivotBody.attachChild(upperBody);

		// Cylinder prueba = new Cylinder("prueba", 5, 25, 0.4f, 0.05f, true);
		// prueba.setLocalRotation(Rotations.rotateX(0.5f));
		// prueba.setDefaultColor(ColorRGBA.blue);
		// pivotBody.attachChild(prueba);

		pivotBase.attachChild(pivotBody);

		pivotHead = new Node();
		pivotHead.setLocalTranslation(0, 0.675f, 0);

		pivotBody.attachChild(pivotHead);

		lowerHeadLeft = new Cylinder("lowerHeadLeft", 5, 25, 0.05f, 0.25f, true);
		lowerHeadLeft.setLocalTranslation(0.125f, 0, 0);
		float[] angles = { FastMath.PI / 2, 0, -0.5f };
		lowerHeadLeft.getLocalRotation().fromAngles(angles);
		lowerHeadLeft.setDefaultColor(ColorRGBA.blue);
		
		upperHeadLeft = new Cylinder("upperHeadLeft", 5, 25, 0.05f, 0.25f, true);
		upperHeadLeft.setLocalTranslation(0.125f, 0.25f, 0);
		angles[0] = -FastMath.PI / 2;
		angles[1] = 0;
		angles[2] = 0.5f;
		upperHeadLeft.getLocalRotation().fromAngles(angles);
		upperHeadLeft.setDefaultColor(ColorRGBA.blue);

		pivotHeadLeft = new Node();
		pivotHeadLeft.attachChild(lowerHeadLeft);
		pivotHeadLeft.attachChild(upperHeadLeft);

		pivotHead.attachChild(pivotHeadLeft);

		lowerHeadRight = new Cylinder("lowerHeadRight", 5, 25, 0.05f, 0.25f,
				true);
		lowerHeadRight.setLocalTranslation(-0.125f, 0, 0);
		angles[0] = -FastMath.PI / 2;
		angles[1] = 0;
		angles[2] = 0.5f;
		lowerHeadRight.getLocalRotation().fromAngles(angles);
		lowerHeadRight.setDefaultColor(ColorRGBA.blue);
		
		upperHeadRight = new Cylinder("upperHeadRight", 5, 25, 0.05f, 0.25f, true);
		upperHeadRight.setLocalTranslation(-0.125f, 0.25f, 0);
		angles[0] = FastMath.PI / 2;
		angles[1] = 0;
		angles[2] = -0.5f;
		upperHeadRight.getLocalRotation().fromAngles(angles);
		upperHeadRight.setDefaultColor(ColorRGBA.blue);

		pivotHeadRight = new Node();
		pivotHeadRight.attachChild(lowerHeadRight);
		pivotHeadRight.attachChild(upperHeadRight);

		pivotHead.attachChild(pivotHeadRight);

		this.setLocalScale(10);
	}

//	public boolean isMoving() {
//		return _moving;
//	}
//
//	public boolean closeHand() {
//		if (!has_object) {
//
//			// Animation of taking an object (closing hand)
//
//			has_object = true;
//			_moving = true;
//		}
//		_moving = false;
//	}

	public boolean openHand(float angle, float time) {
		// Calculate direction of movement
		int direction = (int) Math.ceil(angleClaws * 180 / FastMath.PI)
				- (int) Math.ceil(angle * 180 / FastMath.PI);

		// System.out.println("Direction:"+direction);
		if (direction < 0) {
			if (time < 1) {
				angleClaws += time;
			}
			rotClawLeft.fromAngleAxis(angleClaws, new Vector3f(0, 0, 1));
			pivotHeadLeft.setLocalRotation(rotClawLeft);
			rotClawRight.fromAngleAxis(angleClaws*-1, new Vector3f(0, 0, 1));
			pivotHeadRight.setLocalRotation(rotClawRight);

			// System.out.println("Angulo: " + angleBody * 180 / FastMath.PI
			// + " radianes:" + angleBody);
			return false;
		} else {
			if (direction > 0) {
				if (time < 1) {
					angleClaws -= time;
				}
				rotClawLeft.fromAngleAxis(angleClaws, new Vector3f(0, 0, 1));
				pivotHeadLeft.setLocalRotation(rotClawLeft);
				rotClawRight.fromAngleAxis(angleClaws*-1, new Vector3f(0, 0, 1));
				pivotHeadRight.setLocalRotation(rotClawRight);

				// System.out.println("Disminuyendo Angulo: " + angleBody * 180
				// / FastMath.PI
				// + " radianes:" + angleBody);
				return false;
			} else {
				// System.out.println("Terminado Bend");
				angleClaws = angle;
				return true;
			}
		}
	}

	// Bend body having known angleBody
	public boolean bendBody(float angle, float time) {

		// Calculate direction of movement
		int direction = (int) Math.ceil(angleBody * 180 / FastMath.PI)
				- (int) Math.ceil(angle * 180 / FastMath.PI);

		// System.out.println("Direction:"+direction);
		if (direction < 0) {
			if (time < 1) {
				angleBody += time;
			}
			rotBody.fromAngleAxis(angleBody, new Vector3f(1, 0, 0));
			pivotBody.setLocalRotation(rotBody);

			// System.out.println("Angulo: " + angleBody * 180 / FastMath.PI
			// + " radianes:" + angleBody);
			return false;
		} else {
			if (direction > 0) {
				if (time < 1) {
					angleBody -= time;
				}
				rotBody.fromAngleAxis(angleBody, new Vector3f(1, 0, 0));
				pivotBody.setLocalRotation(rotBody);

				// System.out.println("Disminuyendo Angulo: " + angleBody * 180
				// / FastMath.PI
				// + " radianes:" + angleBody);
				return false;
			} else {
				// System.out.println("Terminado Bend");
				angleBody = angle;
				return true;
			}
		}
	}

	// public void moveTo(Vector3f position, float time) {
	// // Calcular el angulo de rotacion mas corto y girar en el sentido mas
	// // corto
	// if (this.getLocalRotation().y < this.localTranslation
	// .angleBetween(position)) {
	//
	// this.getLocalRotation().y += time * 1 / 6;
	//
	// System.out.println("Angulo: " + this.getLocalRotation().y * 180
	// / FastMath.PI + " radianes:" + this.getLocalRotation().y);
	// // rotFloor.fromAngleAxis(this.localTranslation.angleBetween(position),
	// // new Vector3f(0,1,0));
	// // this.setLocalRotation(rotFloor);
	// _moving = true;
	// }
	// _moving = false;
	// // this.setLocalRotation()(position, new Vector3f(0,1,0));
	// }

	public boolean moveTo(float angle, float time) {

		// Calculate direction of movement
		int direction = (int) Math.ceil(angleFloor * 180 / FastMath.PI)
				- (int) Math.ceil(angle * 180 / FastMath.PI);

		// System.out.println("Direction:"+direction);
		if (direction < 0) {
			if (time < 1) {
				angleFloor += time;
			}
			rotFloor.fromAngleAxis(angleFloor, new Vector3f(0, 1, 0));
			this.setLocalRotation(rotFloor);

			// System.out.println("Angulo: " + angleBody * 180 / FastMath.PI
			// + " radianes:" + angleBody);
			return false;
		} else {
			if (direction > 0) {
				if (time < 1) {
					angleFloor -= time;
				}
				rotFloor.fromAngleAxis(angleFloor, new Vector3f(0, 1, 0));
				this.setLocalRotation(rotFloor);

				// System.out.println("Disminuyendo Angulo: " + angleBody * 180
				// / FastMath.PI
				// + " radianes:" + angleBody);
				return false;
			} else {
				// System.out.println("Terminado Bend");
				angleFloor = angle;
				return true;
			}
		}
	}
}
