package main.parts.UI;

import main.enums.SlotID;

/**
 * A class that represent each items price, stock and name
 * 
 * @author fredh
 */
public class Item {
	
	/**
	 * The name of the item
	 */
	private String itemName;
	
	/**
	 * The item price
	 */
	private int itemPrice;
	
	/**
	 * An enum representing the items position in the machine
	 */
    private SlotID slotId;
    
    /**
     * The amount of items left in stock
     */
    private int stockSize;
        
    /**
     * Constructor creating a new instance of an existing item
     * 
     * @param oldItem Item to be copied
     */
    public Item(Item oldItem) {
        this(oldItem.itemName, oldItem.itemPrice, oldItem.slotId);
    }
    
    /**
     *Constructor for the Item class, sets the name and price
     *
     * @param newName Item name
     * @param newPrice Item price
     * @param slotId Item slot ID
     */
	public Item(String newName, int newPrice, SlotID slotId){
		itemName = newName;
		itemPrice = newPrice;
                this.slotId = slotId;
	}
	
    /**
     * @return The name of the item
     */
	public String getName(){
		return itemName;
	}
	
    /**
     * @return The price of the item
     */
	public int getPrice(){
		return itemPrice;
	}
	
    /**
     * @return The items slot ID
     */
    public SlotID getSlotId() {
        return slotId;
    }
    
    /**
     * Sets the size of the stock according to the parameter
     * 
     * @param newSize The new stock size of the item
     */
    public void setStockSize(int newSize){
        stockSize = newSize;
    }
    
    /**
     * Reduces the stocksize by one
     */
    public void reduceStockSize(){
        if(stockSize > 0){
            stockSize --;
        }
    }
    
    /**
     * @return The stock size of the item
     */
    public int getStockSize(){
        return stockSize;
    }
}
