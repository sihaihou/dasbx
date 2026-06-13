package com.reyco.dasbx.es.core.pipeline;

import org.springframework.core.Ordered;

import com.reyco.dasbx.es.core.query.SearchContext;

/**
 * 搜索流程:责任链
 * 
 * @author reyco
 *
 */
public interface SearchPipeline extends Ordered {

	void execute(SearchContext context);

	@Override
	default int getOrder() {
		return LOWEST_PRECEDENCE;
	}
}
