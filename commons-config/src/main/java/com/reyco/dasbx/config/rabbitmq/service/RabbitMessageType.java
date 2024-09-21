package com.reyco.dasbx.config.rabbitmq.service;

public interface RabbitMessageType {
	
	public static final int RETRIES = 3;
	
	byte getType();
	
	String getRetryKey();
	
	default int getRetryTime() {
		return RETRIES;
	}
	
}
