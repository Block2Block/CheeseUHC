package com.cultofcheese.uhc.entities.exceptions;

/**
 * This is thrown whenever parsing a game configuration detects a mismatch in the settings entered.
 */
public class ConfigurationMismatchException extends RuntimeException {

    public ConfigurationMismatchException() {

    }

    public ConfigurationMismatchException(String message) {
        super(message);
    }

    public ConfigurationMismatchException(Throwable cause) {
        super(cause);
    }

    public ConfigurationMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

}
