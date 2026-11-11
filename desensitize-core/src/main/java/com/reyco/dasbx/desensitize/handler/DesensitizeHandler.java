package com.reyco.dasbx.desensitize.handler;

import com.reyco.dasbx.desensitize.DesensitizeType;

/**
 * 脱敏处理器接口
 * @author reyco
 *
 */
public interface DesensitizeHandler {
	
	/**
	 * 是否支持
	 * @param type
	 * @return
	 */
	default Boolean support(DesensitizeType type) {
		return false;
	}
	
	/**
	 * 处理
	 * @param type
	 * @param value
	 * @return
	 */
	String handler(DesensitizeType type,String value);
	
}
