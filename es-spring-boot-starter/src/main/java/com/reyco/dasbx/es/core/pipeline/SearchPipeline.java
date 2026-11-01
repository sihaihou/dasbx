package com.reyco.dasbx.es.core.pipeline;

import org.springframework.core.Ordered;

import com.reyco.dasbx.es.core.feature.interceptor.SearchOperationType;
import com.reyco.dasbx.es.core.query.SearchContext;

/**
 * 搜索流程:责任链
 * 
 * @author reyco
 *
 */
public interface SearchPipeline extends Ordered {

	/**
	 * 当前 Pipeline 是否适用于该搜索操作。
	 * 默认只处理首次搜索，scroll 续页 Pipeline 必须显式覆盖。
	 */
	default boolean supports(SearchOperationType operation) {
		return operation == SearchOperationType.SEARCH;
	}

	void execute(SearchContext context);

	@Override
	default int getOrder() {
		return LOWEST_PRECEDENCE;
	}
}
