package com.reyco.dasbx.es.core.exception;

public class SearchMappingException extends SearchException{

	public SearchMappingException(String message) {
		super(message);
	}
	public SearchMappingException(Throwable cause) {
        super(cause);
    }

    public SearchMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
