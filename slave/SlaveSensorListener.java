package slave;

import java.util.logging.Level;
import java.util.logging.Logger;
import main.parts.sensors.ColorSensorEV3;
import main.parts.sensors.ColorSensorNXT;
import main.parts.sensors.Sensor;

public class SlaveSensorListener implements Runnable {

    private static final float SENSOR_THRESHOLD = 0.08f;
    
    private SlaveMain slaveMain;
    private Sensor[] sensors;
    private int[] sensorValues;
    private float[] idleValues;
    private boolean[] triggeredLastTime;

    public SlaveSensorListener(SlaveMain slaveMain) {
        this.slaveMain = slaveMain;
        sensors = new Sensor[4];
        sensorValues = new int[4];
        idleValues = new float[sensors.length];
        triggeredLastTime = new boolean[sensors.length];
        
        sensors[0] = new ColorSensorNXT("S1");
        sensors[1] = new ColorSensorNXT("S2");
        sensors[2] = new ColorSensorEV3("S3");
        sensors[3] = new ColorSensorEV3("S4");
        sensorValues[0] = 1;
        sensorValues[1] = 10;
        sensorValues[2] = 5;
        sensorValues[3] = 20;
        
        for (int i = 0; i < sensors.length; i++) {
            for (int j = 0; j < 10; j++) {
                sensors[i].update();
                idleValues[i] = Math.max(sensors[i].getValue(), idleValues[i]);
            }
            
            triggeredLastTime[i] = false;
        }
    } 

    public void start() {
        new Thread(this).start();
    }

    public void run() {
        while (SlaveMain.running) {
            for (int i = 0; i < sensors.length; i++) {
                sensors[i].update();
                
                boolean isTriggered = sensors[i].getValue() - idleValues[i] > SENSOR_THRESHOLD;
                
                if (isTriggered && !triggeredLastTime[i]) {
                    slaveMain.sendMoneyAddedPacket(sensorValues[i]);
                    try {
                        // Prevent 5'ers being registered twice
                        Thread.sleep(100);
                    } catch (InterruptedException ex) { }
                }
                
                triggeredLastTime[i] = isTriggered;
            }
        }
    }
}
