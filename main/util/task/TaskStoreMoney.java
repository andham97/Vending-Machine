package main.util.task;

import main.enums.Priority;
import main.enums.TaskType;
import main.util.Task;

/**
 *
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class TaskStoreMoney extends Task {

    public TaskStoreMoney(Priority p) {
        super(p, TaskType.StoreMoney);
    }
    
}
