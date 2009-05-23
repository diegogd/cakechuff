package cc.simulation.elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import cc.simulation.utils.ModelLoader;

import com.jme.bounding.BoundingBox;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;
/**
 * Implementation and definition of the conveyor cake, one of
 * the simulation elements that compose CakeChuff system.
 * Extends its functionality from the "normal" conveyor.
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class ConveyorCake extends Conveyor {

	private static final long serialVersionUID = 1L;
	/**
	 * Constructor
	 * Loads the graphical model of the conveyor
	 */
	public ConveyorCake() {
		// super("CakeSystem");
		loadModel();
	}
	/**
	 * Loads the graphical model of the conveyor
	 */
	public void loadModel(){
		URL model = getClass().getClassLoader().getResource("model/cakeconveyor.obj");						
		
		this.attachChild(ModelLoader.loadOBJ(model, true));
		
//		model = getClass().getClassLoader().getResource("model/plc.obj");								
//		Spatial plc = ModelLoader.loadOBJ(model);
//		plc.setLocalTranslation(3, 4, 2);
//		this.attachChild(plc);
	}	
	
}
