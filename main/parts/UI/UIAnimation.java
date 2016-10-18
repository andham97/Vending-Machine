package main.parts.UI;

import main.Main;

public class UIAnimation implements Runnable {
	
	private Menu menu;
	
	public UIAnimation(){
		menu = new Menu();
	}
	
	public void start(){
		new Thread(this).start();
		menu.drawMenu();
	}
	
	public void run(){
		while(Main.isRunning){
			
		}
	}
}
