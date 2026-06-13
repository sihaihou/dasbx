package com.reyco.dasbx.es.core.pipeline.feature;

import java.util.List;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.feature.highlight.HighlightDefinition;
import com.reyco.dasbx.es.core.pipeline.SearchPipeline;
import com.reyco.dasbx.es.core.query.SearchContext;

public class HighlightPipeline implements SearchPipeline {

	@Override
	public void execute(SearchContext context) {
		try {
			List<HighlightDefinition> highlights = context.getQuery().getHighlights();
			if (highlights == null || highlights.isEmpty()) {
				return;
			}
			HighlightBuilder builder = new HighlightBuilder();
			for (HighlightDefinition def : highlights) {
				HighlightBuilder.Field field = new HighlightBuilder.Field(def.getField());
				field.preTags(def.getPreTag());
				field.postTags(def.getPostTag());
				field.fragmentSize(def.getFragmentSize());
				field.numOfFragments(def.getNumOfFragments());
				builder.field(field);
			}
			builder.requireFieldMatch(false);
			context.getSourceBuilder().highlighter(builder);
		}catch (Exception e) {
			throw new SearchBuildException("Highlight build error!",e);
		}
	}
	
	@Override
	public int getOrder() {
		return 60;
	}
}
