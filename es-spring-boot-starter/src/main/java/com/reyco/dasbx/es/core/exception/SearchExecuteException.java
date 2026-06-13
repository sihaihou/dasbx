package com.reyco.dasbx.es.core.exception;

public class SearchExecuteException extends SearchException {

	public SearchExecuteException(String message) {
		super(message);
	}
	public SearchExecuteException(Throwable cause) {
        super(cause);
    }

    public SearchExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}
