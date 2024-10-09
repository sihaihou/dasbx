package com.reyco.dasbx.actuator.tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractToolsIndicator implements ToolsIndicator{
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public final Tools tools() {
		Tools.Builder builder = new Tools.Builder();
		try {
			doToolsCheck(builder);
		}catch (Exception ex) {
			if (this.logger.isWarnEnabled()) {
				this.logger.warn(ex);
			}
			builder.name(ex.getMessage());
		}
		return builder.build();
	}

	protected abstract void doToolsCheck(Tools.Builder builder) throws Exception;
	
}
