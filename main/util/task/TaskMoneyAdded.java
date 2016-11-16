package main.util.task;

import main.enums.Priority;
import main.enums.TaskType;
import main.util.Task;

public class TaskMoneyAdded extends Task {

    private final int amount;

    public TaskMoneyAdded(Priority pri, int amount) {
        super(pri, TaskType.MoneyAdded);
        this.amount = amount;
    }

    public int getData() {
        return amount;
    }
}
