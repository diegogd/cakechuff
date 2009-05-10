package cc.simulation.elements;

import cc.simulation.utils.ModelLoader;

import com.jme.scene.Node;

public class Wrapper extends Node {
	
	private float speed;
	private boolean direction;
	Node pivot;
	
	public Wrapper() {
		
		speed = 0;
		direction = false;
		pivot = new Node();
		loadShape();
		this.attachChild(pivot);
		this.setName("Wrapper");
	}

	public float getSpeed(){
		return this.speed;
	}
	public void setSpeed(float speed){
		this.speed = speed;
	}
	
	public void loadShape(){
		pivot.attachChild(
		ModelLoader.loadOBJ(getClass().getClassLoader().getResource("model/engraver.obj"))
		);
	}
	
	
	public void update(float timeperframes)
	{
//		System.out.println(pivot.getLocalTranslation().y);
		if(speed>0){
			if (direction){
				if(pivot.getLocalTranslation().y >= 0.0f){
					pivot.getLocalTranslation().y  = 0.0f;
					direction = false;
					
					//Approach 2
					speed = 0;
				}
				else {
					pivot.getLocalTranslation().y += speed*timeperframes;
				}
			}else{
				if(pivot.getLocalTranslation().y <= -1.6f){
					pivot.getLocalTranslation().y  = -1.6f;
					direction = true;
					
					//Cargar Blister?
					
					
				}
				else {
					pivot.getLocalTranslation().y -= speed*timeperframes;
				}				
			}	
		}
	}
}
