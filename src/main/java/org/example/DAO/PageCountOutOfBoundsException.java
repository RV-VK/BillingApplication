package org.example.DAO;

/**
 * Exception thrown in a pageable list function if a non-existing page is prompted.
 */
public class PageCountOutOfBoundsException extends Exception {
	public PageCountOutOfBoundsException(String message) {
		super(message);
	}
}
