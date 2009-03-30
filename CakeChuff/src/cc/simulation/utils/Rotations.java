package cc.simulation.utils;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public class Rotations {

	public static Quaternion rotateX(float radians){
		Quaternion q = new Quaternion();
		q.fromAngleAxis(FastMath.PI*radians, Vector3f.UNIT_X);
		return q;
	}
	
	public static Quaternion rotateY(float radians){
		Quaternion q = new Quaternion();
		q.fromAngleAxis(FastMath.PI*radians, Vector3f.UNIT_Y);
		return q;
	}
	
	public static Quaternion rotateZ(float radians){
		Quaternion q = new Quaternion();
		q.fromAngleAxis(FastMath.PI*radians, Vector3f.UNIT_Z);
		return q;
	}
}
