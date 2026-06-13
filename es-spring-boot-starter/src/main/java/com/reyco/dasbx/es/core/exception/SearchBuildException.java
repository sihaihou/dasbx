package com.reyco.dasbx.es.core.exception;

public class SearchBuildException extends SearchException {

	public SearchBuildException(String message) {
		super(message);
	}
	public SearchBuildException(Throwable cause) {
        super(cause);
    }

    public SearchBuildException(String message, Throwable cause) {
        super(message, cause);
    }
}
