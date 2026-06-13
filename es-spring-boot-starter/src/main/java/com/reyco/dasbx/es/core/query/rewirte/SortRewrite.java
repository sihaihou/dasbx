package com.reyco.dasbx.es.core.query.rewirte;

import com.reyco.dasbx.es.core.metadata.FieldCapability;
import com.reyco.dasbx.es.core.metadata.resolver.FieldResolver;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.sort.FieldSortDefinition;

public class SortRewrite implements QueryRewrite<FieldSortDefinition> {

	private FieldResolver fieldResolver;

	public SortRewrite(FieldResolver fieldResolver) {
		this.fieldResolver = fieldResolver;
	}

	@Override
	public Class<FieldSortDefinition> support() {
		return FieldSortDefinition.class;
	}

	@Override
	public void rewrite(FieldSortDefinition sort, SearchContext context) {
		String resolveField = fieldResolver.resolve(context.getClazz(), sort.getField(), FieldCapability.SORT);
		sort.setResolvedField(resolveField);
	}
}
