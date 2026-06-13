package com.reyco.dasbx.es.core.query.rewirte;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.metadata.FieldCapability;
import com.reyco.dasbx.es.core.metadata.resolver.FieldResolver;
import com.reyco.dasbx.es.core.query.SearchContext;

public class AggregationRewrite implements QueryRewrite<AggregationDefinition> {

	private FieldResolver fieldResolver;

	public AggregationRewrite(FieldResolver fieldResolver) {
		this.fieldResolver = fieldResolver;
	}

	@Override
	public Class<AggregationDefinition> support() {
		return AggregationDefinition.class;
	}

	@Override
	public void rewrite(AggregationDefinition aggregation, SearchContext context) {
		String resolveField = fieldResolver.resolve(context.getClazz(),aggregation.getField(),FieldCapability.AGGREGATION);
		aggregation.setResolvedField(resolveField);
		
		// children
		if (aggregation.getChildren() != null) {
			for (AggregationDefinition child : aggregation.getChildren()) {
				rewrite(child, context);
			}
		}
	}
}
