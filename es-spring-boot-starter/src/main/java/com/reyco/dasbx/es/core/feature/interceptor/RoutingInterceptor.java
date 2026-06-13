package com.reyco.dasbx.es.core.feature.interceptor;

import org.elasticsearch.action.search.SearchRequest;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;

/**
 * 路由
 * @author reyco
 *
 */
public class RoutingInterceptor implements SearchRequestInterceptor{

	@Override
	public void apply(SearchContext context, SearchRequest request) {
		SearchQuery query =context.getQuery();
		if(query.getRouting() != null){
			request.routing(query.getRouting());
		}		
	}

	@Override
	public int getOrder() {
		return 10;
	}
}
