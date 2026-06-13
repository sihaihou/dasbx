package com.reyco.dasbx.es.core.log;

import com.reyco.dasbx.es.core.query.SearchContext;

public interface DslLogger {

	void log(SearchContext context, long took);

	void error(SearchContext context, long took, Exception e);

}
