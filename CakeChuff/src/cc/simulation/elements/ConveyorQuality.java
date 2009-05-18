package cc.simulation.elements;

import java.net.URL;

import cc.simulation.utils.ModelLoader;

import com.jme.math.Vector3f;

public class ConveyorQuality extends Conveyor {

	private static final long serialVersionUID = 1L;

	public ConveyorQuality() {
//		super("ConveyorCake");
		loadModel();
	}
	public void loadModel(){
		URL model = getClass().getClassLoader().getResource("model/blisterconveyor.obj");						
		
		this.attachChild(ModelLoader.loadOBJ(model));
		
//		model = getClass().getClassLoader().getResource("model/plc.obj");								
//		Spatial plc = ModelLoader.loadOBJ(model);
//		plc.setLocalTranslation(3, 4, 2);
//		this.attachChild(plc);
	}	
	
}
