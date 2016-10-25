package main.parts.UI;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import main.Main;
import main.enums.ButtonType;
import main.enums.Priority;
import main.parts.UIController;
import main.util.task.TaskButtonPress;

public class UIInput implements Runnable {

	private Keys keys;

	public UIInput() {
		this.keys = BrickFinder.getLocal().getKeys();
	}

	public void start() {
		new Thread(this).start();
	}

	public void run() {
		while (Main.isRunning) {
			if (checkQuitKeys())
				UIController.queue.addTask(new TaskButtonPress(Priority.High, ButtonType.Quit));
			else {
				if((Keys.ID_ESCAPE & this.keys.getButtons()) != 0){
					UIController.queue.addTask(new TaskButtonPress(Priority.Medium, ButtonType.Escape));
				}
				if((Keys.ID_UP & this.keys.getButtons()) != 0){
					UIController.queue.addTask(new TaskButtonPress(Priority.Medium, ButtonType.Up));
				}
				if((Keys.ID_DOWN & this.keys.getButtons()) != 0){
					UIController.queue.addTask(new TaskButtonPress(Priority.Medium, ButtonType.Down));
				}
				if((Keys.ID_ENTER & this.keys.getButtons()) != 0){
					UIController.queue.addTask(new TaskButtonPress(Priority.Medium, ButtonType.Enter));
				}
			}
		}
	}

	private boolean checkQuitKeys() {
		int keyVal = this.keys.getButtons();
		int idRight = Keys.ID_RIGHT;
		int idLeft = Keys.ID_LEFT;
		return ((keyVal & idRight) != 0 && (keyVal & idLeft) != 0);
	}
}
