package com.dm.exception;

/**
 * @author zy
 * @Description:
 */
public class InsertParserException extends RuntimeException {
	public InsertParserException() {
		super();
	}

	public InsertParserException(String message) {
		super(message);
	}

	public InsertParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public InsertParserException(Throwable cause) {
		super(cause);
	}

	protected InsertParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
