package main.parts.UI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

public class Menu {

    Item item1 = new Item("Melkerull", 20, SlotID.Bottom);
    Item item2 = new Item("Smil", 20, SlotID.Middle);
    Item item3 = new Item("Melkebart", 15, SlotID.Top);
    Item[] itemArray = new Item[]{item1, item2, item3};
    String[] confArray = new String[]{"Y", "N"};

    private GraphicsLCD graphics = LocalEV3.get().getGraphicsLCD();
    private final int screenWidth;
    private final int screenHeight;
    private int menuSelection = 0;
    private int confSelection = 0;
    private int menuLevel = 0;

    public Menu() {
        Stock.load(itemArray);
        screenWidth = graphics.getWidth();
        screenHeight = graphics.getHeight();
    }

    public void drawMenu() {
        graphics.clear();
        drawChangeSection();
        if (menuLevel == 0) {
            for (int nr = 0; nr < itemArray.length; nr++) {
                if (itemArray[nr].getStockSize() <= 0) {
                    continue;
                }
                
                if (nr == menuSelection) {
                    drawOptionRect(nr + 1, itemArray[nr], true);
                } else {
                    drawOptionRect(nr + 1, itemArray[nr], false);
                }
            }
        } else if (menuLevel == 1) {
            drawConfirmOption(itemArray[menuSelection]);
        }
    }

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

    private void drawConfButton(int xPos, int yPos, int width, int height, String confString, boolean selected) {
        drawRectBetter(xPos, yPos, width, height, selected);
        graphics.setFont(Font.getLargeFont());
        graphics.drawString(confString, xPos + (width / 2 - 10), yPos + (height / 2 - 10), 0, selected);
    }

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

    private void drawRectBetter(int x, int y, int width, int height, boolean fill) {
        if (!fill) {
            graphics.drawRect(x, y, width, height);
        } else {
            graphics.fillRect(x, y, width, height);
        }
    }

    public void checkKeys(ButtonType keyVal) {
        if (menuLevel == 0) {
            if (keyVal == ButtonType.Up) {
                updateMenuSelection(-1);
            } else if (keyVal == ButtonType.Down) {
                updateMenuSelection(1);
            } else if (keyVal == ButtonType.Enter) {
                if(Main.WALLET >= itemArray[menuSelection].getPrice()
                        && itemArray[menuSelection].getStockSize() > 0)
                    updateMenuLevel(1);
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

    public void purchaseItem() {
        Item selected = itemArray[menuSelection];
        selected.reduceStockSize();
        SlaveController.queue.addTask(new TaskDispenseSlot(Priority.Medium, selected.getSlotId()));
        Main.WALLET -= selected.getPrice();
        Stock.save(itemArray);
    }

    private void updateMenuSelection(int change) {
        menuSelection += change;
        if(menuSelection == itemArray.length){
        	menuSelection = 0;
        }
        else if(menuSelection < 0){
        	menuSelection = itemArray.length - 1;
        }
        drawMenu();
    }

    private void updateConfirmationSelection() {
        if (confSelection == 0) {
            confSelection++;
        } else {
            confSelection--;
        }
        drawMenu();
    }

    private void updateMenuLevel(int change) {
        menuLevel += change;
        drawMenu();
    }
}
