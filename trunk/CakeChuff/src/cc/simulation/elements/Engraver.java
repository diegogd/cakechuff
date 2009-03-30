package cc.simulation.elements;

import java.io.File;

import cc.simulation.utils.ModelLoader;

import com.jme.scene.Node;

public class Engraver extends Node{
	
	public Engraver() {
		loadShape();
	}

	public void loadShape(){
		this.attachChild(
		ModelLoader.loadOBJ(getClass().getClassLoader().getResource("model/engraver.obj"))
		);
	}
}
