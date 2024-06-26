package com.reyco.dasbx.resource.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.resource.core.model.ResourceDefinition;

public class SimpleResourceHandler implements ResourceHandler{
	
	private static Logger logger = LoggerFactory.getLogger(SimpleResourceHandler.class);
	
	@Override
	public void handler(ResourceDefinition reycoResource) {
		if(logger.isDebugEnabled()) {
			logger.debug(reycoResource.toString());
		}
	}

}
