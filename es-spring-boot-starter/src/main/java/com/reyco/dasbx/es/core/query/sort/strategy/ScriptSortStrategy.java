package com.reyco.dasbx.es.core.query.sort.strategy;

import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.sort.ScriptSortDefinition;

public class ScriptSortStrategy implements SortStrategy<ScriptSortDefinition> {

	@Override
	public Class<ScriptSortDefinition> support() {
		return ScriptSortDefinition.class;
	}

	@Override
	public void apply(ScriptSortDefinition sort, SearchContext context) {
		Script script = new Script(ScriptType.INLINE, "painless", sort.getScript(), sort.getParams());
		ScriptSortBuilder builder = SortBuilders.scriptSort(script, sort.getType()).order(sort.getSortOrder());
		context.getSourceBuilder().sort(builder);
	}
}
