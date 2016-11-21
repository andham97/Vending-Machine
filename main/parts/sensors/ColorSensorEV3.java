package main.parts.sensors;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

/**
 * An extension of the EV3ColorSensor class with extra functionality for simpler code
 * 
 * @author andreashammer
 */
public class ColorSensorEV3 implements Sensor {

	/**
	 * Reference to color sensor
	 */
    private EV3ColorSensor colorSensor;
    
    /**
     * Reference to sample provider of color sensor
     */
    private SampleProvider colorProvider;
    
    /**
     * Array containing the samples fetched from the sensors
     */
    private float[] colorSample;

    /**
     * Initialize a new instance of ColorSensorEV3 and activate the port on the brick
     * 
     * @param portName The string representation of the port to use
     */
    public ColorSensorEV3(String portName) {
        Port port = LocalEV3.get().getPort(portName);
        colorSensor = new EV3ColorSensor(port);
        colorProvider = colorSensor.getRedMode();
        colorSample = new float[colorProvider.sampleSize()];
    }

    /**
     * Update the sample array values
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
