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
import main.util.task.TaskDispenseSlot;
import main.util.task.TaskRefundMoney;
import main.util.task.TaskStoreMoney;

/**
 * Tracks the user interaction with the menu system and responds accordingly
 * Render all GUI elements
 * 
 * @author fredh
 */
public class Menu {
	
	/**
	 * A reference to the current stock
	 */
	private ArrayList<Item> items;
	
	/**
	 * Array containing the confirmation characters for the confirmation menu
	 */
	private String[] confArray = new String[] { "Y", "N" };

	/**
	 * Reference to the graphics context of the EV3 screen
	 */
	private GraphicsLCD graphics;
	
	/**
	 * Store the screen width for easy access
	 */
	private final int screenWidth;
	
	/**
	 * Store the screen height for easy access
	 */
	private final int screenHeight;
	
	/**
	 * Represent where what item is selected in the main menu
	 */
	private int menuSelection = 0;
	
	/**
	 * Represent the state of the confirmation menu
	 */
	private int confSelection = 0;
	
	/**
	 * Represent the current menu level
	 * 
	 * 0 - Main menu
	 * 1 - Confirmation menu
	 */
	private int menuLevel = 0;

	/**
	 * Constructor for the menu class, initializer.
	 */
	public Menu() {
		items = Stock.get();
		screenWidth = graphics.getWidth();
		screenHeight = graphics.getHeight();
		graphics = LocalEV3.get().getGraphicsLCD();
	}

	/**
	 * Draw the GUI based on menu level and the selected item
	 */
	public void drawMenu() {
		graphics.clear();

		/*
		 * Draw the top section of the display (wallet and change button)
		 */
		drawChangeSection();

		synchronized (Stock.lock) {
			/*
			 * Draw the menu items
			 */
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
			}

			/*
			 * Draw the selected items details and confirmation screen
			 */
			else if (menuLevel == 1) {
				drawConfirmOption(items.get(menuSelection));
			}
		}
	}

	/**
	 * Process the key value and update the menu level and/or selection based on
	 * the user input
	 * 
	 * @param keyVal ButtonType representing the key pressed
	 */
	public void checkKeys(ButtonType keyVal) {
		/*
		 * Navigate the main menu and select the item to purchase
		 */
		if (menuLevel == 0 && items.size() > 0) {
			if (keyVal == ButtonType.Up) {
				updateMenuSelection(-1);
			} else if (keyVal == ButtonType.Down) {
				updateMenuSelection(1);
			} else if (keyVal == ButtonType.Enter) {
				synchronized (Stock.lock) {
					if (Main.WALLET >= items.get(menuSelection).getPrice()
							&& items.get(menuSelection).getStockSize() > 0) {
						updateMenuLevel(1);
					}
				}
			} else if (keyVal == ButtonType.Escape && Main.WALLET > 0) {
				SlaveController.queue.addTask(new TaskRefundMoney(Priority.High));
				Main.WALLET = 0;
				drawMenu();
			}
		}

		/*
		 * Confirm the purchase Navigate the confirmation screen Or exit back to
		 * the main menu
		 */
		else if (menuLevel == 1) {
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
	 * Purchases the currently selected item Notify the different parts of the
	 * system based on the purchased item
	 */
	public void purchaseItem() {
		Item selected;
		synchronized (Stock.lock) {
			selected = items.get(menuSelection);
			selected.reduceStockSize();
			if (selected.getStockSize() <= 0) {
				items.remove(menuSelection);
			}
		}
		SlaveController.queue.addTask(new TaskDispenseSlot(Priority.Medium, selected.getSlotId()));
		SlaveController.queue.addTask(new TaskStoreMoney(Priority.High));
		Main.WALLET -= selected.getPrice();
		Stock.save();
	}

	/**
	 * Notifies the user when all items are out of stock
	 */
	private void drawOutOfStockNotice() {
		int height = screenHeight / 2;
		graphics.setFont(Font.getLargeFont());
		graphics.drawString("Tomt! \\m/", 0, height, 0);
	}

	/**
	 * Draws the top section that shows the user's money, and the return button
	 */
	private void drawChangeSection() {
		int posX = screenWidth - 80;
		int width = screenWidth - posX;
		int height = screenHeight / 4;

		/*
		 * Draw the wallet
		 */
		graphics.drawRect(posX, 0, width, height);
		graphics.setFont(Font.getLargeFont());
		graphics.drawString(Main.WALLET + "kr", posX, 2, 0);

		int lineXAngle1 = 55;
		int lineXAngle2 = 70;

		/*
		 * Draw the change button
		 */
		graphics.drawLine(0, 0, 0, height);
		graphics.drawLine(0, height, lineXAngle1, height);
		graphics.drawLine(lineXAngle1, height, lineXAngle2, height / 2);
		graphics.drawLine(lineXAngle2, height / 2, lineXAngle2, 0);
		graphics.drawLine(lineXAngle2, 0, 0, 0);
		graphics.setFont(Font.getDefaultFont());
		graphics.drawString(" Retur", 0, height / 4, 0);
	}

	/**
	 * Draws the confirmation options in the second level of the menu
	 * 
	 * @param item The item to get item information from
	 */
	private void drawConfirmOption(Item item) {
		String text = item.getName();
		int price = item.getPrice();
		int height = screenHeight / 4;
		int width1 = screenWidth - 75;
		int width2 = screenWidth - width1 - 35;

		/*
		 * Draw the item details
		 */
		drawRectBetter(0, height * 3, width1, height, false);
		drawRectBetter(width1, height * 3, width2, height, false);

		graphics.setFont(Font.getDefaultFont());
		graphics.drawString(text, 10, (height * 3) + (height / 4), 0);
		graphics.drawString(price + "kr", width1 + 1, (height * 3) + (height / 4), 0);

		/*
		 * Draw the canvas for a logo Logo not implemented, no time
		 */
		drawRectBetter(0, height, width1 + width2, height * 2, true);

		int confButtonHeight = (screenHeight - (screenHeight / 4)) / 2;
		int confButtonWidth = screenWidth - (width1 + width2);

		/*
		 * Draw the confirmation menu
		 */
		for (int nr = 0; nr < 2; nr++) {
			if (nr == confSelection) {
				drawConfButton(width1 + width2, (confButtonHeight * nr + 1) + height, confButtonWidth, confButtonHeight,
						confArray[nr], true);
			} else {
				drawConfButton(width1 + width2, (confButtonHeight * nr + 1) + height, confButtonWidth, confButtonHeight,
						confArray[nr], false);
			}
		}
	}

	/**
	 * Draws the confirmation buttons for purchase
	 * 
	 * @param xPos X position of the button
	 * @param yPos Y position of the button
	 * @param width The button width
	 * @param height The button height
	 * @param confString The button string, from confArray
	 * @param selected Representing if the button is selected
	 */
	private void drawConfButton(int xPos, int yPos, int width, int height, String confString, boolean selected) {
		drawRectBetter(xPos, yPos, width, height, selected);
		graphics.setFont(Font.getLargeFont());
		graphics.drawString(confString, xPos + (width / 2 - 10), yPos + (height / 2 - 10), 0, selected);
	}

	/**
	 * Draws the item options
	 * 
	 * @param y The list position for the item
	 * @param item The item that the info will be fetched from
	 * @param selected Representing if the item is selected
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
	 * 
	 * @param x The x start position of the draw call
	 * @param y The y start position of the draw call
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 * @param fill Flags if the rectangle should be filled
	 */
	private void drawRectBetter(int x, int y, int width, int height, boolean fill) {
		if (!fill) {
			graphics.drawRect(x, y, width, height);
		} else {
			graphics.fillRect(x, y, width, height);
		}
	}

	/**
	 * Updates the currently selected item
	 * 
	 * @param change -1 to move up, 1 to move down
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
	 * 
	 * @param change -1 to go back to main menu, 1 to go into item buying screen
	 */
	private void updateMenuLevel(int change) {
		menuLevel += change;
		drawMenu();
	}
}
