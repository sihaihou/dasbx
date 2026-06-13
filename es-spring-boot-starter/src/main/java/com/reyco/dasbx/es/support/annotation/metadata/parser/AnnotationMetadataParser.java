package com.reyco.dasbx.es.support.annotation.metadata.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;

import com.reyco.dasbx.es.support.annotation.EsIndex;
import com.reyco.dasbx.es.support.annotation.aggregation.EsAgg;
import com.reyco.dasbx.es.support.annotation.aggregation.EsAggs;
import com.reyco.dasbx.es.support.annotation.highlight.EsHighlightField;
import com.reyco.dasbx.es.support.annotation.metadata.AggregationMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.AnnotationMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.HighlightMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.PageMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.QueryMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.RoutingMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.SortMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.SourceMetadata;
import com.reyco.dasbx.es.support.annotation.page.EsPage;
import com.reyco.dasbx.es.support.annotation.query.EsQuery;
import com.reyco.dasbx.es.support.annotation.routing.EsRouting;
import com.reyco.dasbx.es.support.annotation.sort.EsSort;
import com.reyco.dasbx.es.support.annotation.source.EsSource;

public class AnnotationMetadataParser {

	public AnnotationMetadata parse(Class<?> clazz) {

		AnnotationMetadata metadata = new AnnotationMetadata();

		EsIndex esIndex = AnnotatedElementUtils.getMergedAnnotation(clazz, EsIndex.class);
		if (esIndex != null) {
			metadata.setIndex(StringUtils.hasText(esIndex.index()) ? esIndex.index() : clazz.getSimpleName());
		}

		Class<?> current = clazz;
		while (current != null && current != Object.class) {
			for (Field field : current.getDeclaredFields()) {
				
				field.setAccessible(true);
				
				EsRouting routing = AnnotatedElementUtils.findMergedAnnotation(field, EsRouting.class);
				if (routing != null) {
					metadata.getRoutings().add(new RoutingMetadata(field,routing));
				}
				
				EsQuery query = AnnotatedElementUtils.findMergedAnnotation(field, EsQuery.class);
				if (query != null) {
					metadata.getQueries().add(new QueryMetadata(field, query));
				}
				
				EsSource source = AnnotatedElementUtils.findMergedAnnotation(field, EsSource.class);
				if (routing != null) {
					metadata.getSources().add(new SourceMetadata(field,source));
				}
				
				EsPage page = AnnotatedElementUtils.findMergedAnnotation(field, EsPage.class);
				if (page != null) {
					metadata.getPages().add(new PageMetadata(field,page));
				}

				EsSort sort = AnnotatedElementUtils.findMergedAnnotation(field, EsSort.class);
				if (sort != null) {
					metadata.getSorts().add(new SortMetadata(field, sort));
				}

				EsHighlightField highlight = AnnotatedElementUtils.findMergedAnnotation(field, EsHighlightField.class);
				if (highlight != null) {
					metadata.getHighlights().add(new HighlightMetadata(field, highlight));
				}

				Set<EsAgg> aggs = AnnotatedElementUtils.getMergedRepeatableAnnotations(field, EsAgg.class,EsAggs.class);
				if (!aggs.isEmpty()) {
					metadata.getAggregations().add(new AggregationMetadata(field, new ArrayList<>(aggs)));
				}
			}
			current = current.getSuperclass();
		}

		return metadata;
	}
}
