package DAO;

/**
 * Exception thrown when any Miscellaneous SQL State exception occurs
 */
public class ApplicationErrorException extends Exception {
	public ApplicationErrorException(String message) {
		super(message);
	}
}
