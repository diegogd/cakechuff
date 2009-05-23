package cc.simulation.elements;

import java.net.URL;

import cc.simulation.utils.ModelLoader;

import com.jme.math.Vector3f;
/**
 * Implementation and definition of the conveyor blister, one of
 * the simulation elements that compose CakeChuff system.
 * Extends its functionality from the "normal" conveyor.
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class ConveyorBlister extends Conveyor {

	private static final long serialVersionUID = 1L;
	/**
	 * Constructor
	 * Loads the graphical model of the conveyor
	 */
	public ConveyorBlister() {
//		 super("CakeSystem");
		loadModel();
	}
	
	/**
	 * Loads the graphical model of the conveyor
	 */
	public void loadModel(){
		URL model = getClass().getClassLoader().getResource("model/blisterconveyor.obj");						
		
		this.attachChild(ModelLoader.loadOBJ(model, true));
		
//		model = getClass().getClassLoader().getResource("model/plc.obj");								
//		Spatial plc = ModelLoader.loadOBJ(model);
//		plc.setLocalTranslation(3, 4, 2);
//		this.attachChild(plc);
	}	
	/**
	 * Modifies the current velocity of the conveyor
	 * @param velocity The new velocity of the conveyor
	 */
	@Override
	public void setVelocity(float velocity) {
		super.current_velocity = velocity;
	}
}
