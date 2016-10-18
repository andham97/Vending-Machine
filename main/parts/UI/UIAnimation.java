package main.parts.UI;

import main.Main;

public class UIAnimation implements Runnable {
	public UIAnimation(){
		
	}
	
	public void start(){
		new Thread(this).start();
	}
	
	public void run(){
		while(Main.isRunning){
			
		}
	}
}
