package com.reyco.dasbx.es.core.query.sort.strategy;

import org.elasticsearch.search.sort.SortBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.sort.ScoreSortDefinition;

public class ScoreSortStrategy implements SortStrategy<ScoreSortDefinition> {

	@Override
	public Class<ScoreSortDefinition> support() {
		return ScoreSortDefinition.class;
	}

	@Override
	public void apply(ScoreSortDefinition sort, SearchContext context) {
		context.getSourceBuilder()
				.sort(SortBuilders.scoreSort().order(sort.getSortOrder()));
	}

}
