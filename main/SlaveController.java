package main;

import com.ComProtocol;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import main.enums.Priority;
import main.util.Task;
import main.util.TaskQueue;
import main.util.task.TaskMoneyAdded;
import main.util.task.TaskDispenseSlot;
import main.util.task.exceptions.IllegalTaskException;

/**
 * Control the slave EV3 sending and receiving data from the different
 * 
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class SlaveController implements Runnable {

	/**
	 * The controllers task queue
	 */
    public static TaskQueue queue;

    /**
     * Initialize the queue
     */
    public SlaveController() {
        queue = new TaskQueue();
    }

    /**
     * Launches a new thread from this class
     */
    public void start() {
        new Thread(this, "SlaveController").start();
    }

    /**
     * The main slave controller loop
     * Connect to the slave
     * Read the data stream and check for new data from the slave
     * Write data to the stream for the slave, fetched from the task queue
     * Disconnect from the slave
     */
    public void run() {
        try {
        	/*
        	 * Connect to the socket
        	 */
            ServerSocket server = new ServerSocket(9999);
            Socket connection = server.accept();
            connection.setTcpNoDelay(true);
            DataInputStream in = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));

            while (Main.isRunning) {
                
                try {
                    /*
                     * Check for awaiting data packets from slave
                     */
                    if (in.available() >= 8) {
                        int packetType = in.readInt();
                        int packetPayload = in.readInt();
                        
                        switch (packetType) {
                            case ComProtocol.PACKET_MONEY_ADDED:
                                Main.queue.addTask(new TaskMoneyAdded(Priority.Low, packetPayload));
                                break;

                            default:
                                System.out.println("Unknown packet: " + packetType
                                    + ", payload: " + packetPayload);
                        }
                    }
                }
                catch (IOException ex) {
                    System.err.println("IO ERROR: " + ex.getMessage());
                }
                
                /*
                 * Check for awaiting tasks in queue
                 */
                Task task = queue.getNext();
                if (task != null) {
                    try {
                        switch (task.getTaskType()) {
                            case Dispense:
                            	
                                /*
                                 * Sends a PACKET_DISPENSE_SLOT packet to slave
                                 */
                                out.writeInt(ComProtocol.PACKET_DISPENSE_SLOT);
                                out.writeInt(((TaskDispenseSlot) task).getData());
                                out.flush();
                                break;
                                
                            case StoreMoney:
                            	
                                /*
                                 * Sends a PACKET_STORE_MONEY packet to slave
                                 */
                                out.writeInt(ComProtocol.PACKET_STORE_MONEY);
                                out.writeInt(0);
                                out.flush();
                                break;
                                
                            case RefundMoney:
                            	
                                /*
                                 * Sends a PACKET_REFUND_MONEY packet to slave
                                 */
                                out.writeInt(ComProtocol.PACKET_REFUND_MONEY);
                                out.writeInt(0);
                                out.flush();
                                break;

                            default:
                                throw new IllegalTaskException("No implementation for task: " + task.getTaskType().toString());
                        }
                    } catch (IllegalTaskException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            /*
             * Flush the stream and close the connection
             */
            out.writeInt(ComProtocol.PACKET_SHUTDOWN);
            out.writeInt(0);
            out.flush();

            out.close();
            in.close();
            connection.close();
            server.close();

        } catch (IOException ex) {
            System.err.println("IO ERROR: " + ex.getMessage());
        }
    }
}
