package com.reyco.dasbx.log.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.log.core.SyslogMessage;

public class SimpleSyslogHandler implements SyslogHandler{
	
	private static Logger logger = LoggerFactory.getLogger(SimpleSyslogHandler.class);
	
	@Override
	public void handler(SyslogMessage syslogMessage) {
		if(logger.isDebugEnabled()) {
			logger.debug(syslogMessage.toString());
		}
	}

}
