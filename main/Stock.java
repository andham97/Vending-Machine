package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import main.enums.SlotID;
import main.parts.UI.Item;

/**
 *
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class Stock {

    private final static String FILENAME = "stock.txt";
    
    private static final Item[] originalStock = new Item[]{
        new Item("Melkerull", 20, SlotID.Bottom), 
        new Item("Smil", 20, SlotID.Middle), 
        new Item("Melkebart", 15, SlotID.Top)
    };
    
    private static ArrayList<Item> stock = null;
    
    public static void refill() {
        stock.clear();
        for (Item item : originalStock) {
            item.setStockSize(4);
            stock.add(item);
        }
        save();
    }

    private static void save(ArrayList<Item> items) {
        try {
            StringBuilder encoded = new StringBuilder();
            for (Item item : items) {
                encoded.append(item.getName()).append(";")
                        .append(item.getPrice()).append(";")
                        .append(item.getSlotId()).append(";")
                        .append(item.getStockSize()).append("|");
            }

            FileOutputStream stream = new FileOutputStream(FILENAME, false);
            stream.write(encoded.toString().getBytes("UTF-8"));
            stream.close();

        } catch (IOException ex) {
            System.err.println("Error writing stock file: " + ex.getMessage());
        }
    }
    
    public static void save() {
        if (stock != null) {
            save(stock);
        }
    }
    
    public static ArrayList<Item> get() {
        if (stock == null) {
            stock = new ArrayList<>();
            load(stock);
        }
        return stock;
    }

    private static void load(ArrayList<Item> items) {
        try {
            File file = new File(FILENAME);
            FileInputStream stream = new FileInputStream(FILENAME);
            byte[] bytes = new byte[(int) file.length()];
            stream.read(bytes);
            stream.close();

            items.clear();

            String content = new String(bytes, "UTF-8");
            for (String rawItem : content.split("\\|")) {
                if (!rawItem.equals("")) {
                    String[] itemProps = rawItem.split(";");
                    Item item = new Item(
                            itemProps[0],
                            Integer.parseInt(itemProps[1]),
                            SlotID.valueOf(itemProps[2])
                    );
                    item.setStockSize(Integer.parseInt(itemProps[3]));
                    items.add(item);
                }
            }

        } catch (IOException ex) {
            System.err.println("Error reading stock file: " + ex.getMessage());
        }
    }
}
