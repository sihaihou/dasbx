package com.reyco.dasbx.es.core.pipeline.query;

import java.util.List;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.pipeline.SearchPipeline;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;

public class SourcePipeline implements SearchPipeline {

	@Override
	public void execute(SearchContext context) {
		try {
			SearchQuery query = context.getQuery();
			
			List<String> includes = query.getIncludeFields();
			List<String> excludes = query.getExcludeFields();
	
			if ((includes == null || includes.isEmpty())
					&& (excludes == null || excludes.isEmpty())) {
				return;
			}
	
			String[] includeArray = includes == null ? new String[0] : includes.toArray(new String[0]);
			String[] excludeArray = excludes == null ? new String[0] : excludes.toArray(new String[0]);
	
			context.getSourceBuilder().fetchSource(includeArray,excludeArray);
		}catch (Exception e) {
			throw new SearchBuildException("Source build error!",e);
		}
	}
	
	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}
}
