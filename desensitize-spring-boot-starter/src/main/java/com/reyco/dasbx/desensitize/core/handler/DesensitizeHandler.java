package com.reyco.dasbx.desensitize.core.handler;

import com.reyco.dasbx.desensitize.core.DesensitizeType;

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
