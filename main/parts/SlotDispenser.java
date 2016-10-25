package main.parts;

import java.io.IOException;
import lejos.hardware.BrickFinder;
import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.RegulatedMotor;

/**
 * Slot dispenser controller.
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class SlotDispenser {
    
    private static final int ROTATION_ANGLE = 260;
    
    private RemoteRequestEV3 slaveBrick;
    private RegulatedMotor[] slotMotors;
    
    public SlotDispenser(String[] slotMotorPorts) {
        slaveBrick = null;
            
        try {
            System.out.print("Kobler til SLAVE");
            String slaveIp = BrickFinder.find("SLAVE")[0].getIPAddress();
            System.out.print(" (" + slaveIp + ")... ");
            slaveBrick = new RemoteRequestEV3(slaveIp);
            System.out.println("Tilkoblet");
            
            slotMotors = new RegulatedMotor[slotMotorPorts.length];
            for (int i = 0; i < slotMotorPorts.length; i++) {
                slotMotors[i] = slaveBrick.createRegulatedMotor(slotMotorPorts[i], 'L');
            }
        }
        catch (IOException e) {
            System.out.println("Klarte ikke å koble til SLAVE");
        }
    }
    
    public void dispenseSlot(int slotNum) {
        if (slaveBrick != null && slotNum >= 1 && slotNum <= slotMotors.length) {
            RegulatedMotor motor = slotMotors[slotNum - 1];
            
            // TODO This sequence including value of ROTATION_ANGLE needs testing
            motor.rotate(ROTATION_ANGLE);
            motor.rotate(-ROTATION_ANGLE);
        }
    }
}
