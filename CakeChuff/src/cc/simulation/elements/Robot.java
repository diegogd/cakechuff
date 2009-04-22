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

	// Draw Robot
	private Cylinder lowerBody;
	private Cylinder upperBody;
	private Cylinder base;
	
	private float angleBody;
	private float angleFloor;
	private Quaternion rotFloor = new Quaternion();
	private Quaternion rotBody = new Quaternion();
	
	private Vector3f positionRobot;

	private Node pivot;

	public  Robot(Vector3f positionRobot) {
		has_object = false;
		_moving = false;
		this.positionRobot = new Vector3f(positionRobot);
		this.angleBody = 0;
		this.angleFloor = 0;
		
		loadTempModel();
		
		//Colocar Robot en la posicion
		this.setLocalTranslation(this.positionRobot);
	}
	
	private void loadTempModel(){
		base = new Cylinder("base",5,25,0.2f,0.1f,true);
		base.setLocalRotation(Rotations.rotateX(0.5f));
		base.setModelBound(new BoundingBox());
		base.updateModelBound();
		base.setDefaultColor(ColorRGBA.green);
		base.updateRenderState();
		
		this.attachChild(base);
		
		lowerBody = new Cylinder ("lowerBody",5,25,0.1f, 0.6f,true);
		lowerBody.setLocalRotation(Rotations.rotateX(0.5f));
		lowerBody.setLocalTranslation(0, 0.35f, 0);
		lowerBody.setDefaultColor(ColorRGBA.red);
		
		lowerBody.setModelBound(new BoundingBox());
		lowerBody.updateModelBound();
		lowerBody.updateRenderState();
		
		this.attachChild(lowerBody);
		
		upperBody = new Cylinder ("upperBody",5,25,0.1f, 0.6f,true);
		upperBody.setLocalRotation(Rotations.rotateX(0.5f));
		upperBody.setLocalTranslation(0, 0.3f, 0);
		upperBody.setModelBound(new BoundingBox());
		upperBody.updateModelBound();
		//upperBody.setDefaultColor(ColorRGBA.blue);
		upperBody.setRandomColors();
		upperBody.updateRenderState();
		
		pivot = new Node();
		//pivot.setLocalRotation(Rotations.rotateX(0.5f));
		pivot.setLocalTranslation(0, 0.65f, 0);
		pivot.attachChild(upperBody);
		
		Cylinder prueba = new Cylinder ("prueba",5,25,0.4f, 0.05f,true);
		prueba.setLocalRotation(Rotations.rotateX(0.5f));
		prueba.setDefaultColor(ColorRGBA.blue);
		pivot.attachChild(prueba);
		
		this.attachChild(pivot);
		
		this.setLocalScale(10);
	}

	public boolean isMoving (){
		return _moving;
	}
	
	
	public void closeHand() {
		if (!has_object) {
			
			// Animation of taking an object (closing hand)
			
			has_object = true;
			_moving = true;
		}
		_moving = false;
	}

	
	public void openHand() {
		if (has_object) {
			// Animation of leaving an object (opening hand)
			
			has_object = false;
			_moving = true;
		}
		_moving = false;
	}

	
	//Bend body having known angleBody
	public boolean bendBody(float angle, float time){
		
		if(pivot.getLocalRotation().x<angle){
			pivot.getLocalRotation().x += time*1/6;
			
//			rotBody.fromAngleAxis(pivot.getLocalRotation().x+time*1/8, new Vector3f(1,0,0));
//			this.setLocalRotation(rotBody);
			
			// pivot.setLocalRotation(Rotations.rotateX(pivot.getLocalRotation().x+time*1/8));
			// pivot.setLocalRotation(Rotations.rotateX(0.1f));
			System.out.println("Angulo: " + pivot.getLocalRotation().x*180/FastMath.PI + " radianes:" + pivot.getLocalRotation().x);
			_moving = true;
			return false;
		}
		 
		this.angleBody = pivot.getLocalRotation().x*180/FastMath.PI;
		_moving = false;
		return true;
		
	}
	
	public void moveTo (Vector3f position, float time){
		//Calcular el angulo de rotacion mas corto y girar en el sentido mas corto
		if(this.getLocalRotation().y<this.localTranslation.angleBetween(position)){
			
			this.getLocalRotation().y += time*1/6;
			
			System.out.println("Angulo: " + this.getLocalRotation().y*180/FastMath.PI + " radianes:" + this.getLocalRotation().y);
//			rotFloor.fromAngleAxis(this.localTranslation.angleBetween(position),
//					new Vector3f(0,1,0));
//			this.setLocalRotation(rotFloor);
			_moving = true;
		}
		_moving = false;
		//this.setLocalRotation()(position, new Vector3f(0,1,0));
	}
	
	public void moveTo (float angle, float time){
		//Dado el angulo
		if(this.getLocalRotation().y<angle){
			
//			this.getLocalRotation().y += time*1/6;
			
			System.out.println("Angulo: " + this.getLocalRotation().y*180/FastMath.PI + " radianes:" + this.getLocalRotation().y);
			rotFloor.fromAngleAxis(this.getLocalRotation().y+time*1/6,
					new Vector3f(0,1,0));
			this.setLocalRotation(rotFloor);
			_moving = true;
		}
		_moving = false;
		//this.setLocalRotation()(position, new Vector3f(0,1,0));
	}
} 
