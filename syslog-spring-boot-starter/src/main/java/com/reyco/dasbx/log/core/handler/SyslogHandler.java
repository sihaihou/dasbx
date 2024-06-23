package com.reyco.dasbx.log.core.handler;

import com.reyco.dasbx.log.core.SyslogMessage;

/**
 * 系统日志处理器
 * @author reyco
 *
 */
public interface SyslogHandler {
	
	void handler(SyslogMessage syslogMessage);
	
}
