package main.util;

import main.enums.ButtonType;
import main.enums.Priority;
import main.enums.TaskType;

public class TaskButtonPress extends Task {
	private final ButtonType DATA;
	public TaskButtonPress(Priority p, ButtonType d) {
		super(p, TaskType.ButtonPress);
		this.DATA = d;
	}

	public ButtonType getData() {
		return DATA;
	}
}
