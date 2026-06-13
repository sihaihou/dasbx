package com.reyco.dasbx.es.core.query.sort.strategy;

import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.NestedSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

import com.reyco.dasbx.es.core.metadata.FieldMetadata;
import com.reyco.dasbx.es.core.metadata.register.MetadataRegistry;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.sort.FieldSortDefinition;

public class FieldSortStrategy implements SortStrategy<FieldSortDefinition> {

	private MetadataRegistry registry;

	public FieldSortStrategy(MetadataRegistry registry) {
		this.registry = registry;
	}

	@Override
	public Class<FieldSortDefinition> support() {
		return FieldSortDefinition.class;
	}

	@Override
	public void apply(FieldSortDefinition sort, SearchContext context) {
		FieldMetadata field = registry.getField(context.getClazz(),sort.getField());
		FieldSortBuilder builder = SortBuilders.fieldSort(sort.getResolvedField());
		builder.order(sort.getSortOrder());
		if (field != null && field.isNested()) {
			builder.setNestedSort(new NestedSortBuilder(field.getNestedPath()));
		}
		context.getSourceBuilder().sort(builder);
	}
}
