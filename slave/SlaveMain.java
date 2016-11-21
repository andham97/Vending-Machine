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
 * The main slave controller handling data connection with the master brick
 * Handling the data received and executing the appropriate command
 *
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class SlaveMain {
    
	/**
	 * The slave run loop flag
	 */
    public static boolean running;
    
    /**
     * A reference to the socket connection with the master brick
     */
    private Socket connection;
    
    /**
     * The connection input stream reference
     */
    private DataInputStream in;
    
    /**
     * The connection output stream reference
     */
    private DataOutputStream out;
    
    /**
     * A reference to the slave sensor listener
     */
    private SlaveSensorListener listener;
    
    /**
     * A reference to the bottom engine
     */
    private RegulatedMotor slotMotorBottom;
    
    /**
     * A reference to the middle engine
     */
    private RegulatedMotor slotMotorMiddle;
    
    /**
     * A reference to the top engine
     */
    private RegulatedMotor slotMotorTop;
    
    /**
     * A reference to the shel engine
     */
    private RegulatedMotor moneyShelfMotor;
    
    /**
     * Initialize the engine references and sensor listener
     */
    public SlaveMain() {
        listener = new SlaveSensorListener(this);
        slotMotorBottom = Motor.A;
        slotMotorMiddle = Motor.B;
        slotMotorTop = Motor.C;
        moneyShelfMotor = Motor.D;
    }
    
    /**
     * Connection to the master brick
     * 
     * @throws IOException
     */
    private void connectToMaster() throws IOException {
        System.out.println("Kobler til...");
        String masterIp = BrickFinder.find("MASTER")[0].getIPAddress();
        connection = new Socket(masterIp, 9999);
        connection.setTcpNoDelay(true);
        in = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
        out = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
        
        System.out.println("Tilkoblet");
    }
    
    /**
     * Start the sensor controller
     * Main run loop checking for data packets from master
     */
    private void start() {
        listener.start();
        
        while (running) {
            try {
                /*
                 * Check for awaiting data packets from master
                 */
                if (in.available() >= 8) {
                    int packetType = in.readInt();
                    int packetPayload = in.readInt();

                    switch (packetType) {
                        case ComProtocol.PACKET_DISPENSE_SLOT:
                            dispenseSlot(packetPayload);
                            break;
                            
                        case ComProtocol.PACKET_STORE_MONEY:
                            storeMoney();
                            break;
                            
                        case ComProtocol.PACKET_REFUND_MONEY:
                            refundMoney();
                            break;
                            
                        case ComProtocol.PACKET_SHUTDOWN:
                            System.out.println("Shutdown");
                            running = false;
                            break;

                        default:
                            System.out.println("Unknown packet: " + packetType
                                + ", payload: " + packetPayload);
                    }
                }
            }
            catch (IOException ex) {
                System.out.println("I/O ERROR: " + ex.getMessage());
            }
        }
    }

    /**
     * Close the connection to the master brick
     * 
     * @throws IOException
     */
    private void closeConnections() throws IOException {
        out.close();
        in.close();
        connection.close();
    }
    
    /**
     * Rotate the shelf engine to store the money in the machine
     */
    private void storeMoney() {
        moneyShelfMotor.rotate(-120);
        moneyShelfMotor.rotate(120);
    }
    
    /**
     * Rotate the shelf engine to refund the inserted money
     */
    private void refundMoney() {
        moneyShelfMotor.rotate(90);
        moneyShelfMotor.rotate(-90);
    }
    
    /**
     * Rotate the correct slot engine to dispense the purchased good
     * 
     * @param slotNum The slot to dispense the item from
     */
    private void dispenseSlot(int slotNum) {
        switch (slotNum) {
            case 1:
                slotMotorBottom.rotate(400);
                slotMotorBottom.rotate(-400);
                break;
                
            case 2:
                slotMotorMiddle.rotate(-400);
                slotMotorMiddle.rotate(400);
                break;
                
            case 3:
                slotMotorTop.rotate(-260);
                slotMotorTop.rotate(260);
                break;
        }
    }
    
    /**
     * Send a packet with the amount of money inserted by the customer
     * 
     * @param payload The data to send the master brick
     */
    public synchronized void sendMoneyAddedPacket(int payload) {
        try {
            out.writeInt(ComProtocol.PACKET_MONEY_ADDED);
            out.writeInt(payload);
            out.flush();
        }
        catch (IOException ex) {
            System.err.println("Error sending PACKET_MONEY_ADDED: "
                    + ex.getMessage());
        }
    }

    /**
     * Init the slave and connect to master
     * Launch the main run loop
     * Disconnect from master
     * 
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Starter...");
        try {
            running = true;
            SlaveMain slave = new SlaveMain();
            slave.connectToMaster();
            slave.start();
            slave.closeConnections();
        } catch (IOException ex) {
            System.err.println("I/O ERROR: " + ex.getMessage());
        }
    }
}
