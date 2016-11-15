package main.parts;

import main.Main;
import main.enums.ButtonType;
import main.enums.Priority;
import main.enums.exceptions.IllegalButtonTypeExteption;
import main.parts.UI.Menu;
import main.parts.UI.UIInput;
import main.util.Task;
import main.util.TaskQueue;
import main.util.task.TaskButtonPress;
import main.util.task.TaskQuit;
import main.util.task.exceptions.IllegalTaskException;

public class UIController implements Runnable {

    public static TaskQueue queue;

    private UIInput input;
    private Menu menu;
    
    private boolean needsRender = false;

    public UIController() {
        UIController.queue = new TaskQueue();
        this.menu = new Menu();
        this.input = new UIInput();
    }

    public void start() {
        new Thread(this).start();
        this.input.start();
    }
    
    public void render() {
        needsRender = true;
    }

    public void run() {
        menu.drawMenu();
        
        while (Main.isRunning) {
            if (needsRender) {
                menu.drawMenu();
                needsRender = false;
            }
            
            Task task = UIController.queue.getNext();
            try {
                if (task != null) {
                    switch (task.getTaskType()) {
                        case ButtonPress:
                            ButtonType bt = ((TaskButtonPress) task).getData();
                            switch (bt) {
                                case Quit:
                                    Main.queue.addTask(new TaskQuit(Priority.High));
                                    break;
                                    
                                case Up:
                                case Down:
                                case Enter:
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
