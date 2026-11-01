package com.reyco.dasbx.es.support.annotation.sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.search.sort.ScriptSortBuilder.ScriptSortType;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.StringUtils;

import com.reyco.dasbx.es.core.query.sort.SortDefinition;
import com.reyco.dasbx.es.core.query.sort.Sorts;
import com.reyco.dasbx.es.support.annotation.execptio.SearchAnnotationException;
import com.reyco.dasbx.es.support.annotation.metadata.AnnotationMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.SortMetadata;
import com.reyco.dasbx.es.support.execution.annotation.EsExecutionContext;

public class SortBinder {

	public void bind(Object dto, AnnotationMetadata metadata, EsExecutionContext context) {
		List<SortDefinition> sorts = context.getSorts();

		for (SortMetadata meta : metadata.getSorts()) {
			try {
				EsSort sortAnno = meta.getAnnotation();
				Object value = meta.getField().get(dto);
				if (value == null) {
					continue;
				} else if (!(value instanceof EsSortParam)) {
					throw new SearchAnnotationException("@EsSort must be annotated on the EsSortParam class");
				} else {
					EsSortParam sort = (EsSortParam) value;
					Boolean enabled = sort.isEnabled();
					if (enabled == null) {
					    throw new SearchAnnotationException(
					            "@EsSort enabled must not be null");
					}
					if (!enabled) {
					    continue;
					}
					if (sorts == null) {
						sorts = new ArrayList<>();
						context.setSorts(sorts);
					}

					String fieldName = StringUtils.hasText(sortAnno.field()) ? sortAnno.field() : meta.getField().getName();
					if (!StringUtils.hasText(sort.getSortOrder())) {
					    throw new SearchAnnotationException("@EsSort sortOrder must not be empty");
					}
					SortOrder sortOrder = SortOrder.fromString(sort.getSortOrder());
					
					int order = sort.getOrder();
					EsSortType sortType = sort.getType();
					if (sortType == null) {
					    throw new SearchAnnotationException("@EsSort type must not be null");
					}
					
					switch (sortType) {
					case FIELD:
						sorts.add(Sorts.field(fieldName, sortOrder, order));
						break;
					case SCORE:
						sorts.add(Sorts.score(sortOrder, order));
						break;
					case SCRIPT:
						if (!StringUtils.hasText(sort.getScript())) {
						    throw new SearchAnnotationException("SCRIPT sort requires script");
						}
						if (!StringUtils.hasText(sort.getScriptType())) {
						    throw new SearchAnnotationException("SCRIPT sort requires scriptType");
						}
						sorts.add(Sorts.script(sort.getScript(), new HashMap<>(),
								ScriptSortType.fromString(sort.getScriptType()), sortOrder, order));
						break;
					case GEO_DISTANCE:
						Double latitude = sort.getLatitude();
						Double longitude = sort.getLongitude();

						if (latitude == null || longitude == null) {
							throw new SearchAnnotationException("GEO_DISTANCE sort requires latitude and longitude");
						}

						if (latitude.isNaN() || latitude.isInfinite() || longitude.isNaN() || longitude.isInfinite()) {
							throw new SearchAnnotationException("GEO_DISTANCE sort coordinates must be finite");
						}
						if (latitude < -90 || latitude > 90) {
							throw new SearchAnnotationException(
									"GEO_DISTANCE sort latitude must be between -90 and 90");
						}

						if (longitude < -180 || longitude > 180) {
							throw new SearchAnnotationException(
									"GEO_DISTANCE sort longitude must be between -180 and 180");
						}

						if (!StringUtils.hasText(sort.getUnit())) {
							throw new SearchAnnotationException("GEO_DISTANCE sort unit must not be empty");
						}
						sorts.add(Sorts.geoDistance(
					            fieldName,
					            latitude,
					            longitude,
					            DistanceUnit.fromString(sort.getUnit()),
					            sortOrder,
					            order));
						break;
					}
				}
			} catch (SearchAnnotationException e) {
			    throw e;
			} catch (Exception e) {
			    throw new SearchAnnotationException("sort bind error : " + meta.getField().getName(), e);
			}
		}
	}

	public static SortOrder convert(String sortOrder) {
		return SortOrder.fromString(sortOrder);
	}
}
