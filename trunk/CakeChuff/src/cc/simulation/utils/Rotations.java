package cc.simulation.utils;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
/**
 * Utility class used in the simulation. Helps in the rotation 
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Rotations {
	/**
	 * Makes a rotation in the x axis a specific number of radians
	 * @param radians Number of radians to be rotated
	 * @return Quaternion after the rotation has been done
	 */
	public static Quaternion rotateX(float radians){
		Quaternion q = new Quaternion();
		q.fromAngleAxis(FastMath.PI*radians, Vector3f.UNIT_X);
		return q;
	}
	/**
	 * Makes a rotation in the y axis a specific number of radians
	 * @param radians Number of radians to be rotated
	 * @return Quaternion after the rotation has been done
	 */
	public static Quaternion rotateY(float radians){
		Quaternion q = new Quaternion();
		q.fromAngleAxis(FastMath.PI*radians, Vector3f.UNIT_Y);
		return q;
	}
	/**
	 * Makes a rotation in the z axis a specific number of radians
	 * @param radians Number of radians to be rotated
	 * @return Quaternion after the rotation has been done
	 */
	public static Quaternion rotateZ(float radians){
		Quaternion q = new Quaternion();
		q.fromAngleAxis(FastMath.PI*radians, Vector3f.UNIT_Z);
		return q;
	}
}
