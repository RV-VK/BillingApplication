package Service;

/**
 * Exception thrown when in a Sales transaction a Non-dividable entity is prompted a decimal quantity.
 */
public class UnDividableEntityException extends Exception{
    public UnDividableEntityException(String message)
    {
        super(message);
    }
}
