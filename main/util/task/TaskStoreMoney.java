package main.util.task;

import main.enums.Priority;
import main.enums.TaskType;
import main.util.Task;

/**
 * A task sub class notifying the system to store the money
 *
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class TaskStoreMoney extends Task {

	/**
	 * Initialize a new store money task
	 * 
	 * @param p The task priority
	 */
    public TaskStoreMoney(Priority p) {
        super(p, TaskType.StoreMoney);
    }
    
}
