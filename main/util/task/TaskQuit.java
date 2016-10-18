package main.util.task;

import main.enums.Priority;
import main.enums.TaskType;
import main.util.Task;

public class TaskQuit extends Task {
	public TaskQuit(Priority p){
		super(p, TaskType.Quit);
	}
}
