package com.reyco.dasbx.portal.service.websocket;

public interface ChannelHandlerParameter {
	
	Long getChannelId();
	
 	default String getWebSocketChannelhandlerMessage() {
 		return "";
 	}
}
