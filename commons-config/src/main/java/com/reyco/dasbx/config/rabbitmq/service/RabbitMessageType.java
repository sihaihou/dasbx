package com.reyco.dasbx.config.rabbitmq.service;

public interface RabbitMessageType {
	
	byte getType();
	
	String getRetryKey();
	
	default int getRetryTime() {
		return 3;
	}
	
}
