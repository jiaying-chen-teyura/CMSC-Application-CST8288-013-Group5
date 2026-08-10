package businesslayer;

/**
 * Represents an error caused by invalid business-layer input
 * or an invalid business operation.
 */
public class ValidationException extends Exception {

    /**
     * Creates a ValidationException with the specified error message.
     *
     * @param message the message describing the validation error
     */
    public ValidationException(String message) {
        super(message);
    }
}