package com.reyco.dasbx.desensitize.core.handler;

import java.util.List;

import com.reyco.dasbx.desensitize.core.DesensitizeType;

/**
 * 脱敏委托类
 * @author reyco
 *
 */
public class DelegateDesensitizeHandler implements DesensitizeHandler{
	
	private List<DesensitizeHandler> desensitizeHandlers;
	
	public void setDesensiteHandlers(List<DesensitizeHandler> desensitizeHandlers) {
		this.desensitizeHandlers = desensitizeHandlers;
	}

	@Override
	public String handler(DesensitizeType type, String value) {
		return getMatchDesensiteHandler(type).handler(type, value);
	}
	
	private DesensitizeHandler getMatchDesensiteHandler(DesensitizeType desensitizeType) {
		for (DesensitizeHandler desensitizeHandler : desensitizeHandlers) {
			if(desensitizeHandler.support(desensitizeType)) {
				return desensitizeHandler;
			}
		}
		return new DefaultExpressionDesensitizeHandler();
	}
}
