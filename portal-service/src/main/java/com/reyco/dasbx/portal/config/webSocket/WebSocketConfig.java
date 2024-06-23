package com.reyco.dasbx.portal.config.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.reyco.dasbx.portal.service.websocket.DanmakuWebSocketChannelHandker;
import com.reyco.dasbx.portal.service.websocket.WebSocketServer;


@Configuration
public class WebSocketConfig {

	@Bean
	public ServerEndpointExporter getServerEndpointExporter() {
		return new ServerEndpointExporter();
	}

	@Autowired
	public void setSenderService(DanmakuWebSocketChannelHandker danmakuWebSocketChannelHandker) {
		WebSocketServer.danmakuWebSocketChannelHandker = danmakuWebSocketChannelHandker;
	}
}