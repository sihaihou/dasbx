package com.reyco.dasbx.es.core.query.rewirte;

import com.reyco.dasbx.es.core.query.SearchContext;

public interface QueryRewrite<T> {

	Class<T> support();

	void rewrite(T definition, SearchContext context);

}
