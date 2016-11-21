package main.enums.exceptions;

/**
 * Exception used when a TaskButtonPress data is not a valid button
 * 
 * @author andreashammer
 */
@SuppressWarnings("serial")
public class IllegalButtonTypeExteption extends Exception {

	/**
	 * @param string The exception message
	 */
	public IllegalButtonTypeExteption(String string) {
		super(string);
	}
}
