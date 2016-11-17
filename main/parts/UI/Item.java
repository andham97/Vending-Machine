package main.parts.UI;

import main.enums.SlotID;

public class Item {
	
	private String itemName;
	private int itemPrice;
        private SlotID slotId;
        private int stockSize;
        
        /**
         * Copy-constructor.
         * @param old 
         */
        public Item(Item old) {
            this(old.itemName, old.itemPrice, old.slotId);
        }
        /**
         *Constructor for the Item class, sets the name and price
         * @param newName
         * @param newPrice
         * @param slotId 
         */
	public Item(String newName, int newPrice, SlotID slotId){
		itemName = newName;
		itemPrice = newPrice;
                this.slotId = slotId;
	}
        /**
         * Returns the name of the item
         * @return 
         */
	public String getName(){
		return itemName;
	}
        /**
         * Returns the price of the item
         * @return 
         */
	public int getPrice(){
		return itemPrice;
	}
        /**
         * returns slotID
         * @return 
         */
        public SlotID getSlotId() {
            return slotId;
        }
        /**
         * Sets the size of the stock according to the parameter
         * @param newSize 
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
         * Returns the stocksize
         * @return 
         */
        public int getStockSize(){
            return stockSize;
        }
}
