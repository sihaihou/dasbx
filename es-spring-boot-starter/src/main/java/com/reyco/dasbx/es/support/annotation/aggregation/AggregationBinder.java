package com.reyco.dasbx.es.support.annotation.aggregation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationType;
import com.reyco.dasbx.es.support.annotation.metadata.AggregationMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.AnnotationMetadata;
import com.reyco.dasbx.es.support.execution.annotation.EsExecutionContext;
import com.reyco.dasbx.es.support.result.facet.FacetLabelProvider;

public class AggregationBinder {

	public void bind(AnnotationMetadata metadata, EsExecutionContext context) {

		List<AggregationDefinition> aggs = context.getAggregations();

		Map<String, Class<? extends FacetLabelProvider>> providers = context.getProviderMap();

		for (AggregationMetadata meta : metadata.getAggregations()) {
			if (meta.getAnnotations().isEmpty()) {
				continue;
			}
			if(aggs==null) {
				aggs = new ArrayList<>();
				context.setAggregations(aggs);
			}
			if(providers==null) {
				providers = new HashMap<String, Class<? extends FacetLabelProvider>>();
				context.setProviderMap(providers);
			}
			buildAggregationDefinition(aggs, providers, meta);
		}
	}

	private static void buildAggregationDefinition(List<AggregationDefinition> aggsList,
			Map<String, Class<? extends FacetLabelProvider>> providerMap, AggregationMetadata meta) {
		Field field = meta.getField();
		for (EsAgg agg : meta.getAnnotations()) {
			String name = StringUtils.hasText(agg.name()) ? agg.name() : field.getName();
			String fieldName = StringUtils.hasText(agg.field()) ? agg.field() : field.getName();
			// 原生链式组装，完美契合你的亲生 AggregationDefinition 类
			AggregationDefinition aggDef = new AggregationDefinition()
					.setName(name)
					.setField(fieldName)
					.setSize(agg.size())
					.setOrder(agg.order())
					.setType(convertToSubtype(agg.type())); // 自动映射底层的
																										// 枚举
			aggsList.add(aggDef);

			providerMap.put(name, agg.provider());
		}
	}

	/**
	 * 将引擎层的聚合枚举安全路由为底层原生的 AggregationType 枚举
	 */
	private static AggregationType convertToSubtype(EsAggType aggType) {
		try {
			return AggregationType.valueOf(aggType.name());
		} catch (IllegalArgumentException e) {
			return AggregationType.TERMS;
		}
	}
}
