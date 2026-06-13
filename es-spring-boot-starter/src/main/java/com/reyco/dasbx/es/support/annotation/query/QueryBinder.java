package com.reyco.dasbx.es.support.annotation.query;

import java.lang.reflect.Array;
import java.util.Collection;

import org.springframework.util.StringUtils;

import com.reyco.dasbx.es.core.query.condition.BoolCondition;
import com.reyco.dasbx.es.core.query.condition.Querys;
import com.reyco.dasbx.es.support.annotation.execptio.SearchAnnotationException;
import com.reyco.dasbx.es.support.annotation.metadata.AnnotationMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.QueryMetadata;
import com.reyco.dasbx.es.support.execution.annotation.EsExecutionContext;

public class QueryBinder {

	private QueryStrategyRegistry registry;

	public QueryBinder() {
		this.registry = new QueryStrategyRegistry();
	}

	public void bind(Object dto, AnnotationMetadata metadata, EsExecutionContext context) {
		BoolCondition bool = context.getBoolCondition();

		for (QueryMetadata meta : metadata.getQueries()) {
			EsQuery queryAnno = meta.getAnnotation();
			if (queryAnno == null) {
				continue;
			}
			try {
				Object value = meta.getField().get(dto);
				if (isEmptyValue(value)) {
					continue;
				}
				if (bool == null) {
					bool = Querys.bool();
					context.setBoolCondition(bool);
				}
				String fieldName = StringUtils.hasText(queryAnno.field()) ? queryAnno.field()
						: meta.getField().getName();
				registry.apply(queryAnno.type(), bool, new QueryParam(fieldName, value, queryAnno));
			} catch (Exception e) {
				throw new SearchAnnotationException("query bind error : " + meta.getField().getName(), e);
			}
		}
	}
	private boolean isEmptyValue(Object value) {
	    if (value == null) {
	        return true;
	    }
	    if (value instanceof String) {
	        return !StringUtils.hasText((String) value);
	    }
	    if (value instanceof Collection) {
	        return ((Collection<?>) value).isEmpty();
	    }

	    if (value.getClass().isArray()) {
	        return Array.getLength(value) == 0;
	    }

	    return false;
	}
}
