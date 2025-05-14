package com.reyco.dasbx.sync.exception;

public class SyncException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8373199673031424029L;
	public SyncException() {
		super("Sync Exception");
	}
	public SyncException(String msg) {
		super(msg);
	}
	public SyncException(Exception ex) {
		super(ex);
	}
}
