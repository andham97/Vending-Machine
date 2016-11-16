package main;

import java.util.ArrayList;
import lejos.hardware.Sound;
import main.parts.SensorController;
import main.parts.UI.Item;
import main.parts.UIController;
import main.util.Task;
import main.util.TaskQueue;
import main.util.task.TaskMoneyAdded;
import main.util.task.exceptions.IllegalTaskException;

public class Main {

    public static boolean isRunning = false;
    public static TaskQueue queue;
    public static int WALLET = 0;

    private SensorController sensorController;
    private UIController uiController;
    private SlaveController slaveController;

    public Main() {
        Main.queue = new TaskQueue();
        this.sensorController = new SensorController();
        this.uiController = new UIController();
        this.slaveController = new SlaveController();
    }

    public Main start() {
        Main.isRunning = true;
        this.uiController.start();
        this.slaveController.start();
        return this;
    }

    public void run() {
        while (Main.isRunning) {
            Task task = Main.queue.getNext();
            if (task != null) {
                try {
                    switch (task.getTaskType()) {
                        case MoneyAdded:
                            Main.WALLET += ((TaskMoneyAdded) task).getData();
                            uiController.render();
                            break;
                            
                        case Quit:
                            Main.isRunning = false;
                            break;
                            
                        case Dispense:
                            SlaveController.queue.addTask(task);
                            break;
                            
                        case RefillStock:
                            Stock.refill();
                            uiController.render();
                            break;
                            
                        default:
                            throw new IllegalTaskException("No implementation for task: " + task.getTaskType().toString());
                    }
                } catch (IllegalTaskException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
