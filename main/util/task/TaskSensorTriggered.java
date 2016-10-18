package main.util.task;

import main.enums.Priority;
import main.enums.TaskType;
import main.util.Task;

public class TaskSensorTriggered extends Task {
	private final int DATA;
	public TaskSensorTriggered(Priority p, int d) {
		super(p, TaskType.SensorTriggered);
		this.DATA = d;
	}

	public int getData() {
		return DATA;
	}
}
