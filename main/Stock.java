package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import main.enums.SlotID;
import main.parts.UI.Item;

/**
 * A class handling the stock and storing it on the file system
 *
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class Stock {

	/**
	 * The stock filename for storing the stock if shutdown
	 */
    private final static String FILENAME = "stock.txt";
    
    /**
     * The original stock initialized with the items available
     */
    private static final ArrayList<Item> originalStock = new ArrayList<>();
    static {
        originalStock.add(new Item("Melkerull", 15, SlotID.Top));
        originalStock.add(new Item("Smil", 20, SlotID.Middle)); 
        originalStock.add(new Item("Melkebart", 20, SlotID.Bottom));
    }
    
    /**
     * An object holding the current stock
     */
    private static ArrayList<Item> currentStock = null;
    
    /**
     * Object used when synchronizing executing of shared resources
     */
    public static final Object lock = new Object();
    
    /**
     * Refills stock, setting 4 items in stock for all items.
     */
    public static void refill() {
        synchronized (lock) {
            currentStock.clear();
            for (Item origItem : originalStock) {
                /*
            	* Copy item from originalStock and add
                */
            	Item item = new Item(origItem);
                item.setStockSize(4);
                currentStock.add(item);
            }
            save();
        }
    }
    
    /**
     * Saves the current item list to stock file. 
     */
    public static void save() {
        synchronized (lock) {
            if (currentStock != null) {
                try {
                    StringBuilder encoded = new StringBuilder();
                    for (Item original : originalStock) {

                        int numInStock = 0;

                        /*
                         *  Checks if this item exists in currentStock,
                         *  meaning there are more than one of this item left in stock
                         */
                        for (Item item : currentStock) {
                            if (item.getName().equals(original.getName())) {
                                numInStock = item.getStockSize();
                                break;
                            }
                        }

                        encoded.append(original.getName()).append("=")
                               .append(numInStock).append(" ");
                    }

                    FileOutputStream stream = new FileOutputStream(FILENAME, false);
                    stream.write(encoded.toString().getBytes("UTF-8"));
                    stream.close();

                } catch (IOException ex) {
                    System.err.println("Error writing stock file: " + ex.getMessage());
                }
            }
        }
    }
    
    /**
     * @return The current item list, excluding items which are out of stock.
     */
    public static ArrayList<Item> get() {
        synchronized (lock) {
            if (currentStock == null) {
                currentStock = new ArrayList<>();
                try {
                	
                	/*
                	 * Read the data from the stock file
                	 */
                    File file = new File(FILENAME);
                    FileInputStream stream = new FileInputStream(FILENAME);
                    byte[] bytes = new byte[(int) file.length()];
                    stream.read(bytes);
                    stream.close();
                    String content = new String(bytes, "UTF-8");

                    currentStock.clear();

                    /*
                     * Adding the items to the current stock list
                     */
                    for (String rawItem : content.split(" ")) {
                        if (!rawItem.equals("")) {
                            String[] itemProps = rawItem.split("=");

                            /*
                             * Only add to item list if there are any in stock
                             */
                            if (!itemProps[1].equals("0")) {
                                for (Item origItem : originalStock) {
                                    if (origItem.getName().equals(itemProps[0])) {
                                        Item item = new Item(origItem);
                                        item.setStockSize(Integer.parseInt(itemProps[1]));
                                        currentStock.add(item);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                } catch (IOException | NumberFormatException ex) {
                    System.out.println("Error reading stock file: " + ex.getMessage()
                            + ", using originalStock");

                    refill();
                }
            }
        }
        return currentStock;
    }
}
