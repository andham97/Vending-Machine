package main.util;

import java.util.ArrayList;

/**
 * A class handling queuing of multiple tasks and taking task priority into account
 * 
 * @author andreashammer
 */
public class TaskQueue {
	/**
	 * An array storing the tasks waiting to be executed
	 */
	private ArrayList<Task> tasks;
	
	/**
	 * Initialize the queue
	 */
	public TaskQueue(){
		 this.tasks = new ArrayList<Task>();
	}
	
	/**
	 * Add a task to the queue
	 * 
	 * @param task The task to be added to the queue
	 */
	public void addTask(Task task){
		tasks.add(task);
	}
	
	/**
	 * @return The next task based on priority
	 */
	public Task getNext(){
		Task curTask = null;
		for(int i = 0; i < this.tasks.size(); i++){
			Task tmp = this.tasks.get(i);
			if(curTask == null)
				curTask = tmp;
			else if(tmp != null && tmp.checkPriorityLevel(curTask.getPriority()))
				curTask = tmp;
		}
		if(curTask != null)
			tasks.remove(curTask);
		return curTask;
	}
}
