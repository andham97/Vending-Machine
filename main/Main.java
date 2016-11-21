package main;

import main.parts.UIController;
import main.util.Task;
import main.util.TaskQueue;
import main.util.task.TaskMoneyAdded;
import main.util.task.exceptions.IllegalTaskException;

public class Main {

	/**
	 * A variable that the entire system uses as the isRunning variable
	 */
    public static boolean isRunning = false;
    
    /**
     * The class' queue for 
     */
    public static TaskQueue queue;
    
    /**
     * Storing the amount of money the user has inserted into the system
     */
    public static int WALLET = 0;
    
    /**
     * A reference to the uicontroller class
     */
    private UIController uiController;
    
    /**
     * A reference to the scale controller class
     */
    private SlaveController slaveController;

    public Main() {
        Main.queue = new TaskQueue();
        this.uiController = new UIController();
        this.slaveController = new SlaveController();
    }

    /**
     * @return reference to this object for easier writing in the Launch class
     */
    public Main start() {
        Main.isRunning = true;
        this.uiController.start();
        this.slaveController.start();
        return this;
    }

    /**
     * The main run loop processing the different system tasks
     */
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
