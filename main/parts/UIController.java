package main.parts;

import main.Main;
import main.enums.TaskType;
import main.parts.UI.UIAnimation;
import main.parts.UI.UIInput;
import main.util.Task;
import main.util.TaskQueue;

public class UIController implements Runnable {
	public static TaskQueue queue;
	
	private UIInput input;
	private UIAnimation animation;
	
	public UIController(){
		UIController.queue = new TaskQueue();
		this.animation = new UIAnimation();
		this.input = new UIInput();
	}
	
	public void start(){
		new Thread(this).start();
		this.animation.start();
		this.input.start();
	}
	
	public void run(){
		while(Main.isRunning){
			Task task = UIController.queue.getNext();
			if(task != null){
				switch(task.getTaskType()){
				case ButtonPress:
					
					break;
				}
			}
		}
	}
}
