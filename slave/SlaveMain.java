package slave;

import com.ComProtocol;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

/**
 *
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class SlaveMain {
    
    public static boolean running;
    
    private Socket connection;
    private DataInputStream in;
    private DataOutputStream out;
    
    private SlaveSensorListener listener;
    private RegulatedMotor slotMotorBottom;
    private RegulatedMotor slotMotorMiddle;
    private RegulatedMotor slotMotorTop;
    
    public SlaveMain() {
        listener = new SlaveSensorListener(this);
        slotMotorBottom = Motor.A;
        slotMotorMiddle = Motor.B;
        slotMotorTop = Motor.C;
    }
    
    private void connectToMaster() throws IOException {
        System.out.print("Kobler til ");
        String masterIp = BrickFinder.find("MASTER")[0].getIPAddress();
        System.out.println(masterIp + ":9999");
        
        connection = new Socket(masterIp, 9999);
        connection.setTcpNoDelay(true);
        in = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
        out = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
        
        System.out.println("Tilkoblet");
    }
    
    private void start() {
        listener.start();
        
        while (running) {
            try {
                // Check for awaiting data packets from master
                if (in.available() >= 8) {
                    int packetType = in.readInt();
                    int packetPayload = in.readInt();

                    switch (packetType) {
                        case ComProtocol.PACKET_DISPENSE_SLOT:
                            dispenseSlot(packetPayload);
                            break;

                        default:
                            System.out.println("Unknown packet: " + packetType
                                + ", payload: " + packetPayload);
                    }
                }
            }
            catch (IOException ex) {
                System.out.println("IO ERROR: " + ex.getMessage());
            }
        }
    }

    private void closeConnections() throws IOException {
        out.close();
        in.close();
        connection.close();
    }
    
    private void dispenseSlot(int slotNum) {
        switch (slotNum) {
            case 1:
                slotMotorBottom.rotate(520);
                slotMotorBottom.rotate(-520);
                break;
                
            case 2:
                slotMotorMiddle.rotate(-520);
                slotMotorMiddle.rotate(520);
                break;
                
            case 3:
                slotMotorTop.rotate(-260);
                slotMotorTop.rotate(260);
                break;
        }
    }
    
    public synchronized void sendSensorTriggeredPacket(int payload) {
        try {
            out.writeInt(ComProtocol.PACKET_MONEY_ADDED);
            out.writeInt(payload);
            out.flush();
        }
        catch (IOException ex) {
            System.err.println("Error sending PACKET_SENSOR_TRIGGERED: "
                    + ex.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            running = true;
            SlaveMain slave = new SlaveMain();
            slave.connectToMaster();
            slave.start();
            slave.closeConnections();
        } catch (IOException ex) {
            System.err.println("IO ERROR: " + ex.getMessage());
        }
    }
}
