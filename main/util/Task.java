package main.util;

import main.enums.Priority;
import main.enums.TaskType;

public class Task {
	private final Priority PRIORITY;
	private final TaskType TASK_TYPE;
	
	public Task(Priority p, TaskType t){
		this.PRIORITY = p;
		this.TASK_TYPE = t;
	}
	
	public TaskType getTaskType(){
		return this.TASK_TYPE;
	}
	
	public Priority getPriority(){
		return this.PRIORITY;
	}
	
	public boolean checkPriorityLevel(Priority p){
		if(p == Priority.Medium && this.PRIORITY == Priority.High)
			return true;
		else if(p == Priority.Low && this.PRIORITY != Priority.Low)
			return true;
		else
			return false;
	}
}
