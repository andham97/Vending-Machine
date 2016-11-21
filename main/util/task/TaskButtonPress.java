package main.util.task;

import main.enums.ButtonType;
import main.enums.Priority;
import main.enums.TaskType;
import main.util.Task;

/**
 * A task sub class representing the button press action
 * 
 * @author andreashammer
 */
public class TaskButtonPress extends Task {
	
	/**
	 * The task data representation
	 */
	private final ButtonType DATA;
	
	/**
	 * 
	 * @param p Priority level
	 * @param d Task's data
	 */
	public TaskButtonPress(Priority p, ButtonType d) {
		super(p, TaskType.ButtonPress);
		this.DATA = d;
	}

	/**
	 * @return The data the task carries
	 */
	public ButtonType getData() {
		return DATA;
	}
}
