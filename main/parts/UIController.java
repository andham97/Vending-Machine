package main.parts;

import main.Main;
import main.enums.ButtonType;
import main.enums.Priority;
import main.enums.exceptions.IllegalButtonTypeExteption;
import main.parts.UI.UIAnimation;
import main.parts.UI.UIInput;
import main.util.Task;
import main.util.TaskQueue;
import main.util.task.TaskButtonPress;
import main.util.task.TaskQuit;
import main.util.task.exceptions.IllegalTaskException;

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
			try {
				if(task != null){
					switch(task.getTaskType()){
					case ButtonPress:
						ButtonType bt = ((TaskButtonPress) task).getData();
						switch(bt){
						case Quit:
							Main.queue.addTask(new TaskQuit(Priority.High));
						case Up:
							this.animation.up();
							break;
						case Down:
							this.animation.down();
							break;
						case Enter:
							this.animation.enter();
							break;
						case Escape:
							this.animation.escape();
							break;
						default:
							throw new IllegalButtonTypeExteption("Not a registered button: " + bt.toString());
						}
					default:
						throw new IllegalTaskException("No implementation for task: " + task.getTaskType().toString());
					}
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
