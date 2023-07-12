package org.example.DAO;

/**
 * Exception thrown when a unique key is repeated in any entity. A Unique Constraint violation in SQL table.
 */
public class UniqueConstraintException extends Exception {
	public UniqueConstraintException(String message) {
		super(message);
	}
}
