package main.util.task;

import main.enums.Priority;
import main.enums.SlotID;
import main.enums.TaskType;
import main.util.Task;

public class TaskDispenseSlot extends Task {
	private int DATA;

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
	
	public int getData(){
		return this.DATA;
	}
}
