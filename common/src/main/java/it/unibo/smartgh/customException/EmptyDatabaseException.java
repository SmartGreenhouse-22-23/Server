package it.unibo.smartgh.customException;

public class EmptyDatabaseException extends Exception{

    public EmptyDatabaseException(String message) {
        super(message);
    }
}
