package main;

import main.util.Task;
import main.util.TaskQueue;
import main.util.task.exceptions.IllegalTaskException;

public class ConnectionController implements Runnable {
	public static TaskQueue queue;
	public ConnectionController(){
		ConnectionController.queue = new TaskQueue();
	}
	
	public void start(){
		new Thread(this).start();
	}
	
	public void run(){
		while(Main.isRunning){
			Task task = ConnectionController.queue.getNext();
			if(task != null){
				try {
					switch(task.getTaskType()){
					
					default:
						throw new IllegalTaskException("No implementation for task: " + task.getTaskType().toString());
					}
				}
				catch(IllegalTaskException e){
					e.printStackTrace();
				}
			}
		}
	}
}
