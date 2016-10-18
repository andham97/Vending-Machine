package main.util;

import java.util.ArrayList;

public class TaskQueue {
	private ArrayList<Task> tasks = new ArrayList<Task>();
	
	public void addTask(Task task){
		tasks.add(task);
	}
	
	public Task getNext(){
		Task curTask = null;
		for(int i = 0; i < this.tasks.size(); i++){
			Task tmp = this.tasks.get(i);
			if(curTask == null)
				curTask = tmp;
			else if(tmp.checkPriorityLevel(curTask.getPriority()))
				curTask = tmp;
		}
		tasks.remove(curTask);
		return curTask;
	}
}
