package main.util.task;

import main.enums.Priority;
import main.enums.SlotID;
import main.enums.TaskType;
import main.util.Task;

/**
 * A task sub class representing the dispense slot action
 * 
 * @author Magnus C. Hyll <magnus@hyll.no>
 * @author andreashammer
 */
public class TaskDispenseSlot extends Task {
	
	/**
	 * The data carried in the task
	 */
	private final int DATA;

	/**
	 * Initialize a new instance of the class TaskDispenseSlot
	 * 
	 * @param p Task priority
	 * @param id The slot ID the task contains
	 */
	public TaskDispenseSlot(Priority p, SlotID id) {
		super(p, TaskType.Dispense);
		switch(id){
		case Bottom:
			this.DATA = 1;
			break;
		case Middle:
			this.DATA = 2;
			break;
		case Top:
			this.DATA = 3;
			break;
		default:
			this.DATA = 0;
		}
	}
	
	/**
	 * @return The slot ID of the task
	 */
	public int getData(){
		return this.DATA;
	}
}
