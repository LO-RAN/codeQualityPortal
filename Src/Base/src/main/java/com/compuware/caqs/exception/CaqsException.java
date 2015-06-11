package com.compuware.caqs.exception;

/**
 * @author cwfr-fdubois
 *
 */
public class CaqsException extends Exception {

    /** The serial version UID. */
	private static final long serialVersionUID = 5990406930252931263L;

    /** The message key for internationlization. */
	private String msgKey;

	/**
	 * Default constructor.
	 */
	public CaqsException() {
		super();
	}

    /**
     * Create a new exception with the given message.
     * @param message the exception message.
     */
	public CaqsException(String message) {
		super(message);
	}

    /**
     * Create a new exception with the given message key and default message.
     * @param msgKey the message key.
     * @param defaultMessage the default message.
     */
	public CaqsException(String msgKey, String defaultMessage) {
		super(defaultMessage);
		this.msgKey = msgKey;
	}

    /**
     * Create a new exception with the given message key, default message and incoming exception.
     * @param msgKey the message key.
     * @param defaultMessage the default message.
     * @param e the incomming exception.
     */
	public CaqsException(String msgKey, String defaultMessage, Throwable e) {
		super(defaultMessage, e);
		this.msgKey = msgKey;
    }

    /**
     * Create a new exception with the given message and incoming exception.
     * @param message the exception message.
     * @param cause the incomming exception.
     */
	public CaqsException(String message, Throwable cause) {
		super(message, cause);
	}

    /**
     * Create a new exception with the given incoming exception.
     * @param cause the incomming exception.
     */
	public CaqsException(Throwable cause) {
		super(cause);
	}

    /**
     * Get the message key for internationalization.
     * @return the message key.
     */
	public String getMsgKey() {
		return this.msgKey;
	}

}
