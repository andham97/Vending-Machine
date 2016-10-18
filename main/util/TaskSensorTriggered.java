package main.util;

import main.enums.Priority;
import main.enums.TaskType;

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
