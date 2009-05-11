package cc.simulation.elements;

import cc.simulation.subsystems.QualitySubsystem;
import cc.simulation.utils.Rotations;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Cylinder;

public class Robot extends Node {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2973029142743347671L;
	// State to know if it has taken an object
	private boolean has_object;
	private Spatial object;

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
			pivotHeadRight, pivotElement, Father;

	public Robot(Vector3f positionRobot) {
		has_object = false;
		object = null;
		this.positionRobot = new Vector3f(positionRobot);
		this.angleBody = 0;
		this.angleFloor = 0;
		speed = 1;

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
		upperBody.setModelBound(new BoundingBox());
		upperBody.updateModelBound();
		upperBody.setLocalRotation(Rotations.rotateX(0.5f));
		upperBody.setLocalTranslation(0, 0.3f, 0);

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
		lowerHeadLeft.setModelBound(new BoundingBox());
		lowerHeadLeft.updateModelBound();
		lowerHeadLeft.setLocalTranslation(0.125f, 0, 0);
		float[] angles = { FastMath.PI / 2, 0, -0.5f };
		lowerHeadLeft.getLocalRotation().fromAngles(angles);
		lowerHeadLeft.setDefaultColor(ColorRGBA.blue);

		upperHeadLeft = new Cylinder("upperHeadLeft", 5, 25, 0.05f, 0.25f, true);
		upperHeadLeft.setModelBound(new BoundingBox());
		upperHeadLeft.updateModelBound();
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
		lowerHeadRight.setModelBound(new BoundingBox());
		lowerHeadRight.updateModelBound();
		lowerHeadRight.setLocalTranslation(-0.125f, 0, 0);
		angles[0] = -FastMath.PI / 2;
		angles[1] = 0;
		angles[2] = 0.5f;
		lowerHeadRight.getLocalRotation().fromAngles(angles);
		lowerHeadRight.setDefaultColor(ColorRGBA.blue);

		upperHeadRight = new Cylinder("upperHeadRight", 5, 25, 0.05f, 0.25f,
				true);
		upperHeadRight.setModelBound(new BoundingBox());
		upperHeadRight.updateModelBound();
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

		pivotElement = new Node();
		pivotHead.attachChild(pivotElement);

		this.setLocalScale(10);

	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return speed;
	}

	// public boolean isMoving() {
	// return _moving;
	// }
	//
	// public boolean closeHand() {
	// if (!has_object) {
	//
	// // Animation of taking an object (closing hand)
	//
	// has_object = true;
	// _moving = true;
	// }
	// _moving = false;
	// }

	public boolean openHand(float angle, float time) {
		// Calculate direction of movement
		int direction = (int) Math.ceil(angleClaws * 180 / FastMath.PI)
				- (int) Math.ceil(angle * 180 / FastMath.PI);

		// System.out.println("Direction:"+direction);
		if (direction < 0) {
			if (time < 1) {
				angleClaws += time * speed;
			}
			rotClawLeft.fromAngleAxis(angleClaws, new Vector3f(0, 0, 1));
			pivotHeadLeft.setLocalRotation(rotClawLeft);
			rotClawRight.fromAngleAxis(angleClaws * -1, new Vector3f(0, 0, 1));
			pivotHeadRight.setLocalRotation(rotClawRight);

			// System.out.println("Angulo: " + angleBody * 180 / FastMath.PI
			// + " radianes:" + angleBody);
			return false;
		} else {
			if (direction > 0) {
				if (time < 1) {
					angleClaws -= time * speed;
				}
				rotClawLeft.fromAngleAxis(angleClaws, new Vector3f(0, 0, 1));
				pivotHeadLeft.setLocalRotation(rotClawLeft);
				rotClawRight.fromAngleAxis(angleClaws * -1, new Vector3f(0, 0,
						1));
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

	public boolean openHandObject(float angle, float time, Spatial element) {
		// Calculate direction of movement
		int direction = (int) Math.ceil(angleClaws * 180 / FastMath.PI)
				- (int) Math.ceil(angle * 180 / FastMath.PI);

		// System.out.println("Direction:"+direction);
		if (direction < 0) {
			if (!lowerHeadLeft.hasCollision(element, false)
					&& !upperHeadLeft.hasCollision(element, false)
					&& !lowerHeadRight.hasCollision(element, false)
					&& !upperHeadRight.hasCollision(element, false)) {
				if (time < 1) {
					angleClaws += time * speed;
				}
				rotClawLeft.fromAngleAxis(angleClaws, new Vector3f(0, 0, 1));
				pivotHeadLeft.setLocalRotation(rotClawLeft);
				rotClawRight.fromAngleAxis(angleClaws * -1, new Vector3f(0, 0,
						1));
				pivotHeadRight.setLocalRotation(rotClawRight);

				// System.out.println("Angulo: " + angleBody * 180 / FastMath.PI
				// + " radianes:" + angleBody);
				return false;
			} else {
				System.out.println("Colisionado 1!!!");
				object = element;
				has_object = true;
				Vector3f in = element.getLocalTranslation();
				Father = element.getParent();
				Father.detachChild(element);
				pivotElement.attachChild(element);

				if (element instanceof Blister) {
					// element.setLocalRotation(Rotations.rotateX(0f));
					element.setLocalTranslation(0, -5f, 0);
					// element.setLocalTranslation(0, -1.5f, 0);
					// element.setLocalScale(0.25f);
					// Vector3f aux = new Vector3f();
					// pivotElement.worldToLocal(in, aux);
					// System.out.println(in.x + " " + in.y + " " + in.z);
					// System.out.println(aux.x + " " + aux.y + " " + aux.z);
					// System.out.println(pivotElement.getWorldTranslation().x
					// + " " + pivotElement.getWorldTranslation().y + " "
					// + pivotElement.getWorldTranslation().z);

					// pivotElement.setLocalTranslation(0f,0f,0f);

					element.updateRenderState();
				} else if (element instanceof Cake) {
					// element.setLocalRotation(Rotations.rotateX(0f));
					// element.setLocalTranslation(0, -0.65f, 0);
					// element.setLocalTranslation(0, -0.0f, 0);
					// element.setLocalScale(0.25f);
					// element.updateRenderState();
				} else {
					element.setLocalRotation(Rotations.rotateX(0f));
					element.setLocalTranslation(0, -0.65f, 0);
					element.setLocalTranslation(0, -0.0f, 0);
					element.setLocalScale(0.25f);
					element.updateRenderState();
				}

				return true;
			}
		} else {
			if (direction > 0) {
				if (!lowerHeadLeft.hasCollision(element, false)
						&& !upperHeadLeft.hasCollision(element, false)
						&& !lowerHeadRight.hasCollision(element, false)
						&& !upperHeadRight.hasCollision(element, false)) {
					if (time < 1) {
						angleClaws -= time * speed;
					}
					rotClawLeft
							.fromAngleAxis(angleClaws, new Vector3f(0, 0, 1));
					pivotHeadLeft.setLocalRotation(rotClawLeft);
					rotClawRight.fromAngleAxis(angleClaws * -1, new Vector3f(0,
							0, 1));
					pivotHeadRight.setLocalRotation(rotClawRight);

					// System.out.println("Colisionado!!!");
					return false;
				} else {
					System.out.println("Colisionado 2!!!");
					object = element;
					has_object = true;
					element.getParent();
					this.getParent().detachChild(element);
					pivotHead.attachChild(element);

					element.setLocalRotation(Rotations.rotateX(0f));
					element.setLocalTranslation(0, -0.65f, 0);
					element.setLocalTranslation(0, -0.0f, 0);

					element.setLocalScale(0.25f);
					element.updateRenderState();

					return true;
				}
			} else {
				// System.out.println("Terminado Bend");
				angleClaws = angle;
				return true;
			}
		}
	}

	public boolean leaveHandObject(float angle, float time, Spatial element) {
		// Calculate direction of movement
		int direction = (int) Math.ceil(angleClaws * 180 / FastMath.PI)
				- (int) Math.ceil(angle * 180 / FastMath.PI);

		// System.out.println("Direction:"+direction);
		if (this.has_object) {
			// if (!this.object.hasCollision(element, false)){
			//				
			// object.getLocalTranslation().y--;
			// return false;
			// }else{
			if (element instanceof Table) {
				if (this.object instanceof Blister) {
					this.has_object = false;
					// pivotElement.detachChild(object);
					object.removeFromParent();
					Father.attachChild(object);
					System.out.println(Father.toString());
					object.setLocalTranslation(9f, -1f, -2f);
					object.updateRenderState();
					Father.updateRenderState();

					return true;
				}
			}else if (element instanceof QualitySubsystem){
				
			}
			// }
		}
		return true;
	}

	// Bend body having known angleBody
	public boolean bendBody(float angle, float time) {

		// Calculate direction of movement
		int direction = (int) Math.ceil(angleBody * 180 / FastMath.PI)
				- (int) Math.ceil(angle * 180 / FastMath.PI);

		// System.out.println("Direction:"+direction);
		if (direction < 0) {
			if (time < 1) {
				angleBody += time * speed;
			}
			rotBody.fromAngleAxis(angleBody, new Vector3f(1, 0, 0));
			pivotBody.setLocalRotation(rotBody);

			// if (this.has_object) {
			// this.object.getLocalTranslation().y -= 0.01;
			// }

			// System.out.println("Angulo: " + angleBody * 180 / FastMath.PI
			// + " radianes:" + angleBody);
			return false;
		} else {
			if (direction > 0) {
				if (time < 1) {
					angleBody -= time * speed;
				}
				rotBody.fromAngleAxis(angleBody, new Vector3f(1, 0, 0));
				pivotBody.setLocalRotation(rotBody);

				// if (this.has_object) {
				// this.object.getLocalTranslation().y += 0.01;
				// }

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

	public boolean moveTo(float angle, float time) {

		// Calculate direction of movement
		int direction = (int) Math.ceil(angleFloor * 180 / FastMath.PI)
				- (int) Math.ceil(angle * 180 / FastMath.PI);

		// System.out.println("Direction:"+direction);
		if (direction < 0) {
			if (time < 1) {
				angleFloor += time * speed;
			}
			rotFloor.fromAngleAxis(angleFloor, new Vector3f(0, 1, 0));
			this.setLocalRotation(rotFloor);

			// if (this.has_object) {
			// this.object.setLocalRotation(rotFloor);
			// this.object.setLocalTranslation((float)this.object.getLocalTranslation().x+0.01f,
			// 0f, (float)this.object.getLocalTranslation().z+0.01f);
			// }

			// System.out.println("Angulo: " + angleBody * 180 / FastMath.PI
			// + " radianes:" + angleBody);
			return false;
		} else {
			if (direction > 0) {
				if (time < 1) {
					angleFloor -= time * speed;
				}
				rotFloor.fromAngleAxis(angleFloor, new Vector3f(0, 1, 0));
				this.setLocalRotation(rotFloor);

				// if (this.has_object) {
				// this.object.setLocalRotation(rotFloor);
				// this.object.setLocalTranslation((float)this.object.getLocalTranslation().x+0.1f,
				// 0f, (float)this.object.getLocalTranslation().z+0.1f);
				// }

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
