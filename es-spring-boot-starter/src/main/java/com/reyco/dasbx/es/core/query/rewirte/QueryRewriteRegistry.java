package com.reyco.dasbx.es.core.query.rewirte;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.query.SearchContext;

public class QueryRewriteRegistry {

	private final Map<Class<?>, QueryRewrite<?>> rewriteMap = new ConcurrentHashMap<>();

	public QueryRewriteRegistry(List<QueryRewrite<?>> rewrites) {
		for (QueryRewrite<?> rewrite : rewrites) {
			Class<?> support = rewrite.support();
			if (rewriteMap.containsKey(support)) {
				throw new SearchBuildException("Duplicate QueryRewrite : " + support.getName());
			}
			rewriteMap.put(support, rewrite);
		}
	}

	public void rewrite(Object definition, SearchContext context) {
		if (definition == null) {
			return;
		}
		QueryRewrite rewrite = rewriteMap.get(definition.getClass());
		if (rewrite != null) {
			rewrite.rewrite(definition, context);
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private <T> void invokeRewrite(QueryRewrite<?> rewrite, Object definition, SearchContext context) {
		((QueryRewrite<T>) rewrite).rewrite((T) definition, context);
	}
}
