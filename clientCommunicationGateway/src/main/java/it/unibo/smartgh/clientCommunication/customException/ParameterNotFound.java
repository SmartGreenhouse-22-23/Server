package it.unibo.smartgh.clientCommunication.customException;

/**
 * Custom exception to handle the situation in which the specified parameter doesn't exist.
 */
public class ParameterNotFound extends Exception{

    /**
     * Public constructor of the class.
     * @param message the message to send.
     */
    public ParameterNotFound(String message) {
        super(message);
    }
}
