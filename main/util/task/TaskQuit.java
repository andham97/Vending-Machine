package main.util.task;

import main.enums.Priority;
import main.enums.TaskType;
import main.util.Task;

/**
 * A task sub class notifying the system to exit the program
 * 
 * @author andreashammer
 */
public class TaskQuit extends Task {
	
	/**
	 * Initializes a new quit task with the specified priority
	 * 
	 * @param p The task priority
	 */
	public TaskQuit(Priority p){
		super(p, TaskType.Quit);
	}
}
