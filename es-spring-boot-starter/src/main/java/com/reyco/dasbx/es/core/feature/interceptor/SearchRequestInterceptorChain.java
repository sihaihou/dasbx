package com.reyco.dasbx.es.core.feature.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.reyco.dasbx.es.core.query.SearchContext;

public class SearchRequestInterceptorChain {

	private List<SearchRequestInterceptor> interceptors;
	
	private int interceptorindex = -1;

	public SearchRequestInterceptorChain(List<SearchRequestInterceptor> interceptors) {
		super();
		List<SearchRequestInterceptor> sortedInterceptors = new ArrayList<>(interceptors);
		AnnotationAwareOrderComparator.sort(sortedInterceptors);
		
		this.interceptors = sortedInterceptors;
	}

	public void apply(SearchContext context, SearchRequest request) {
		try {
			for (int i = 0; i < interceptors.size(); i++) {
				SearchRequestInterceptor interceptor = interceptors.get(i);
				interceptor.before(context, request);
				interceptorindex = i;
			}
			for (SearchRequestInterceptor interceptor : interceptors) {
				interceptor.apply(context, request);
			}
		} finally {
			for (int i = interceptorindex; i >= 0; i--) {
				try {
					SearchRequestInterceptor interceptor = interceptors.get(i);
					interceptor.after(context, request);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
}
