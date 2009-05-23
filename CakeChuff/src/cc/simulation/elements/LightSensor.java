package cc.simulation.elements;

import com.jme.bounding.BoundingBox;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Cylinder;
/**
 * Implementation and definition of the light sensor, one of
 * the simulation elements that compose CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class LightSensor extends Sensor {
	
	Cylinder light;
	/**
	 * Constructor
	 * Initializes the light sensor (color of the light and graphical model)
	 * @param id Identification of the light sensor
	 */
	public LightSensor(String id) {
		setName(id);
		light = new Cylinder(id, 20, 20, 0.05f, 2, true);
		light.setModelBound(new BoundingBox());
		light.updateModelBound();
		light.setDefaultColor(ColorRGBA.red);
		light.updateRenderState();
		this.attachChild(light);
	}
	/**
	 * Sets the light sensor on (color: green)
	 */
	public void setOn()
	{
		light.setDefaultColor(ColorRGBA.green);
		this.setActived(true);
	}
	/**
	 * Sets the light sensor off (color: red)
	 */
	public void setOff()
	{
		light.setDefaultColor(ColorRGBA.red);
		this.setActived(false);
	}
}
