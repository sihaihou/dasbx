package com.reyco.dasbx.es.core.exception;

public class SearchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8219857243377546802L;

	public SearchException(String message) {
		super(message);
	}
	
	public SearchException(Throwable cause) {
        super(cause);
    }

    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }

}
