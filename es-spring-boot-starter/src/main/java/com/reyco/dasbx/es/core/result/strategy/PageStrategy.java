package com.reyco.dasbx.es.core.result.strategy;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.page.PageDefinition;

public interface PageStrategy <T extends PageDefinition> {

	Class<T> support();

	void apply(T page,SearchContext context);
	
}
