package org.epam.devbistro.utils;


// Custom exception for invalid test data
public class InvalidTestDataException extends RuntimeException {

    public InvalidTestDataException(String message) {
        super(message);
    }

    public InvalidTestDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
