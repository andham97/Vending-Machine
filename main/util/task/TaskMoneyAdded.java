package main.util.task;

import main.enums.Priority;
import main.enums.TaskType;
import main.util.Task;

/**
 * A task sub class representing the money add action
 * 
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class TaskMoneyAdded extends Task {

	/**
	 * The data stored in the task
	 */
    private final int DATA;

    /**
     * Initialize a new instance of the class TaskMoneyAdded with priority level and data
     * 
     * @param pri The task priority
     * @param amount The data stored in the task
     */
    public TaskMoneyAdded(Priority pri, int amount) {
        super(pri, TaskType.MoneyAdded);
        this.DATA = amount;
    }

    /**
     * @return The data stored in the task
     */
    public int getData() {
        return DATA;
    }
}
