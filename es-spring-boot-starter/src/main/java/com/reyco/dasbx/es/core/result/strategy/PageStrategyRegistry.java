package com.reyco.dasbx.es.core.result.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.page.PageDefinition;
import com.reyco.dasbx.es.core.result.page.Pages;

public class PageStrategyRegistry {

	private final Map<Class<? extends PageDefinition>, PageStrategy<?>> strategyMap = new ConcurrentHashMap<>();

	public PageStrategyRegistry(List<PageStrategy<?>> strategies) {
		if(strategies==null || strategies.isEmpty()) {
			strategies = new ArrayList<>();
			strategies.add(new OffsetPageStrategy());
		}
		for (PageStrategy<?> strategy : strategies) {
			Class<? extends PageDefinition> support = strategy.support();
	        if(strategyMap.containsKey(support)) {
	            throw new SearchBuildException("Duplicate PageStrategy : "+ support.getName());
	        }
	        strategyMap.put(support, strategy);
		}
	}

	@SuppressWarnings("unchecked")
	public void apply(PageDefinition page, SearchContext context) {
		if (page == null) {
			page = Pages.offset();
		}

		PageStrategy strategy = strategyMap.get(page.getClass());
		if (strategy == null) {
			throw new SearchBuildException("PageStrategy not found:"+page.getClass().getName());
		}
		
		strategy.apply(page, context);
	}
}
