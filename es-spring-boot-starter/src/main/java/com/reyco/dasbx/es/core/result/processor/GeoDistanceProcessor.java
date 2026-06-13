package com.reyco.dasbx.es.core.result.processor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.elasticsearch.search.SearchHit;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.geo.sort.GeoDistanceSortDefinition;
import com.reyco.dasbx.es.core.query.sort.SortDefinition;
import com.reyco.dasbx.es.core.result.SearchHitResult;

public class GeoDistanceProcessor implements SearchHitProcessor {
	
	@Override
	public <T> void process(SearchContext context, SearchHit hit, T entity, SearchHitResult<T> result) {
		Integer distanceIndex = findDistanceIndex(context);
		if (distanceIndex == null) {
			return;
		}
		if (hit.getSortValues() != null && hit.getSortValues().length > distanceIndex) {
			Object value = hit.getSortValues()[distanceIndex];
			if (value instanceof Number) {
				BigDecimal distanceBigDecimal = new BigDecimal(String.valueOf(value)).setScale(4, RoundingMode.HALF_UP);
				double distance = distanceBigDecimal.doubleValue();
				result.setDistance(distance);
			}
		}

	}
	@Override
	public int getOrder() {
		return 0;
	}
	/**
	 * 获取距离索引
	 * 
	 * @param context
	 * @return
	 */
	private static Integer findDistanceIndex(SearchContext context) {
		List<SortDefinition> sorts = context.getQuery().getSorts();
		for (int i = 0; i < sorts.size(); i++) {
			if (sorts.get(i) instanceof GeoDistanceSortDefinition) {
				return i;
			}
		}
		return null;
	}
}
