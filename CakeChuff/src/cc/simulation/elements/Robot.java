package cc.simulation.elements;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cc.simulation.subsystems.QualitySubsystem;
import cc.simulation.utils.ModelLoader;
import cc.simulation.utils.Rotations;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.AxisRods;
import com.jme.scene.shape.Cylinder;
/**
 * Implementation and definition of the robots, one of
 * the simulation elements that compose CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Robot extends Node {

	private static Logger logger = Logger.getLogger(Robot.class.getName());
		
	/**
	 * Distance used to check if an object is close enough.
	 */
	private final float OBJECTS_MINDISTANCE = 6f;
	/**
	 * 
	 */
	private static final long serialVersionUID = 2973029142743347671L;
	// State to know if it has taken an object
	public boolean has_object;
	private Spatial takenObject, lastBlister;

	private float speed; // modificar la velocidad

	// Draw Robot
	private Cylinder lowerBody;
	private Cylinder upperBody;
	private Cylinder base;

	private Cylinder lowerHeadLeft, lowerHeadRight;
	private Cylinder upperHeadLeft, upperHeadRight;
	
	private int precision = 2;

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
	/**
	 * Constructor
	 * Initializes the robot and its position
	 * @param positionRobot Initial position of the robot
	 */
	public Robot(Vector3f positionRobot) {
		has_object = false;
		takenObject = null;
		this.positionRobot = new Vector3f(positionRobot);
		this.angleBody = 0;
		this.angleFloor = 0;
		speed = 1;

		// loadTempModel();
		loadModel();

		// Colocar Robot en la posicion
		this.setLocalTranslation(this.positionRobot);
	}
	/**
	 * Auxiliary method. Loads a temporary graphic model of the robot
	 */
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

		// pivotElement.setLocalRotation(Rotations.rotateX(0.1f));
		// pivotElement.setLocalTranslation(0, -0.65f, 0);
		// pivotElement.setLocalTranslation(0, -0.675f, 0);
		//		
		// pivotElement.setLocalRotation(Rotations.rotateX(0.1f));
		// Cylinder temp = new Cylinder("temp55", 5, 25, 0.05f, 0.25f,
		// true);
		// pivotElement.attachChild(temp);

		pivotHead.attachChild(pivotElement);

		// AxisRods axis = new AxisRods("rods", true, 0.1f);
		//		
		// pivotElement.attachChild(axis);

		this.setLocalScale(10);

	}
	/**
	 * Loads the graphic model of the robot
	 */
	public void loadModel() {

		loadObject("robotBase.obj", this, false);

		pivotBase = new Node();
		pivotBase.setLocalRotation(Rotations.rotateX(-0.1f));
		pivotBase.setLocalTranslation(0f, 0.13f, 0f);

		loadObject("robotLower.obj", pivotBase, false);
		this.attachChild(pivotBase);

		pivotBody = new Node();

		pivotBody.setLocalTranslation(0, 0.52f, 0);

		loadObject("robotUpper.obj", pivotBody, false);

		pivotBase.attachChild(pivotBody);

		pivotHead = new Node();
		pivotHead.setLocalTranslation(0, 0.53f, 0);

		pivotBody.attachChild(pivotHead);

		pivotHeadLeft = new Node();

		loadObject("robotRightClaw.obj", pivotHeadLeft, true);

		pivotHeadLeft.setLocalTranslation(0.22f, 0f, 0f);

		pivotHead.attachChild(pivotHeadLeft);

		pivotHeadRight = new Node();
		loadObject("robotLeftClaw.obj", pivotHeadRight, true);

		pivotHeadRight.setLocalTranslation(-0.22f, 0f, 0f);

		pivotHead.attachChild(pivotHeadRight);

		pivotElement = new Node();

		pivotHead.attachChild(pivotElement);

		this.setLocalScale(10);
	}
	/**
	 * Loads a graphic model of an object
	 * @param objectName Name of the object to be loaded
	 * @param parent Father node of the object to be loaded
	 * @param bounding True if the object has boundings and false if it does not
	 */
	private void loadObject(String objectName, Node parent, boolean bounding) {
		URL model = getClass().getClassLoader().getResource(
				"model/" + objectName);
		Spatial object = ModelLoader.loadOBJ(model, bounding);
		object.setLocalScale(0.1f);
		parent.attachChild(object);
	}
	/**
	 * Modifies the speed of the robot
	 * @param speed The new speed of the robot
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	/**
	 * Returns the speed of the robot
	 * @return The speed of the robot
	 */
	public float getSpeed() {
		return speed;
	}
	/**
	 * Returns the robot has an object
	 * @return True if the robot has an object, false if it does not
	 */
	public boolean getHasObject() {
		return has_object;
	}
	/**
	 * Opens the hand of the robot
	 * @param angle Angle that the hand opens
	 * @param time Time taken for the robot to open its hand
	 * @return True if the robot opens its hands with no problem, false if it does not
	 */
	public boolean openHand(float angle, float time) {
		// Calculate direction of movement
		int direction = (int) Math.ceil(angleClaws * 180 / FastMath.PI)
				- (int) Math.ceil(angle * 180 / FastMath.PI);

//		 System.out.println("OPENHAND: Direction:"+direction);
		if (direction < -precision) {
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
			if (direction > precision) {
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
				logger.finest("Finished OPENHAND");
				angleClaws = angle;
				rotClawLeft.fromAngleAxis(angleClaws, new Vector3f(0, 0, 1));
				pivotHeadLeft.setLocalRotation(rotClawLeft);
				rotClawRight.fromAngleAxis(angleClaws * -1, new Vector3f(0, 0, 1));
				pivotHeadRight.setLocalRotation(rotClawRight);
				return true;
			}
		}
	}
	/**
	 * Makes the robot take a list of elements
	 * @param elements List of spatial elements to be taken
	 * @return True if the robot takes the elements with no problem, false if it does not
	 */
	public boolean takeObject(List<Spatial> elements) {
		boolean taken = false;

		if (!has_object) {
			Spatial element = getNearestObject(elements);
			if( element != null ){			
				takenObject = element;
				has_object = true;
				Father = element.getParent();
				Father.detachChild(element);
				pivotElement.attachChild(element);
				elements.remove(element);
				taken = true;
	
				if (element instanceof Blister) {
					if (element.getLocalRotation().y < 0) {
						// Blister de la mesa
						element.setLocalRotation(Rotations.rotateY(0f));
						// element.setLocalTranslation(-6f, 1f, -3f);
						element.setLocalTranslation(0, -0.0f, -0.0f);
						pivotElement.setLocalScale(0.1f);
						element.setLocalTranslation(6f, 0.0f, 8.5f);
	
						element.updateRenderState();
					} else {
						// Blister del sub2
						element.setLocalTranslation(0, -0.0f, -0.0f);
						pivotElement.setLocalScale(0.1f);
						element.setLocalTranslation(6f, 0.0f, 8.5f);
	
						element.updateRenderState();
					}
	
				} else if (element instanceof Cake) {				
					element.setLocalTranslation(0, -0.0f, -0.0f);
					// element.setLocalRotation(Rotations.rotateX(-0.5f));
					pivotElement.setLocalScale(0.1f);
					element.setLocalTranslation(0f, 5.5f, 0f);
				}
			}
		}

		return taken;

	}

//	public boolean openHandObject(float angle, float time, Spatial element) {
//		// Calculate direction of movement
//		int direction = (int) Math.ceil(angleClaws * 180 / FastMath.PI)
//				- (int) Math.ceil(angle * 180 / FastMath.PI);
//
//		logger.log(Level.FINEST, "OPENHANDOBJECT Direction: {0}", new String[]{""+direction});
//		if (!has_object) {
//			if (direction < -precision) {
//				if (
//				// !lowerHeadLeft.hasCollision(element, false)
//				// && !upperHeadLeft.hasCollision(element, false)
//				!pivotHeadLeft.hasCollision(element, false)
//						&& !pivotHeadRight.hasCollision(element, false)
//				// Check Distance
//				&& pivotHeadLeft.getWorldTranslation()
//				   .distanceSquared(element.getWorldTranslation()) > OBJECTS_MINDISTANCE
//				&& pivotHeadRight.getWorldTranslation()
//				   .distanceSquared(element.getWorldTranslation()) > OBJECTS_MINDISTANCE
//				// && !lowerHeadRight.hasCollision(element, false)
//				// && !upperHeadRight.hasCollision(element, false)
//				) {
//					if (time < 1) {
//						angleClaws += time * speed;
//					}
//					rotClawLeft
//							.fromAngleAxis(angleClaws, new Vector3f(0, 0, 1));
//					pivotHeadLeft.setLocalRotation(rotClawLeft);
//					rotClawRight.fromAngleAxis(angleClaws * -1, new Vector3f(0,
//							0, 1));
//					pivotHeadRight.setLocalRotation(rotClawRight);
//
//					// System.out.println("Angulo: " + angleBody * 180 /
//					// FastMath.PI
//					// + " radianes:" + angleBody);
//					return false;
//				} else {
//					logger.finer("Colisionado Robot1!!!");
//					logger.log(Level.WARNING, "Pinza1: {0} Pinza2 {1}",
//					new String[]{
//						""+	pivotHeadLeft.getWorldTranslation()
//							   .distance(element.getWorldTranslation()),
//						""+	pivotHeadRight.getWorldTranslation()
//							   .distance(element.getWorldTranslation())
//					}
//					);
//					takenObject = element;
//					has_object = true;
//					Father = element.getParent();
//					Father.detachChild(element);
//					pivotElement.attachChild(element);
//
//					if (element instanceof Blister) {
//						if (element.getLocalRotation().y < 0) {
//							// Blister de la mesa
//							element.setLocalRotation(Rotations.rotateY(0f));
//							// element.setLocalTranslation(-6f, 1f, -3f);
//							element.setLocalTranslation(0, -0.0f, -0.0f);
//							pivotElement.setLocalScale(0.1f);
//							element.setLocalTranslation(6f, 0.0f, 8.5f);
//
//							element.updateRenderState();
//						} else {
//							// Blister del sub2
//							element.setLocalTranslation(0, -0.0f, -0.0f);
//							pivotElement.setLocalScale(0.1f);
//							element.setLocalTranslation(6f, 0.0f, 8.5f);
//
//							element.updateRenderState();
//						}
//
//					} else if (element instanceof Cake) {
//						((Cake) element).inSub = false;
//						element.setLocalTranslation(0, -0.0f, -0.0f);
//						// element.setLocalRotation(Rotations.rotateX(-0.5f));
//						pivotElement.setLocalScale(0.1f);
//						element.setLocalTranslation(0f, 5.5f, 0f);
//
//					}
//
//					return true;
//				} 
//			} else {
//				if (direction > precision) {
//					if (!pivotHeadLeft.hasCollision(element, false)
//							&& !pivotHeadRight.hasCollision(element, false)
//						&& pivotHeadLeft.getWorldTranslation()
//						   .distanceSquared(element.getWorldTranslation()) > OBJECTS_MINDISTANCE
//						&& pivotHeadRight.getWorldTranslation()
//						   .distanceSquared(element.getWorldTranslation()) > OBJECTS_MINDISTANCE
//						) {
//						if (time < 1) {
//							angleClaws -= time * speed;
//						}
//						rotClawLeft.fromAngleAxis(angleClaws, new Vector3f(0,
//								0, 1));
//						pivotHeadLeft.setLocalRotation(rotClawLeft);
//						rotClawRight.fromAngleAxis(angleClaws * -1,
//								new Vector3f(0, 0, 1));
//						pivotHeadRight.setLocalRotation(rotClawRight);
//
//						// System.out.println("Colisionado!!!");
//						return false;
//					} else {
//						logger.finer("Colisionado 2 Robot1!!!");
//						takenObject = element;
//						has_object = true;
//						Father = element.getParent();
//						Father.detachChild(element);
//						pivotElement.attachChild(element);
//
//						if (element instanceof Blister) {
//							if (element.getLocalRotation().y < 0) {
//								// Blister de la mesa
//								element.setLocalRotation(Rotations.rotateY(0f));
//								// element.setLocalTranslation(-6f, 1f, -3f);
//								element.setLocalTranslation(0, -0.0f, -0.0f);
//								pivotElement.setLocalScale(0.1f);
//								element.setLocalTranslation(6f, 0.0f, 8.5f);
//
//								element.updateRenderState();
//							} else {
//								// Blister del sub2
//								element.setLocalTranslation(0, -0.0f, -0.0f);
//								pivotElement.setLocalScale(0.1f);
//								element.setLocalTranslation(6f, 0.0f, 8.5f);
//
//								element.updateRenderState();
//							}
//
//						} else if (element instanceof Cake) {
//							((Cake) element).inSub = false;
//							element.setLocalTranslation(0, -0.0f, -0.0f);
//							// element.setLocalRotation(Rotations.rotateX(-0.5f));
//							pivotElement.setLocalScale(0.1f);
//							element.setLocalTranslation(0f, 5.5f, 0f);
//
//						} 
//
//						return true;
//					}
//				} else {
//					logger.finer("Sin Colision Robot1");
//					angleClaws = angle;
//					rotClawLeft
//					.fromAngleAxis(angleClaws, new Vector3f(0, 0, 1));
//			pivotHeadLeft.setLocalRotation(rotClawLeft);
//			rotClawRight.fromAngleAxis(angleClaws * -1, new Vector3f(0,
//					0, 1));
//			pivotHeadRight.setLocalRotation(rotClawRight);
//					return true;
//				}
//			}
//		}else return true;
//	}	
	/**
	 * Makes the robot take a list of elements
	 * @param angle Angle of the robot arm
	 * @param time Time for leaving the object
	 * @param element Place where leave the object
	 * @return True if the robot takes the elements with no problem, false if it does not
	 */
	public boolean leaveHandObject(float angle, float time, Spatial element) {
		// Calculate direction of movement
		int direction = (int) Math.ceil(angleClaws * 180 / FastMath.PI)
				- (int) Math.ceil(angle * 180 / FastMath.PI);

		if (this.has_object) {			
			
			if (element instanceof Table) {
				if (this.takenObject instanceof Blister) {
					this.has_object = false;
					// pivotElement.detachChild(object);
					takenObject.removeFromParent();
					Father.attachChild(takenObject);
					System.out.println(Father.toString());

					takenObject.setLocalRotation(Rotations.rotateY(-0.15f));
					takenObject.setLocalTranslation(6f, -1f, 3f);
					takenObject.updateRenderState();
					Father.updateRenderState();
					lastBlister = takenObject;					
					((Table)element).addBlister(this.takenObject);
					takenObject = null;

					return true;
				} else if (this.takenObject instanceof Cake) {
					if (lastBlister != null) {
						this.has_object = false;
						takenObject.setLocalTranslation(0, 0, 0);
						System.out.println(Father.toString());
						// element.setLocalRotation(Rotations.rotateX(0.5f));
						((Blister) lastBlister).placeCake(takenObject);

						takenObject = null;

					}
					// } else {
					// this.has_object = false;
					// // pivotElement.detachChild(object);
					// object.removeFromParent();
					// Father.attachChild(object);
					// System.out.println(Father.toString());
					// object.setLocalTranslation(9f, 0, -2f);
					// object.updateRenderState();
					// Father.updateRenderState();
					// }
					return true;
				}
			} else if (element instanceof QualitySubsystem) {
				if (this.takenObject instanceof Blister) {
					this.has_object = false;
					// pivotElement.detachChild(object);

					((QualitySubsystem)element).takenBlisters.add(takenObject);
					takenObject.removeFromParent();
					Father.attachChild(takenObject);
					System.out.println(Father.toString());
					takenObject.setLocalTranslation(16f, -1f, 8.5f);
					takenObject.updateRenderState();
					Father.updateRenderState();
					lastBlister = null;
					takenObject = null;

					return true;
				}
			} else if (element instanceof PacketBox) {
				if (this.takenObject instanceof Blister) {
					this.has_object = false;
					// pivotElement.detachChild(object);
					((PacketBox) element).addInBox(this.takenObject);
					takenObject = null;

					return true;
				}
			}
			// }
		}
		return true;
	}
	/**
	 * Makes the robot get the nearest object to its position
	 * @param elements List of spatial elements to be taken
	 * @return True if the robot takes the elements with no problem, false if it does not
	 */
	private Spatial getNearestObject(List<Spatial> elements){
		float distance=0, tempDistance;
		Spatial nearest = null, current;
		
		if( elements.size() > 0 ){
			Iterator<Spatial> elem = elements.iterator();
			current = elem.next();
			nearest = current;
			distance = distance(current);						
			if(elem.hasNext()){
				current = elem.next();
				tempDistance = distance(current);
				if(tempDistance < distance){
					nearest = current;
					distance = distance(current);
				}
			}
		}
		
		return nearest;
	}
	/**
	 * Calculate the distance from the robot to another object
	 * @param otherObject Object whose distance to wants to be calculated
	 * @return The distance from the robot to the other object
	 */
	private float distance(Spatial otherObject){
		float distance=0;		
		distance = Math.min(
				pivotHeadLeft.getWorldScale().distance(otherObject.getWorldScale()),
				pivotHeadRight.getWorldScale().distance(otherObject.getWorldScale()));		
		return distance;
	}

	// Bend body having known angleBody
	/**
	 * Bends the body of the robot knowing the angle
	 * @param angle Angle that the body has to be bent
	 * @param time Time taken for the body to bend
	 * @return True if the body of the robot bends with no problem, false if it does not
	 */
	public boolean bendBody(float angle, float time) {

		// Calculate direction of movement
		int direction = (int) Math.ceil(angleBody * 180 / FastMath.PI)
				- (int) Math.ceil(angle * 180 / FastMath.PI);

//		 System.out.println("BENDBODY Direction:"+direction);
		if (direction < -precision) {
			if (time < 1) {
				angleBody += time * speed;
			}
			rotBody.fromAngleAxis(angleBody, new Vector3f(1, 0, 0));
			pivotBody.setLocalRotation(rotBody);

			return false;
		} else {
			if (direction > precision) {
				if (time < 1) {
					angleBody -= time * speed;
				}
				rotBody.fromAngleAxis(angleBody, new Vector3f(1, 0, 0));
				pivotBody.setLocalRotation(rotBody);

				return false;
			} else {
				logger.finest("Finished BENDBODY");
				angleBody = angle;
				rotBody.fromAngleAxis(angleBody, new Vector3f(1, 0, 0));
				pivotBody.setLocalRotation(rotBody);
				return true;
			}
		}
	}
	/**
	 * Modification of the position of the robot
	 * @param angle Angle of the movement of the robot
	 * @param time Time taken 
	 * @return True if the no problems occured, false if it does not
	 */
	public boolean moveTo(float angle, float time) {

		// Calculate direction of movement
		int direction = (int) Math.ceil(angleFloor * 180 / FastMath.PI)
				- (int) Math.ceil(angle * 180 / FastMath.PI);

//		 System.out.println("MOVETO Direction:"+direction);
		if (direction < -precision) {
			if (time < 1) {
				angleFloor += time * speed;
			}
			rotFloor.fromAngleAxis(angleFloor, new Vector3f(0, 1, 0));
			this.setLocalRotation(rotFloor);

			return false;
		} else {
			if (direction > precision) {
				if (time < 1) {
					angleFloor -= time * speed;
				}
				rotFloor.fromAngleAxis(angleFloor, new Vector3f(0, 1, 0));
				this.setLocalRotation(rotFloor);

				return false;
			} else {
				logger.finest("Finished MOVETO");
				angleFloor = angle;
				rotFloor.fromAngleAxis(angleFloor, new Vector3f(0, 1, 0));
				this.setLocalRotation(rotFloor);
				return true;
			}
		}
	}
}
