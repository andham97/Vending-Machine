package main.parts.UI;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import main.Main;
import main.enums.ButtonType;
import main.enums.Priority;
import main.parts.UIController;
import main.util.TaskButtonPress;

public class UIInput implements Runnable {
	
	private Keys keys;
	
	public UIInput(){
		keys = BrickFinder.getLocal().getKeys();
	}
	
	public void start(){
		new Thread(this).start();
	}
	
	public void run(){
		while(Main.isRunning){
			if(checkQuitKeys())
				UIController.queue.addTask(new TaskButtonPress(Priority.High, ButtonType.Quit));
		}
	}
	
	private boolean checkQuitKeys(){
		int keyVal = keys.getButtons();
		int idRight = Keys.ID_RIGHT;
		int idLeft = Keys.ID_LEFT;
		return ((keyVal & idRight) != 0 && (keyVal & idLeft) != 0);
		
	}
}
