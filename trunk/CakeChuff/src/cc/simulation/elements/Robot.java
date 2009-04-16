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

	// State to know if it has taken an object
	boolean has_object;

	// Draw Robot
	private Cylinder lowerBody;
	private Cylinder upperBody;
	private Cylinder base;
	
	private float angleBody;
	private float angleFloor;
	private Quaternion rotFloor = new Quaternion();
	
	private Vector3f positionRobot;

	private Node pivot;
	private Cylinder liquid;

	public  Robot(Vector3f positionRobot) {
		has_object = false;
		this.positionRobot = new Vector3f(positionRobot);
		this.angleBody = 0;
		this.angleFloor = 0;
		
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
		
		/*
		Cylinder cake = new Cylinder("cake",4, 20, 0.7f, 0.5f, true);
		cake.setLocalRotation(Rotations.rotateX(0.5f));
		cake.setModelBound(new BoundingBox());
		cake.updateModelBound();
		cake.setDefaultColor(ColorRGBA.brown);
		cake.updateRenderState();
		this.attachChild(cake);
		*/
		
		//Colocar Robot en la posicion
		this.setLocalTranslation(this.positionRobot);
	}

	
	public void closeHand() {
		if (!has_object) {
			
			// Animation of taking an object (closing hand)
			
			has_object = true;
		}
	}

	
	public void openHand() {
		if (has_object) {
			// Animation of leaving an object (opening hand)
			
			has_object = false;
		}
	}

	//Calculate angle of the given vector with the actual position of the 
	// robot and rotate it
	private void rotateLeft(Vector3f position) {

	}

	private void rotateRight(Vector3f position) {

	}
	
	//Bend body having known angleBody
	public void bendBody(float angle, float time){
		
		if(pivot.getLocalRotation().x<angle){
			pivot.getLocalRotation().x += time*1/6;
			System.out.println(pivot.getLocalRotation().x*180/FastMath.PI + " radianes:" + pivot.getLocalRotation().x);
		}
		this.angleBody = angle*180/FastMath.PI;
		
	}
	
	public void moveTo (Vector3f position){
		//Calcular el angulo de rotacion mas corto y girar en el sentido mas corto
		rotFloor.fromAngleAxis(this.localTranslation.angleBetween(position)*180/FastMath.PI,
				new Vector3f(0,1,0));
		this.setLocalRotation(rotFloor);
		//this.setLocalRotation()(position, new Vector3f(0,1,0));
	}
} 
