package com.reyco.dasbx.id.core.exception;

public class IdGeneratorException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3462326253191170322L;
	
	public IdGeneratorException() {
		this("MachineId or workId out of bounds,should be between 0 and 31.");
	}
	public IdGeneratorException(String msg) {
		super(msg);
	}
}
