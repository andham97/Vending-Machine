package main.util.task;

import main.enums.Priority;
import main.enums.TaskType;
import main.util.Task;

/**
 * A task sub class notifying the system to refund the inserted money
 *
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class TaskRefundMoney extends Task {

	/**
	 * Initialized a new refund money task
	 * 
	 * @param p The task priority
	 */
    public TaskRefundMoney(Priority p) {
        super(p, TaskType.RefundMoney);
    }
    
}
