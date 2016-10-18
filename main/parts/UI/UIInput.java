package main.parts.UI;

import main.Main;

public class UIInput implements Runnable {
	public UIInput(){
		
	}
	
	public void start(){
		new Thread(this).start();
	}
	
	public void run(){
		while(Main.isRunning){
			
		}
	}
}
