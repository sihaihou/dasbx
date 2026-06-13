package com.reyco.dasbx.es.support.annotation.execptio;

import com.reyco.dasbx.es.core.exception.SearchException;

public class SearchAnnotationException extends SearchException {

	public SearchAnnotationException(String message) {
		super(message);
	}
	
	public SearchAnnotationException(Throwable cause) {
        super(cause);
    }

    public SearchAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

}
