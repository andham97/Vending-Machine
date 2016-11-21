package com;

/**
 * Constants used to differentiate the different types of data sent between the EV3s
 * 
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class ComProtocol {
	/**
	 * Constant telling the data is of the operation money add
	 */
    public static final int PACKET_MONEY_ADDED   = 1;
    
    /**
     * Constant telling the data is of the operation dispense slot
     */
    public static final int PACKET_DISPENSE_SLOT = 2;
    
    /**
     * Constant telling the data is of the operation store the money
     */
    public static final int PACKET_STORE_MONEY   = 3;
    
    /**
     * Constant telling the data is of the operation refund the money
     */
    public static final int PACKET_REFUND_MONEY  = 4;
    
    /**
     * Constant telling the data is of the operation shutdown the execution
     */
    public static final int PACKET_SHUTDOWN      = 5;
}
