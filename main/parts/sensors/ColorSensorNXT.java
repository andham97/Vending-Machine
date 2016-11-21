package main.parts.sensors;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.robotics.SampleProvider;

/**
 * An extension of the NXTLightSensor class with extra functionality for simpler code
 * 
 * @author andreashammer
 *
 */
public class ColorSensorNXT implements Sensor {

	/**
	 * Reference to color sensor
	 */
    private NXTLightSensor colorSensor;
    
    /**
     * Reference to sample provider object of the sensor
     */
    private SampleProvider colorProvider;
    
    /**
     * Array containing the values of the sensor
     */
    private float[] colorSample;

    /**
     * Initialize the ColorSensorNXT and activate the port on the brick
     * 
     * @param portName The string representation of the port to use
     */
    public ColorSensorNXT(String portName) {
        Port port = LocalEV3.get().getPort(portName);
        colorSensor = new NXTLightSensor(port);
        colorProvider = colorSensor.getRedMode();
        colorSample = new float[colorProvider.sampleSize()];
    }

    /**
     * Update the sample array value
     */
    public void update() {
        colorProvider.fetchSample(colorSample, 0);
    }

    /**
     * @return The current value of the sample array
     */
    public float getValue() {
        return colorSample[0];
    }
}
