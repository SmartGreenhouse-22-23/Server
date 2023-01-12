package it.unibo.smartgh.customException;

/**
 * Custom exception that refers to the case in which a specific database is empty.
 */
public class EmptyDatabaseException extends Exception{

    /**
     * Public constructor of the class.
     * @param message the message we want to show.
     */
    public EmptyDatabaseException(String message) {
        super(message);
    }
}
