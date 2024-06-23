package com.reyco.dasbx.log.core.handler;

import java.util.List;

import com.reyco.dasbx.log.core.SyslogMessage;

/**
 * 组合类
 * @author reyco
 *
 */
public class CompositeSyslogHandler implements SyslogHandler{
	
	private final List<SyslogHandler> delegates;
	
	public CompositeSyslogHandler(List<SyslogHandler> delegates) {
		super();
		this.delegates = delegates;
	}
	
	@Override
	public void handler(SyslogMessage syslogMessage) {
		delegates.stream().forEach(handler->handler.handler(syslogMessage));
	}

}
