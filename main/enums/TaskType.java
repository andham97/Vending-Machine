package main.enums;

/**
 * Enum used to know what type of object the Task object represented
 * so we could cast to the correct object type
 * 
 * @author andreashammer
 */
public enum TaskType {
	MoneyAdded,
	ButtonPress,
	Quit,
	Dispense,
    RefillStock,
    StoreMoney,
    RefundMoney
}
