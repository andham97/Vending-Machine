package main.parts;

import main.Main;
import main.enums.ButtonType;
import main.enums.exceptions.IllegalButtonTypeExteption;
import main.parts.UI.Menu;
import main.parts.UI.UIInput;
import main.util.Task;
import main.util.TaskQueue;
import main.util.task.TaskButtonPress;
import main.util.task.exceptions.IllegalTaskException;

/**
 * 
 * @author andreashammer
 *
 */
public class UIController implements Runnable {
	
	/**
	 * The TaskQueue where other threads place tasks for the UIController thread to execute
	 */
    public static TaskQueue queue;

    /**
     * Referance to the input controller object
     */
    private UIInput input;
    
    /**
     * Referance to the menu object carrying out draw operations to the display
     */
    private Menu menu;
    
    /**
     * Used to flag if the display needs to be updated
     */
    private boolean needsRender;

    /**
     * Setting up the TaskQueue, menu and input controllers
     */
    public UIController() {
        UIController.queue = new TaskQueue();
        this.menu = new Menu();
        this.input = new UIInput();
        needsRender = false;
    }

    /**
     * Launching this class as its own thread as well as starting the input thread
     */
    public void start() {
        new Thread(this).start();
        this.input.start();
    }
    
    /**
     * Sets the render flag to true causing a re-render of the screen
     */
    public void render() {
        needsRender = true;
    }

    /**
     * Main control loop looping through potential tasks
     * Rendering the screen if called upon 
     */
    public void run() {
        menu.drawMenu();
        
        while (Main.isRunning) {
            if (needsRender) {
                menu.drawMenu();
                needsRender = false;
            }
            
            /* 
             * Get the next task from the queue
             */
            Task task = UIController.queue.getNext();
            try {
                if (task != null) {
                	
                	/*
                	 * Determines the type of task
                	 * Cast to the correct task object and extracts the data
                	 * Throws appropriate exceptions where tasks are corrupt
                	 */
                    switch (task.getTaskType()) {
                        case ButtonPress:
                            ButtonType bt = ((TaskButtonPress) task).getData();
                            switch (bt) {
                                case Up:
                                case Down:
                                case Enter:
                                case Escape:
                                    this.menu.checkKeys(bt);
                                    break;
                                    
                                default:
                                    throw new IllegalButtonTypeExteption("Not a registered button: " + bt.toString());
                            }
                            break;
                            
                        default:
                            throw new IllegalTaskException("No implementation for task: " + task.getTaskType().toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
