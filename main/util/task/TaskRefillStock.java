package main.util.task;

import main.enums.Priority;
import main.enums.TaskType;
import main.util.Task;

/** 
 * A task sub class representing the refill stock action
 * 
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class TaskRefillStock extends Task {
	
	/**
	 * @param p The task priority
	 */
    public TaskRefillStock(Priority p) {
        super(p, TaskType.RefillStock);
    }
    
}
