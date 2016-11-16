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

public class UIInput implements Runnable {

    private Keys keys;
    private boolean keyReleased = true;

    public UIInput() {
        this.keys = BrickFinder.getLocal().getKeys();
    }

    public void start() {
        new Thread(this).start();
    }

    public void run() {
        while (Main.isRunning) {
            if (this.keyReleased) {
                if (checkQuitKeys()) {
                    Main.queue.addTask(new TaskQuit(Priority.High));
                } else if (checkAdminKeys()) {
                    Main.queue.addTask(new TaskRefillStock(Priority.Low));
                } else {
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
                }
            } else if (this.keys.getButtons() == 0) {
                this.keyReleased = true;
            }
        }
    }

    private boolean checkAdminKeys() {
        return keys.getButtons() == (Keys.ID_RIGHT | Keys.ID_UP);
    }

    private boolean checkQuitKeys() {
        int keyVal = this.keys.getButtons();
        int idRight = Keys.ID_RIGHT;
        int idLeft = Keys.ID_LEFT;
        return ((keyVal & idRight) != 0 && (keyVal & idLeft) != 0);
    }
}
