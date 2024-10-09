package com.reyco.dasbx.es.core.exception;

public class ElasticsearchException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2111104920042121023L;
	public ElasticsearchException() {
		super("Elasticsearch Exception");
	}
	public ElasticsearchException(String msg) {
		super(msg);
	}
	public ElasticsearchException(Exception ex) {
		super(ex);
	}
}
