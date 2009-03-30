package cc.simulation.elements;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Cylinder;

public class TouchSensor extends Sensor {
	
	Box sensor;

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
	
	public void setOn()
	{
		sensor.setDefaultColor(ColorRGBA.green);
	}
	
	public void setOff()
	{
		sensor.setDefaultColor(ColorRGBA.orange);
	}
}
