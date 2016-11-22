package main.util.task.exceptions;

/**
 * Exception used when a Task class type is not a valid button
 * 
 * @author andreashammer
 */

@SuppressWarnings("serial")
public class IllegalTaskException extends Exception {
	
	/**
	 * @param string The exception message
	 */
	public IllegalTaskException(String string){
		super(string);
	}
}
