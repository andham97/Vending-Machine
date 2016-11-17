package main.parts.UI;

import java.util.ArrayList;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import main.Main;
import main.SlaveController;
import main.Stock;
import main.enums.ButtonType;
import main.enums.Priority;
import main.enums.SlotID;
import main.util.task.TaskDispenseSlot;
import main.util.task.TaskRefundMoney;
import main.util.task.TaskStoreMoney;

public class Menu {

    Item item1 = new Item("Melkerull", 20, SlotID.Bottom);
    Item item2 = new Item("Smil", 20, SlotID.Middle);
    Item item3 = new Item("Melkebart", 15, SlotID.Top);
    Item[] itemArray = new Item[]{item1, item2, item3};
    ArrayList<Item> items;
    String[] confArray = new String[]{"Y", "N"};

    private GraphicsLCD graphics = LocalEV3.get().getGraphicsLCD();
    private final int screenWidth;
    private final int screenHeight;
    private int menuSelection = 0;
    private int confSelection = 0;
    private int menuLevel = 0;
    /** 
     * Constructor for the menu class, initializer.
     */
    public Menu() {
        items = Stock.get();
        screenWidth = graphics.getWidth();
        screenHeight = graphics.getHeight();
    }
    /**
     * main drawing method, calls the other subdrawing methods when needed
     */
    public void drawMenu() {
        graphics.clear();
        drawChangeSection();
        if (menuLevel == 0) {
            int nr = 0;
            if (items.size() > 0) {
                for (Item item : items) {
                    if (nr == menuSelection) {
                        drawOptionRect(nr + 1, item, true);
                    } else {
                        drawOptionRect(nr + 1, item, false);
                    }
                    nr++;
                }
            } else {
                drawOutOfStockNotice();
            }
        } else if (menuLevel == 1) {
            drawConfirmOption(items.get(menuSelection));
        }
    }
    /**
     * Notifies the user when all items are out of stock
     */
    private void drawOutOfStockNotice() {
        int height = screenHeight / 2;
        graphics.setFont(Font.getLargeFont());
        graphics.drawString("Out of stock!", 0, height, 0);
    }
    /**
     * Draws the top section that shows the user's money, and the return button
     */
    private void drawChangeSection() {
        int posX = screenWidth - 80;
        int width = screenWidth - posX;
        int height = screenHeight / 4;
        graphics.drawRect(posX, 0, width, height);
        graphics.setFont(Font.getLargeFont());
        graphics.drawString(Main.WALLET + "kr", posX, 0, 0);
        int lineXAngle1 = 55;
        int lineXAngle2 = 70;
        graphics.drawLine(0, 0, 0, height);
        graphics.drawLine(0, height, lineXAngle1, height);
        graphics.drawLine(lineXAngle1, height, lineXAngle2, height / 2);
        graphics.drawLine(lineXAngle2, height / 2, lineXAngle2, 0);
        graphics.drawLine(lineXAngle2, 0, 0, 0);
        graphics.setFont(Font.getDefaultFont());
        graphics.drawString("Veksel", 0, height / 4, 0);
    }
    /**
     * Draws the confirmation options in the second level of the menu
     * @param item 
     */
    private void drawConfirmOption(Item item) {
        String text = item.getName();
        int price = item.getPrice();
        int height = screenHeight / 4;
        int width1 = screenWidth - 75;
        int width2 = screenWidth - width1 - 35;
        drawRectBetter(0, height * 3, width1, height, false);
        drawRectBetter(width1, height * 3, width2, height, false);
        graphics.setFont(Font.getDefaultFont());
        graphics.drawString(text, 10, (height * 3) + (height / 4), 0);
        graphics.drawString(price + "kr", width1 + 1, (height * 3) + (height / 4), 0);

        drawRectBetter(0, height, width1 + width2, height * 2, true);

        int confButtonHeight = (screenHeight - (screenHeight / 4)) / 2;
        int confButtonWidth = screenWidth - (width1 + width2);

        for (int nr = 0; nr < 2; nr++) {
            if (nr == confSelection) {
                drawConfButton(width1 + width2, (confButtonHeight * nr + 1) + height, confButtonWidth, confButtonHeight, confArray[nr], true);
            } else {
                drawConfButton(width1 + width2, (confButtonHeight * nr + 1) + height, confButtonWidth, confButtonHeight, confArray[nr], false);
            }
        }
    }
    /**
     * Draws the confirmation buttons for purchase
     * @param xPos
     * @param yPos
     * @param width
     * @param height
     * @param confString
     * @param selected 
     */
    private void drawConfButton(int xPos, int yPos, int width, int height, String confString, boolean selected) {
        drawRectBetter(xPos, yPos, width, height, selected);
        graphics.setFont(Font.getLargeFont());
        graphics.drawString(confString, xPos + (width / 2 - 10), yPos + (height / 2 - 10), 0, selected);
    }
    /**
     * Draws the item options
     * @param y
     * @param item
     * @param selected 
     */
    private void drawOptionRect(int y, Item item, boolean selected) {
        String text = item.getName();
        int price = item.getPrice();
        int height = screenHeight / 4;
        int width1 = screenWidth - 45;
        int width2 = screenWidth - width1;
        drawRectBetter(0, height * y, width1, height, selected);
        drawRectBetter(width1, height * y, width2, height, selected);
        graphics.setFont(Font.getDefaultFont());
        graphics.drawString(text, 35, (height * y) + (height / 4), 0, selected);
        graphics.drawString(price + "kr", width1 + 1, (height * y) + (height / 4), 0, selected);
    }
    /**
     * Allows one simple method call for both filled and unfilled rectangles
     * @param x
     * @param y
     * @param width
     * @param height
     * @param fill 
     */
    private void drawRectBetter(int x, int y, int width, int height, boolean fill) {
        if (!fill) {
            graphics.drawRect(x, y, width, height);
        } else {
            graphics.fillRect(x, y, width, height);
        }
    }
    /**
     * Checks keyinputs and acts accordingly
     * @param keyVal 
     */
    public void checkKeys(ButtonType keyVal) {
        if (menuLevel == 0 && items.size() > 0) {
            if (keyVal == ButtonType.Up) {
                updateMenuSelection(-1);
            } else if (keyVal == ButtonType.Down) {
                updateMenuSelection(1);
            } else if (keyVal == ButtonType.Enter) {
                if (Main.WALLET >= items.get(menuSelection).getPrice()
                        && items.get(menuSelection).getStockSize() > 0) {
                    updateMenuLevel(1);
                }
            } else if (keyVal == ButtonType.Escape) {
                SlaveController.queue.addTask(new TaskRefundMoney(Priority.High));
                Main.WALLET = 0;
                drawMenu();
            }
        } else if (menuLevel == 1) {
            if (keyVal == ButtonType.Up || keyVal == ButtonType.Down) {
                updateConfirmationSelection();
            } else if (keyVal == ButtonType.Enter) {
                if (confSelection == 0) {
                    purchaseItem();
                }
                updateMenuLevel(-1);
            }
        }
    }
    /**
     * Purchases the currently selected item
     */
    public void purchaseItem() {
        Item selected = items.get(menuSelection);
        selected.reduceStockSize();
        if (selected.getStockSize() <= 0) {
            items.remove(menuSelection);
        }
        SlaveController.queue.addTask(new TaskDispenseSlot(Priority.Medium, selected.getSlotId()));
        SlaveController.queue.addTask(new TaskStoreMoney(Priority.High));
        Main.WALLET -= selected.getPrice();
        Stock.save();
    }
    /**
     * Updates the currently selected item
     * @param change 
     */
    private void updateMenuSelection(int change) {
        menuSelection += change;
        if (menuSelection >= items.size()) {
            menuSelection = 0;
        } else if (menuSelection < 0) {
            menuSelection = items.size() - 1;
        }
        drawMenu();
    }
    /**
     * Updates the currently selected confirmation option
     */
    private void updateConfirmationSelection() {
        if (confSelection == 0) {
            confSelection++;
        } else {
            confSelection--;
        }
        drawMenu();
    }
    /**
     * Updates the current menu level
     * @param change 
     */
    private void updateMenuLevel(int change) {
        menuLevel += change;
        drawMenu();
    }
}
