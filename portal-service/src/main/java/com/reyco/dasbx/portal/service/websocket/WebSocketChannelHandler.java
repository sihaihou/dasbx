package com.reyco.dasbx.portal.service.websocket;

/**
 * 
 * @author reyco
 *
 */
public interface WebSocketChannelHandler {
	/**
	 * 获取数据
	 * @param channelHandlerParameter
	 * @return
	 */
	WebSocketMessage HandleWebSocketChannel(ChannelHandlerParameter channelHandlerParameter);
}
