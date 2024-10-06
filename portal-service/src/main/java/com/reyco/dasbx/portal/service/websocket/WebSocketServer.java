package com.reyco.dasbx.portal.service.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.reyco.dasbx.commons.utils.convert.JsonUtils;

@Component
@ServerEndpoint("/websocket/{channelId}")
public class WebSocketServer {

	private static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

	private static LongAdder onlineCount = new LongAdder();

	private static Set<WebSocketServer> webSocketSet = Collections.synchronizedSet(new HashSet<WebSocketServer>());
	
	public static DanmakuWebSocketChannelHandker danmakuWebSocketChannelHandker;
	
	private Session session;
	private Long channelId;

	/**
	 * 连接建立成功调用的方法
	 * 
	 * @param session
	 * @param sid
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("channelId") Long channelId) {
		try {
			this.session = session;
			this.channelId = channelId;
			webSocketSet.add(this);
			addOnlineCount();
			logger.debug(channelId + "号频道开始监听,当前在线人数为" + getOnlineCount());
			WebSocketMessage webSocketMessage = danmakuWebSocketChannelHandker.HandleWebSocketChannel(()->channelId);
			sendMessage(webSocketMessage);
		} catch (IOException e) {
			logger.error("websocket IO异常");
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		webSocketSet.remove(this); // 从set中删除
		subOnlineCount(); // 在线数减1
		logger.debug(channelId + "号频道有一连接关闭！当前在线人数为" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message
	 *            客户端发送过来的消息
	 * @param session
	 * @throws IOException
	 */
	@OnMessage
	public void onMessage(String message,Session session) throws IOException {
		logger.debug("收到来自" + channelId + "号频道的推送信息:" + message);
		// 群发消息
		WebSocketMessage webSocketMessage = JsonUtils.jsonToObj(message, WebSocketMessage.class);
		for (WebSocketServer item : webSocketSet) {
			if (item.channelId.equals(this.channelId)) {
				if(webSocketMessage.getBeat()) {
					if(item.session.getId().equalsIgnoreCase(session.getId())) {
						item.sendMessage(webSocketMessage);
					}
				}else {
					item.sendMessage(webSocketMessage);
				}
				
			}
		}
	}

	/**
	 * 发生错误
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		logger.error("发生错误");
		error.printStackTrace();
	}

	/**
	 * 群发自定义消息
	 * 
	 * @param message
	 * @param sid
	 * @throws IOException
	 */
	public static void sendInfo(Long channelId,@PathParam("message")final String message) throws IOException {
		logger.debug("推送消息到" + channelId + "号频道，推送内容:" + message);
		ChannelHandlerParameter channelHandlerParameter = new ChannelHandlerParameter() {
			@Override
			public Long getChannelId() {
				return channelId;
			}
			@Override
			public String getWebSocketChannelhandlerMessage() {
				return message;
			}
		};
		WebSocketMessage webSocketMessage = danmakuWebSocketChannelHandker.HandleWebSocketChannel(channelHandlerParameter);
		for (WebSocketServer item : webSocketSet) {
			try {
				if (channelId == null) {
					item.sendMessage(webSocketMessage);
					continue;
				}
				if (item.channelId.equals(channelId)) {
					item.sendMessage(webSocketMessage);
				}
			} catch (IOException e) {
				continue;
			}
		}
	}
	public void sendMessage(WebSocketMessage webSocketMessage) throws IOException {
		sendMessage(JsonUtils.objToJson(webSocketMessage));
	}
	/**
	 * 实现服务器主动推送
	 */
	public void sendMessage(String message) throws IOException {
		this.session.getAsyncRemote()/*.getBasicRemote()*/.sendText(message);
	}
	
	public static int getOnlineCount() {
		return onlineCount.intValue();
	}

	public static void addOnlineCount() {
		onlineCount.increment();
	}

	public static void subOnlineCount() {
		onlineCount.decrement();
	}
}