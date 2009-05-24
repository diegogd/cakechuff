package cc.simulation.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import com.jme.bounding.BoundingBox;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;
/**
 * Utility class used in the simulation. Loads graphical objects (models)
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class ModelLoader {
	/**
	 * Loads a specific graphical object or model
	 * @param pathmodel Path where the model is
	 * @param boundingBox True if a bounding box is needed, false if it is not.
	 * @return The model that wants to be loaded
	 */
	public static Spatial loadOBJ(URL pathmodel, boolean boundingBox)
	{
		FormatConverter converter = new ObjToJme();
		converter.setProperty("mtllib", pathmodel);
		converter.setProperty("texdir", ModelLoader.class.getResource("/model/texture"));
		
		ByteArrayOutputStream BO = new ByteArrayOutputStream();
		try{
			converter.convert(pathmodel.openStream(), BO);
			Spatial element= (Spatial) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
	        
			if( boundingBox ) {
		        element.setModelBound(new BoundingBox());
		        element.updateModelBound();
			}
	        return element;
		} catch (Exception e) {
			// TODO: handle exception
			// System.out.println("");
			e.printStackTrace();
			return null;
		}
	}
	
}
