package com.reyco.dasbx.es.core.validator;

import java.util.List;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.metadata.FieldCapability;
import com.reyco.dasbx.es.core.metadata.FieldMetadata;
import com.reyco.dasbx.es.core.metadata.IndexMetadata;
import com.reyco.dasbx.es.core.metadata.register.MetadataRegistry;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;

public class AggregationValidator implements QueryValidator {

	private MetadataRegistry registry;

	public AggregationValidator(MetadataRegistry registry) {
		super();
		this.registry = registry;
	}

	@Override
	public void validate(SearchContext context) {
		SearchQuery query = context.getQuery();
		List<AggregationDefinition> aggregations = query.getAggregations();
		if (aggregations == null || aggregations.isEmpty()) {
			return;
		}

		IndexMetadata metadata = registry.get(context.getClass());
		if (metadata == null) {
			return;
		}

		for (AggregationDefinition agg : aggregations) {
			validateAggregation(metadata, agg);
		}
	}

	private void validateAggregation(IndexMetadata metadata, AggregationDefinition agg) {
		FieldMetadata field = metadata.getField(agg.getField());
		if (field != null) {
			String resolvedField = field.resolveField(FieldCapability.AGGREGATION);
			if (resolvedField == null) {
				throw new SearchBuildException("field [" + field.getField() + "] does not support aggregation");
			}
		}

		if (agg.getChildren() != null) {
			for (AggregationDefinition child : agg.getChildren()) {
				validateAggregation(metadata, child);
			}
		}
	}

	@Override
	public int getOrder() {
		return 30;
	}
}
