package main.util.task;

import main.enums.Priority;
import main.enums.TaskType;
import main.util.Task;

/**
 *
 * @author Magnus C. Hyll <magnus@hyll.no>
 */
public class TaskRefundMoney extends Task {

    public TaskRefundMoney(Priority p) {
        super(p, TaskType.RefundMoney);
    }
    
}
