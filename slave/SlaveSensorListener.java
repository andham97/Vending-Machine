package slave;

import main.parts.sensors.ColorSensorEV3;
import main.parts.sensors.ColorSensorNXT;
import main.parts.sensors.Sensor;

public class SlaveSensorListener implements Runnable {

    private static final float SENSOR_THRESHOLD = 0.08f;
    
    private SlaveMain slaveMain;
    private Sensor[] sensors = new Sensor[4];
    private float[] idleValues;
    private boolean[] triggeredLastTime;

    public SlaveSensorListener(SlaveMain slaveMain) {
        this.slaveMain = slaveMain;
        
        idleValues = new float[sensors.length];
        triggeredLastTime = new boolean[sensors.length];
        
        for (int i = 0; i < sensors.length; i++) {
            
            if (i < 2) {
                sensors[i] = new ColorSensorNXT("S" + (i + 1));
            }
            else {
                sensors[i] = new ColorSensorEV3("S" + (i + 1));
            }
            
            for (int j = 0; j < 50; j++) {
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
                    
                    int data = 0;
                    switch (i) {
                        // S1
                        case 0:
                            data = 1;
                            break;
                            
                        // S2
                        case 1:
                            data = 10;
                            break;
                            
                        // S3
                        case 2:
                            data = 5;
                            break;
                            
                        // S4
                        case 3:
                            data = 20;
                            break;
                    }
                    
                    slaveMain.sendSensorTriggeredPacket(data);
                }
                
                triggeredLastTime[i] = isTriggered;
            }
        }
    }
}
