package org.example.validation;

/**
 * @author Zero
 *         Created on 2017/1/24.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
