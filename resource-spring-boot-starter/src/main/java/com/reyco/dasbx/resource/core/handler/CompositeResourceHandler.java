package com.reyco.dasbx.resource.core.handler;

import java.util.List;

import com.reyco.dasbx.resource.core.model.ResourceDefinition;

/**
 * 组合类
 * @author reyco
 *
 */
public class CompositeResourceHandler implements ResourceHandler{
	
	private final List<ResourceHandler> delegates;
	
	public CompositeResourceHandler(List<ResourceHandler> delegates) {
		super();
		this.delegates = delegates;
	}
	
	@Override
	public void handler(ResourceDefinition reycoResource) {
		if(delegates!=null && delegates.size()>0) {
			delegates.stream().forEach(handler->handler.handler(reycoResource));
		}
	}

}
