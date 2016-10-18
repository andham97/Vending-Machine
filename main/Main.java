package main;

import main.parts.SensorController;
import main.util.Task;
import main.util.TaskQueue;
import main.util.task.TaskSensorTriggered;
import main.util.task.exceptions.IllegalTaskException;

public class Main {
	public static boolean isRunning = false;
	public static TaskQueue queue;
	public static int WALLET = 0;
	
	private SensorController sensorController;
	
	public Main(){
		Main.queue = new TaskQueue();
		this.sensorController = new SensorController();
	}
	
	public Main start(){
		Main.isRunning = true;
		this.sensorController.start();
		return this;
	}
	
	public void run(){
		while(Main.isRunning){
			Task task = Main.queue.getNext();
			if(task != null){
				try {
					switch(task.getTaskType()){
					case SensorTriggered:
						Main.WALLET += ((TaskSensorTriggered) task).getData();
						break;
					case Quit:
						Main.isRunning = false;
						break;
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
