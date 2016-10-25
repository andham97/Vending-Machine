package main.parts.UI;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.*;

public class Menu {
	
	Item item1 = new Item("Melkerull", 20);
	Item item2 = new Item("Smil", 20);
	Item item3 = new Item("Melkebart", 15);
	Item[] itemArray = new Item[]{item1, item2, item3};
	String[] confArray = new String[]{"Y", "N"};
	
	private GraphicsLCD graphics = LocalEV3.get().getGraphicsLCD();
	private Keys keys;
	private final int screenWidth;
	private final int screenHeight;
	private int menuSelection = 0;
	private int confSelection = 0;
	private int itemHLimit = itemArray.length-1;
	private int itemLLimit = 0;
	private boolean keyReleased = true;
	private int menuLevel = 0;
	
	int idRight = Keys.ID_RIGHT;
	int idLeft = Keys.ID_LEFT;
	int idUp = Keys.ID_UP;
	int idDown = Keys.ID_DOWN;
	int idEnter = Keys.ID_ENTER;
	
	public Menu(){
		keys = BrickFinder.getLocal().getKeys();
		screenWidth = graphics.getWidth();
		screenHeight = graphics.getHeight();
	}
	
	public void drawMenu(){
		graphics.clear();
		drawChangeSection();
		if(menuLevel == 0){
			for(int nr = 0; nr<itemArray.length; nr++){
				if(nr == menuSelection)
					drawOptionRect(nr+1, itemArray[nr], true);
				else
					drawOptionRect(nr+1, itemArray[nr], false);
			}
		}else if(menuLevel == 1){
			drawConfirmOption(itemArray[menuSelection]);
		}
	}
	
	private void drawChangeSection(){
		int posX = screenWidth-80;
		int width = screenWidth-posX;
		int height = screenHeight/4;
		graphics.drawRect(posX, 0, width, height);
		graphics.setFont(Font.getLargeFont());
		graphics.drawString("20kr", posX, 0, 0);
		int lineXAngle1 = 55;
		int lineXAngle2 = 70;
		graphics.drawLine(0, 0, 0, height);
		graphics.drawLine(0, height, lineXAngle1, height);
		graphics.drawLine(lineXAngle1 , height, lineXAngle2, height/2);
		graphics.drawLine(lineXAngle2, height/2, lineXAngle2, 0);
		graphics.drawLine(lineXAngle2, 0, 0, 0);
		graphics.setFont(Font.getDefaultFont());
		graphics.drawString("Veksel", 0, height/4, 0);
	}
	
	private void drawConfirmOption(Item item){
		String text = item.getName();
		int price = item.getPrice();
		int height = screenHeight/4;
		int width1 = screenWidth-75;
		int width2 = screenWidth-width1-35;
		drawRectBetter(0, height*3, width1, height, false);
		drawRectBetter(width1, height*3, width2, height, false);
		graphics.setFont(Font.getDefaultFont());
		graphics.drawString(text, 10, (height*3)+(height/4), 0);
		graphics.drawString(price+"kr", width1+1, (height*3)+(height/4), 0);
		
		drawRectBetter(0, height, width1+width2, height*2, true);
		
		int confButtonHeight = (screenHeight-(screenHeight/4))/2;
		int confButtonWidth = screenWidth-(width1+width2);
		
		for(int nr = 0; nr<2; nr++){
			if(nr == confSelection)
				drawConfButton(width1+width2, (confButtonHeight*nr+1)+height, confButtonWidth, confButtonHeight, confArray[nr], true);
			else
				drawConfButton(width1+width2, (confButtonHeight*nr+1)+height, confButtonWidth, confButtonHeight, confArray[nr], false);
		}
	}
	
	private void drawConfButton(int xPos, int yPos, int width, int height, String confString, boolean selected){
		drawRectBetter(xPos, yPos, width, height, selected);
		graphics.setFont(Font.getLargeFont());
		graphics.drawString(confString, xPos+(width/2-10), yPos+(height/2-10), 0, selected);
	}
	
	private void drawOptionRect(int y, Item item, boolean selected){
		String text = item.getName();
		int price = item.getPrice();
		int height = screenHeight/4;
		int width1 = screenWidth-45;
		int width2 = screenWidth-width1;
		drawRectBetter(0, height*y, width1, height, selected);
		drawRectBetter(width1, height*y, width2, height, selected);	
		graphics.setFont(Font.getDefaultFont());
		graphics.drawString(text, 35, (height*y)+(height/4), 0, selected);
		graphics.drawString(price+"kr", width1+1, (height*y)+(height/4), 0, selected);
	}
	
	private void drawRectBetter(int x, int y, int width, int height, boolean fill){
		if(!fill)
			graphics.drawRect(x, y, width, height);
		else
			graphics.fillRect(x, y, width, height);
	}
	
	public void mainLoop(){
		while(!checkQuitKeys()){
			checkKeys();
		}
	}
	
	private void checkKeys(){
		int keyVal = keys.getButtons();
		if(keyReleased){
			if(menuLevel == 0){
				if((keyVal & idUp) != 0){
					keyReleased = false;
					if(menuSelection > itemLLimit)
						updateMenuSelection(-1);
					else
						updateMenuSelection(+itemHLimit);
				}else if ((keyVal & idDown) != 0){
					keyReleased = false;
					if(menuSelection < itemHLimit)
						updateMenuSelection(1);
					else
						updateMenuSelection(-itemHLimit);
				}else if((keyVal & idEnter) != 0){
					keyReleased = false;
					updateMenuLevel(1);
				}
			}else if(menuLevel == 1){
				if((keyVal & idUp) != 0 || (keyVal & idDown) != 0){
					keyReleased = false;
					updateConfirmationSelection();
				}else if((keyVal & idEnter) != 0){
					if(confSelection == 0)
						purchaseItem();
					keyReleased = false;
					updateMenuLevel(-1);
				}
			}
		}else if(keyVal == 0){
			keyReleased = true;
		}
	}
	
	public void purchaseItem(){
		//Purchase code goes here.
	}
	
	private void updateMenuSelection(int change){
		menuSelection += change;
		drawMenu();
	}
	
	private void updateConfirmationSelection(){
		if(confSelection == 0)
			confSelection ++;
		else
			confSelection --;
		drawMenu();
	}
	
	private void updateMenuLevel(int change){
		menuLevel += change;
		drawMenu();
	}
	
	public boolean checkQuitKeys(){
		int keyVal = keys.getButtons();
		return ((keyVal & idRight) != 0 && (keyVal & idLeft) != 0);
		
	}
}
