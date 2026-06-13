package com.reyco.dasbx.desensitize.core.handler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.reyco.dasbx.desensitize.core.DesensitizeType;

/**
 * 脱敏委托类
 * @author reyco
 *
 */
public class DelegateDesensitizeHandler implements DesensitizeHandler{
	
	private final Map<Class<? extends DesensitizeType>, DesensitizeHandler> handlerCache = new ConcurrentHashMap<>(16);
	
	private List<DesensitizeHandler> desensitizeHandlers;
	
	private DesensitizeHandler defaultDesensitizeHandler;
	
	public void setDesensiteHandlers(List<DesensitizeHandler> desensitizeHandlers,DesensitizeHandler defaultDesensitizeHandler) {
		this.desensitizeHandlers = desensitizeHandlers;
		this.defaultDesensitizeHandler = defaultDesensitizeHandler;
	}
	
	@Override
	public String handler(DesensitizeType type, String value) {
		return getMatchDesensiteHandler(type).handler(type, value);
	}
	
	private DesensitizeHandler getMatchDesensiteHandler(DesensitizeType desensitizeType) {
		Class<? extends DesensitizeType> typeClass = desensitizeType.getClass();
		
		// 1. 先从缓存中直接获取
        DesensitizeHandler cachedHandler = handlerCache.get(typeClass);
        if (cachedHandler != null) {
            return cachedHandler;
        }
        
        // 2. 缓存未命中，进入原有 for 循环逻辑
		for (DesensitizeHandler desensitizeHandler : desensitizeHandlers) {
			if(desensitizeHandler.support(desensitizeType)) {
				handlerCache.putIfAbsent(typeClass, desensitizeHandler);
				return desensitizeHandler;
			}
		}
		
		// 3. 兜底逻辑：存入默认处理器并返回
		handlerCache.putIfAbsent(typeClass, defaultDesensitizeHandler);
		return defaultDesensitizeHandler;
	}
}
