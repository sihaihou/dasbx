package com.reyco.dasbx.portal.service.websocket;

/**
 * @author reyco
 * @date 2021.12.02
 * @version v1.0.1
 */
public class WebSocketMessage {

	public final static String REQUEST_SUCCESS = "Request succeeded";
	public final static String REQUEST_FAIL = "Request was aborted";
	/**
	 * 成功或失败
	 */
	private Boolean success;
	/**
	 * 数据
	 */
	private Object data;
	/**
	 * 异常信息
	 */
	private String message;
	/**
	 * 心跳
	 */
	private Boolean beat;

	/**
	 * 
	 */
	private WebSocketMessage() {
		
	}

	public static WebSocketMessage success() {
		return success(new Object());
	}

	public static WebSocketMessage success(Object data) {
		return success(data, REQUEST_SUCCESS);
	}

	public static WebSocketMessage success(Object data, String message) {
		WebSocketMessage webSocketMessage = new WebSocketMessage();
		webSocketMessage.setSuccess(true);
		webSocketMessage.setData(data);
		webSocketMessage.setBeat(false);
		webSocketMessage.setMessage(message);
		return webSocketMessage;
	}

	public static WebSocketMessage fail() {
		return fail(REQUEST_FAIL);
	}

	public static WebSocketMessage fail(String message) {
		WebSocketMessage webSocketMessage = new WebSocketMessage();
		webSocketMessage.setSuccess(false);
		webSocketMessage.setData(null);
		webSocketMessage.setBeat(false);
		webSocketMessage.setMessage(message);
		return webSocketMessage;
	}

	public static WebSocketMessage successBeat() {
		return successBeat(new Object());
	}

	public static WebSocketMessage successBeat(Object data) {
		return successBeat(data, REQUEST_SUCCESS);
	}

	public static WebSocketMessage successBeat(Object data, String message) {
		WebSocketMessage webSocketMessage = new WebSocketMessage();
		webSocketMessage.setSuccess(true);
		webSocketMessage.setData(data);
		webSocketMessage.setBeat(true);
		webSocketMessage.setMessage(message);
		return webSocketMessage;
	}

	public static WebSocketMessage failBeat() {
		return failBeat(REQUEST_FAIL);
	}

	public static WebSocketMessage failBeat(String message) {
		WebSocketMessage webSocketMessage = new WebSocketMessage();
		webSocketMessage.setSuccess(false);
		webSocketMessage.setData(null);
		webSocketMessage.setBeat(true);
		webSocketMessage.setMessage(message);
		return webSocketMessage;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getBeat() {
		return beat;
	}

	public void setBeat(Boolean beat) {
		this.beat = beat;
	}
}
