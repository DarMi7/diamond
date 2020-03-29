package com.dm.exception;

/**
 * @author zy
 * unexpected exception
 */
public class UnexpectedException extends RuntimeException {

    /**
     * Instantiates a new Should never happen exception.
     */
    public UnexpectedException() {
        super();
    }

    /**
     * Instantiates a new Should never happen exception.
     *
     * @param message the message
     */
    public UnexpectedException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Should never happen exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Should never happen exception.
     *
     * @param cause the cause
     */
    public UnexpectedException(Throwable cause) {
        super(cause);
    }
}
