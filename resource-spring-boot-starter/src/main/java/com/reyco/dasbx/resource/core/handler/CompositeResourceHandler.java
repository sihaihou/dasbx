package com.reyco.dasbx.resource.core.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.resource.core.model.ResourceDefinition;

/**
 * 组合类
 * @author reyco
 *
 */
public class CompositeResourceHandler implements ResourceHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompositeResourceHandler.class);
	
	private final List<ResourceHandler> delegates;
	
	public CompositeResourceHandler(List<ResourceHandler> delegates) {
		super();
		this.delegates = delegates;
	}
	
	@Override
	public void handler(ResourceDefinition resourceDefinition) {
		if (delegates == null || delegates.isEmpty()) {
            return;
        }

		for (ResourceHandler delegate : delegates) {
            if (delegate == null || delegate == this) {
                continue;
            }
            try {
                delegate.handler(resourceDefinition);
            } catch (Throwable e) {
                LOGGER.warn("资源日志处理器执行失败: handler={}, message={}", delegate.getClass().getName(), e.getMessage());
            }
        }
	}

}
