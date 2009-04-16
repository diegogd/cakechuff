package cc.simulation.elements;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class Robot1 extends Robot{

	//private Robot robot;
	
	private Vector3f positionSubsystem1 = new Vector3f(4, 9f, 0f);
	private Vector3f positionSubsystem2;
	private Vector3f positionSubsystem3;
	private Vector3f positionTable;
	
	boolean _moving;

	public Robot1(){
		super(new Vector3f(0,0,0));
		_moving = false;
		//robot = new Robot(new Vector3f(0,0,0)); //Modificar valores
	}
	
	public boolean isMoving (){
		return _moving;
	}
	
	public void moveToSub1(float time) {
		//System.out.println();
		super.moveTo(positionSubsystem1);
	}

	public void moveToSub2(float time) {
		super.moveTo(positionSubsystem2);
	}

	public void moveToSub3(float time) {
		super.moveTo(positionSubsystem3);
	}

	public void moveToTable(float time) {
		super.moveTo(positionTable);
	}
	
	public void pickUpCake(float time){
		
		//Hacer todas las llamadas dentro del while
		
		//moveToSub1();
//		while(super.bendBody(0.25f,time)){}
		super.bendBody(0.785f,time); //insertar angulo
		//super.closeHand();
		//super.bendBody(-15);
	}
	
	public void dropCake(float time){
		if(super.has_object){
			moveToTable(time);
			super.bendBody(0.785f,time);
			super.openHand();
			super.bendBody(0,time);
		}
	}
	
	public void pickUpBlister(float time){
		moveToSub2(time);
		super.bendBody(0.785f,time); //insertar angulo
		super.closeHand();
		super.bendBody(0,time);
	}
	
	public void dropBlister(float time){
		if(super.has_object){
			moveToTable(time);
			super.bendBody(0.785f,time);
			super.openHand();
			super.bendBody(0,time);
		}
	}
	
	public void pickUpPacket(float time){
		moveToTable(time);
		super.bendBody(0.785f,time); //insertar angulo
		super.closeHand();
		super.bendBody(0,time);
	}
	
	public void dropPacket(float time){
		if(super.has_object){
			moveToSub3(time);
			super.bendBody(0.785f,time);
			super.openHand();
			super.bendBody(0,time);
		}
	}
	
//	public void update(float time){
//		if (_moving){
//			
//		}
//	}
}
