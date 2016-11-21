package main.parts.sensors;

/**
 * Interface with basic functionality for simple implementation of different sensors in code
 * 
 * @author andreashammer
 */
public interface Sensor {
	
	/**
	 * @return The current value of the sensor
	 */
	public float getValue();
	
	/**
	 * Update the sensor values
	 */
	public void update();
}
