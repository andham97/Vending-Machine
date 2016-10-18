package main.parts.UI;

import lejos.hardware.Keys;
import main.Main;
import main.enums.ButtonType;
import main.enums.Priority;
import main.parts.UIController;

public class UIInput implements Runnable {
	public UIInput(){
		
	}
	
	public void start(){
		new Thread(this).start();
	}
	
	public void run(){
		while(Main.isRunning){
			if(checkQuitKeys())
				UIController.queue.addTask(new TaskButtonPress(Priority.High, Quit.Escape));
		}
	}
	
	private boolean checkQuitKeys(){
		int keyVal = keys.getButtons();
		int idRight = Keys.ID_RIGHT;
		int idLeft = Keys.ID_LEFT;
		return ((keyVal & idRight) != 0 && (keyVal & idLeft) != 0);
		
	}
}
