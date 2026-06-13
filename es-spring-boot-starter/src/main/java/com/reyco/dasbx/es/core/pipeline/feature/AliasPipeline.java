package com.reyco.dasbx.es.core.pipeline.feature;

import java.util.List;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.feature.alias.AliasManager;
import com.reyco.dasbx.es.core.pipeline.SearchPipeline;
import com.reyco.dasbx.es.core.query.SearchContext;

public class AliasPipeline implements SearchPipeline {

	private AliasManager aliasManager;
	
	public AliasPipeline(AliasManager aliasManager) {
		super();
		this.aliasManager = aliasManager;
	}

	public void setAliasManager(AliasManager aliasManager) {
		this.aliasManager = aliasManager;
	}

	@Override
	public void execute(SearchContext context) {
		try {
			String alias = context.getIndex();
			if (alias == null) {
				throw new RuntimeException("index is null");
			}
			List<String> indices = aliasManager.resolve(alias);
			context.setResolvedIndices(indices);
		}catch (Exception e) {
			throw new SearchBuildException("Alias build error!",e);
		}
	}
	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}
}
