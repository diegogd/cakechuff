package cc.simulation.elements;

import com.jme.bounding.BoundingBox;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Cylinder;

public class LightSensor extends Sensor {
	
	Cylinder light;

	public LightSensor(String id) {
		setName(id);
		light = new Cylinder(id, 20, 20, 0.05f, 2, true);
		light.setModelBound(new BoundingBox());
		light.updateModelBound();
		light.setDefaultColor(ColorRGBA.red);
		light.updateRenderState();
		this.attachChild(light);
	}
	
	public void setOn()
	{
		light.setDefaultColor(ColorRGBA.green);
		this.setActived(true);
	}
	
	public void setOff()
	{
		light.setDefaultColor(ColorRGBA.red);
		this.setActived(false);
	}
}
