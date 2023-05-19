package DAO;

/**
 * Exception thrown when a non-existing unitcode is prompted. A foreign key Violation of the Unit table.
 */
public class UnitCodeViolationException extends Exception {
  public UnitCodeViolationException(String message) {
    super(message);
  }
}
