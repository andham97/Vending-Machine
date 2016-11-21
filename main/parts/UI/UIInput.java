package main.parts.UI;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import main.Main;
import main.enums.ButtonType;
import main.enums.Priority;
import main.parts.UIController;
import main.util.task.TaskButtonPress;
import main.util.task.TaskQuit;
import main.util.task.TaskRefillStock;

/**
 * A stand alone thread handling the user interaction with the EV3
 * Notifying the different parts of the system based on the interaction
 * 
 * @author andreashammer
 */
public class UIInput implements Runnable {

	/**
	 * Reference to the Keys object inorder to read the pressed keys
	 */
    private Keys keys;
    
    /**
     * A flag used to regulate the number of tasks queued in the main thread loop
     */
    private boolean keyReleased = true;

    /**
     * Fetching the Keys object reference from the brick
     */
    public UIInput() {
        this.keys = BrickFinder.getLocal().getKeys();
    }

    /**
     * Setting up this class' thread
     */
    public void start() {
        new Thread(this).start();
    }

    /**
     * The main loop for this thread
     */
    public void run() {
        boolean quitKeysLast = false;
        boolean adminKeysLast = false;
        boolean quitKeys, adminKeys;
        while (Main.isRunning) {
            
        	/* 
        	 * Check for admin or quit key combo
        	 */
            quitKeys = checkQuitKeys();
            adminKeys = checkAdminKeys();
            
            if (quitKeys && !quitKeysLast) {
                Main.queue.addTask(new TaskQuit(Priority.High));
            } else if (adminKeys && !adminKeysLast) {
                Main.queue.addTask(new TaskRefillStock(Priority.Low));
            }
            quitKeysLast = quitKeys;
            adminKeysLast = adminKeys;
            
            /*
             * Queue task in uicontroller based on button pressed
             */
            if (this.keyReleased && !quitKeys && !adminKeys) {
                int keyVal = this.keys.getButtons();
                if ((Keys.ID_UP & keyVal) != 0) {
                    this.keyReleased = false;
                    UIController.queue.addTask(new TaskButtonPress(Priority.Medium, ButtonType.Up));
                }
                if ((Keys.ID_DOWN & keyVal) != 0) {
                    this.keyReleased = false;
                    UIController.queue.addTask(new TaskButtonPress(Priority.Medium, ButtonType.Down));
                }
                if ((Keys.ID_ESCAPE & keyVal) != 0) {
                    this.keyReleased = false;
                    UIController.queue.addTask(new TaskButtonPress(Priority.Medium, ButtonType.Escape));
                }
                if ((Keys.ID_ENTER & keyVal) != 0) {
                    this.keyReleased = false;
                    UIController.queue.addTask(new TaskButtonPress(Priority.Medium, ButtonType.Enter));
                }
            } else if (this.keys.getButtons() == 0) {
                this.keyReleased = true;
            }
        }
    }

    /**
     * @return If the admin key combo is pressed
     */
    private boolean checkAdminKeys() {
        return keys.getButtons() == (Keys.ID_RIGHT | Keys.ID_UP);
    }

    /**
     * @return If the quit key combi is pressed
     */
    private boolean checkQuitKeys() {
        int keyVal = this.keys.getButtons();
        int idRight = Keys.ID_RIGHT;
        int idLeft = Keys.ID_DOWN;
        return ((keyVal & idRight) != 0 && (keyVal & idLeft) != 0);
    }
}
