package com.cultofcheese.uhc.entities.exceptions;

/**
 * This exception is thrown when an invalid Game Configuration is passed to the constructor of a <code>Game</code> object.
 */
public class InvalidConfigurationException extends RuntimeException {

    public InvalidConfigurationException() {

    }

    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
