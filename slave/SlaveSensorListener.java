package slave;

import main.parts.sensors.ColorSensorEV3;
import main.parts.sensors.ColorSensorNXT;
import main.parts.sensors.Sensor;

/**
 * The slave sensor controller checking the sensors and notifying the master brick with money inserted
 * 
 * @author Magnus C. Hyll <magnus@hyll.no>
 *
 */
public class SlaveSensorListener implements Runnable {

	/**
	 * The sensor threshold when to notify there is inserted a coin
	 */
    private static final float SENSOR_THRESHOLD = 0.08f;
    
    /**
     * A reference to the slave main object
     */
    private SlaveMain slaveMain;
    
    /**
     * A sensor array
     */
    private Sensor[] sensors;
    
    /**
     * The different sensors value as the money they represent
     */
    private int[] sensorValues;
    
    /**
     * The different sensor idle values
     */
    private float[] idleValues;
    
    /**
     * A flag telling if the sensor has been triggered
     */
    private boolean[] triggeredLastTime;

    /**
     * @param slaveMain The reference to the main slave object
     */
    public SlaveSensorListener(SlaveMain slaveMain) {
        this.slaveMain = slaveMain;
        sensors = new Sensor[4];
        sensorValues = new int[4];
        idleValues = new float[sensors.length];
        triggeredLastTime = new boolean[sensors.length];
        
        /*
         * Initialize the different sensors
         */
        sensors[0] = new ColorSensorNXT("S1");
        sensors[1] = new ColorSensorNXT("S2");
        sensors[2] = new ColorSensorEV3("S3");
        sensors[3] = new ColorSensorEV3("S4");
        sensorValues[0] = 1;
        sensorValues[1] = 10;
        sensorValues[2] = 5;
        sensorValues[3] = 20;
        
        /*
         * Calibrate the sensors and storing their default values
         */
        for (int i = 0; i < sensors.length; i++) {
            for (int j = 0; j < 10; j++) {
                sensors[i].update();
                idleValues[i] = Math.max(sensors[i].getValue(), idleValues[i]);
            }
            
            triggeredLastTime[i] = false;
        }
    } 

    /**
     * Starts a new thread from this object
     */
    public void start() {
        new Thread(this).start();
    }

    /**
     * The main run loop cheking the sensors and sending the data to the master brick
     */
    public void run() {
        while (SlaveMain.running) {
            for (int i = 0; i < sensors.length; i++) {
                sensors[i].update();
                
                boolean isTriggered = sensors[i].getValue() - idleValues[i] > SENSOR_THRESHOLD;
                
                if (isTriggered && !triggeredLastTime[i]) {
                    slaveMain.sendMoneyAddedPacket(sensorValues[i]);
                    try {
                    	
                        /*
                         * Prevent 5'ers being registered twice
                         */
                        Thread.sleep(100);
                    } catch (InterruptedException ex) { }
                }
                
                triggeredLastTime[i] = isTriggered;
            }
        }
    }
}
