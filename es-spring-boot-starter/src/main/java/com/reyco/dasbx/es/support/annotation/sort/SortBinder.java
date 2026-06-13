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
import com.reyco.dasbx.es.support.annotation.page.EsPageParam;
import com.reyco.dasbx.es.support.execution.annotation.EsExecutionContext;

public class SortBinder {
	
	public void bind(Object dto,AnnotationMetadata metadata, EsExecutionContext context) {
		List<SortDefinition> sorts = context.getSorts();
		
		for (SortMetadata meta : metadata.getSorts()) {
			try {
				EsSort sortAnno = meta.getAnnotation();
				Object value = meta.getField().get(dto);
				if(value==null) {
					continue;
				}else if(!(value instanceof EsSortParam)){
					throw new SearchAnnotationException("@EsSort must be annotated on the EsSortParam class");
				}else {
					EsSortParam sort = (EsSortParam) value;
					if(!sort.isEnabled()) {
						continue;
					}
					if(sorts==null) {
						sorts = new ArrayList<>();
						context.setSorts(sorts);
					}
					
					String fieldName = StringUtils.hasText(sortAnno.field()) ? sortAnno.field() : meta.getField().getName();
					
					SortOrder sortOrder = SortOrder.fromString(sort.getSortOrder());
					
					int order = sort.getOrder();
					
					switch (sort.getType()) {
					case FIELD:
						sorts.add(Sorts.field(fieldName,sortOrder,order));		
						break;
					case SCORE:
						sorts.add(Sorts.score(sortOrder,order));
						break;
					case SCRIPT:
						 sorts.add(Sorts.script(sort.getScript(), new HashMap<>(), ScriptSortType.fromString(sort.getScriptType()),sortOrder,order));
						 break;
					case GEO_DISTANCE:
						 sorts.add(Sorts.geoDistance(fieldName, sort.getLatitude(), sort.getLongitude(), DistanceUnit.fromString(sort.getUnit()), sortOrder,order));
						 break;
					}
				}
			}catch (Exception e) {
				throw new SearchAnnotationException("sort bind error : " + meta.getField().getName(), e);
			}
		}
	}
	public static SortOrder convert(String sortOrder) {
		return SortOrder.fromString(sortOrder);
	}
}
