package cc.sensors;

import java.io.IOException;

/**
 * Definition of the implementation of the sensors of the CakeChuff system
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public abstract class Sensor {

	private String idSensor;
	/**
	 * Constructor
	 * Initializes the identification of the sensor
	 * @param id Sensor identification
	 */
	public Sensor(String id) {
		this.idSensor = id;
	}
}
