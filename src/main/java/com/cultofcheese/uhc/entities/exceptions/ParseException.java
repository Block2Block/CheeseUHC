package com.cultofcheese.uhc.entities.exceptions;

/**
 * This is thrown whenever parsing a game configuration detects a mismatch in the settings entered.
 */
public class ParseException extends RuntimeException {

    public ParseException() {

    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
