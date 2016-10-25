package main;

import main.parts.SlotDispenser;
import main.util.Task;
import main.util.TaskQueue;
import main.util.task.TaskSlotDispense;
import main.util.task.exceptions.IllegalTaskException;

public class ConnectionController implements Runnable {
	public static TaskQueue queue;
	private SlotDispenser dispenser;
	
	public ConnectionController(){
		ConnectionController.queue = new TaskQueue();
		this.dispenser = new SlotDispenser(new String[]{"A", "B", "C"});
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
					case Dispense:
						this.dispenser.dispenseSlot(((TaskSlotDispense) task).getData());
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
