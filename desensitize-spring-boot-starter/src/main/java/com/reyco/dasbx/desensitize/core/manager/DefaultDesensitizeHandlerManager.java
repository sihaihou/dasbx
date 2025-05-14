package com.reyco.dasbx.desensitize.core.manager;

import com.reyco.dasbx.desensitize.core.handler.DesensitizeHandler;
import com.reyco.dasbx.desensitize.utils.SpringContextUtils;

public class DefaultDesensitizeHandlerManager implements DesensitizeHandlerManager{
	
	@Override
	public DesensitizeHandler getDesensitizeHandler() {
		DesensitizeHandler desensiteHandler = SpringContextUtils.getBean(DesensitizeHandler.class);
		return desensiteHandler;
	}

}
