package main.parts.UI;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.*;

public class Menu {
	
	private GraphicsLCD graphics = LocalEV3.get().getGraphicsLCD();
	private final int screenWidth;
	private final int screenHeight;
	private int menuSelection = 1;
	
	public Menu(){
		screenWidth = graphics.getWidth();
		screenHeight = graphics.getHeight();
	}
	
	public void drawMenu(){
		for(int nr = 1; nr<4; nr++){
			if(nr == menuSelection)
				drawOptionRect(nr, "Chocolate"+nr, true);
			else
				drawOptionRect(nr, "Chocolate"+nr, false);
		}
	}
	
	private void drawOptionRect(int y, String text, boolean selected){
		int height = screenHeight/4;
		int width1 = screenWidth-45;
		int width2 = screenWidth-width1;
		drawRectBetter(0, height*y, width1, height, selected);
		drawRectBetter(width1, height*y, width2, height, selected);
		graphics.drawString(text, 20, (height*y)+(height/4), 0, selected);
		graphics.drawString("20kr", width1-1, (height*y)+(height/4), 0, selected);
	}
	
	private void drawRectBetter(int x, int y, int width, int height, boolean fill){
		if(!fill)
			graphics.drawRect(x, y, width, height);
		else
			graphics.fillRect(x, y, width, height);
	}
}
