package cc.simulation.elements;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Cylinder;
/**
 * Implementation and definition of the touch sensor, one of
 * the simulation elements that compose CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class TouchSensor extends Sensor {
	
	Box sensor;
	/**
	 * Constructor
	 * Initializes the touch sensor and assigns an id to it
	 * @param id Identification of the touch sensor
	 */
	public TouchSensor(String id) {
		setName(id);
		sensor = new Box(id,new Vector3f(-0.2f, -0.1f, -0.1f), 
				new Vector3f(0.2f, 0.4f, 0.1f));
		sensor.setModelBound(new BoundingBox());
		sensor.updateModelBound();
		sensor.setDefaultColor(ColorRGBA.orange);
		sensor.updateRenderState();
		this.attachChild(sensor);
	}
	/**
	 * Sets the sensor on
	 */
	public void setOn()
	{
		sensor.setDefaultColor(ColorRGBA.green);
	}
	/**
	 * Sets the sensor off
	 */
	public void setOff()
	{
		sensor.setDefaultColor(ColorRGBA.orange);
	}
}
