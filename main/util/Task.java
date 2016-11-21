package main.util;

import main.enums.Priority;
import main.enums.TaskType;

/**
 * A class representing the basic functionality of a generic task
 * 
 * @author andreashammer
 */
public class Task {
	
	/**
	 * The task priority
	 */
	private final Priority PRIORITY;
	
	/**
	 * The task type
	 */
	private final TaskType TASK_TYPE;
	
	/**
	 * @param p The task priority
	 * @param t The task type
	 */
	public Task(Priority p, TaskType t){
		this.PRIORITY = p;
		this.TASK_TYPE = t;
	}
	
	/**
	 * @return The tasks type
	 */
	public TaskType getTaskType(){
		return this.TASK_TYPE;
	}
	
	/**
	 * @return The tasks priority
	 */
	public Priority getPriority(){
		return this.PRIORITY;
	}
	
	/**
	 * Compare the current task and the task parameters priority
	 * @param p The task to compare
	 * @return true if this task is higher prioritized
	 */
	public boolean checkPriorityLevel(Priority p){
		if(p == Priority.Medium && this.PRIORITY == Priority.High)
			return true;
		else if(p == Priority.Low && this.PRIORITY != Priority.Low)
			return true;
		else
			return false;
	}
}
