package main.parts.UI;

public class Item {
	
	private String itemName;
	private int itemPrice;
        private int stockSize;
	//Constructor for the Item class, sets the name and price
	public Item(String newName, int newPrice){
		itemName = newName;
		itemPrice = newPrice;
	}
	//Returns the name of the item
	public String getName(){
		return itemName;
	}
	//Returns the price of the item
	public int getPrice(){
		return itemPrice;
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
