package com.reyco.dasbx.es.core.validator;

import java.util.List;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.metadata.FieldCapability;
import com.reyco.dasbx.es.core.metadata.FieldMetadata;
import com.reyco.dasbx.es.core.metadata.IndexMetadata;
import com.reyco.dasbx.es.core.metadata.register.MetadataRegistry;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;
import com.reyco.dasbx.es.core.query.sort.FieldSortDefinition;
import com.reyco.dasbx.es.core.query.sort.SortDefinition;

public class SortValidator implements QueryValidator {

	private MetadataRegistry registry;

	public SortValidator(MetadataRegistry registry) {
		super();
		this.registry = registry;
	}

	@Override
	public void validate(SearchContext context) {
		SearchQuery query = context.getQuery();
		List<SortDefinition> sorts = query.getSorts();
		if (sorts == null || sorts.isEmpty()) {
			return;
		}

		IndexMetadata metadata = registry.get(context.getClass());
		if (metadata == null) {
			return;
		}

		for (SortDefinition sort : sorts) {
			if (!(sort instanceof FieldSortDefinition)) {
				continue;
			}

			FieldSortDefinition fieldSort = (FieldSortDefinition) sort;

			FieldMetadata field = metadata.getField(fieldSort.getField());

			if (field == null) {
				continue;
			}

			String resolvedField = field.resolveField(FieldCapability.SORT);
			if (resolvedField == null) {
				throw new SearchBuildException("field [" + field.getField() + "] does not support sort");
			}
		}
	}

	@Override
	public int getOrder() {
		return 20;
	}
}
