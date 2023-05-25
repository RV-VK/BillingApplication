package Service;

/**
 * Custom Exception thrown when user enters inputs that do not match the template.
 */
public class InvalidTemplateException extends Exception {
	public InvalidTemplateException(String message) {
		super(message);
	}
}
