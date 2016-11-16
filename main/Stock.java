package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import main.parts.UI.Item;

/**
 *
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class Stock {
    
    private final static String FILENAME = "stock.txt";
    
    public static void save(Item[] items) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(FILENAME, false));
            for (Item item : items) {
                out.write(item.getStockSize() + "\n");
            }
            out.flush();
            out.close();
        } catch (IOException ex) {
            System.err.println("Error writing stock file: " + ex.getMessage());
        }
    }
    
    public static void load(Item[] items) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
            String line;
            int itemNum = 0;
            while ((line = reader.readLine()) != null && itemNum < items.length) {
                if (line.equals("")) {
                    continue;
                }
                
                items[itemNum++].setStockSize(Integer.parseInt(line));
            }
        }
        catch (IOException ex) {
            System.err.println("Error reading stock file: " + ex.getMessage());
        }
    }
}
