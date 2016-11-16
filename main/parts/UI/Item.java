package main.parts.UI;

import lejos.hardware.lcd.Image;

import main.enums.SlotID;

public class Item {
	
	private String itemName;
	private int itemPrice;
        private SlotID slotId;
        private int stockSize;
        
	//Constructor for the Item class, sets the name and price
	public Item(String newName, int newPrice, SlotID slotId){
		itemName = newName;
		itemPrice = newPrice;
                this.slotId = slotId;
	}
	//Returns the name of the item
	public String getName(){
		return itemName;
	}
	//Returns the price of the item
	public int getPrice(){
		return itemPrice;
	}
        public SlotID getSlotId() {
            return slotId;
        }
        //Sets the size of the stock according to the parameter
        public void setStockSize(int newSize){
            stockSize = newSize;
        }
        //Reduces the stocksize by one
        public void reduceStockSize(){
            if(stockSize > 0){
                stockSize --;
            }
        }
        //Returns the stocksize
        public int getStockSize(){
            return stockSize;
        }
}
