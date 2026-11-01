package com.reyco.dasbx.es.support.execution.annotation;

import java.util.Objects;

import com.reyco.dasbx.es.support.annotation.aggregation.AggregationBinder;
import com.reyco.dasbx.es.support.annotation.highlight.HighlightBinder;
import com.reyco.dasbx.es.support.annotation.metadata.AnnotationMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.parser.AnnotationMetadataRegistry;
import com.reyco.dasbx.es.support.annotation.page.PageBinder;
import com.reyco.dasbx.es.support.annotation.query.QueryBinder;
import com.reyco.dasbx.es.support.annotation.routing.RoutingBinder;
import com.reyco.dasbx.es.support.annotation.sort.SortBinder;
import com.reyco.dasbx.es.support.annotation.source.SourceBinder;

public class EsExecutionFactory {

	private final AnnotationMetadataRegistry registry;

	private final RoutingBinder routingBinder;

	private final SourceBinder sourceBinder;

	private final QueryBinder queryBinder;

	private final PageBinder pageBinder;

	private final SortBinder sortBinder;

	private final HighlightBinder highlightBinder;

	private final AggregationBinder aggregationBinder;

	public EsExecutionFactory(AnnotationMetadataRegistry registry, RoutingBinder routingBinder,
			SourceBinder sourceBinder, QueryBinder queryBinder, PageBinder pageBinder, SortBinder sortBinder,
			HighlightBinder highlightBinder, AggregationBinder aggregationBinder) {

		this.registry = Objects.requireNonNull(registry, "AnnotationMetadataRegistry must not be null");
		this.routingBinder = Objects.requireNonNull(routingBinder, "RoutingBinder must not be null");
		this.sourceBinder = Objects.requireNonNull(sourceBinder, "SourceBinder must not be null");
		this.queryBinder = Objects.requireNonNull(queryBinder, "QueryBinder must not be null");
		this.pageBinder = Objects.requireNonNull(pageBinder, "PageBinder must not be null");
		this.sortBinder = Objects.requireNonNull(sortBinder, "SortBinder must not be null");
		this.highlightBinder = Objects.requireNonNull(highlightBinder, "HighlightBinder must not be null");
		this.aggregationBinder = Objects.requireNonNull(aggregationBinder, "AggregationBinder must not be null");
	}

	public EsExecutionContext create(Object dto) {
		EsExecutionContext context = new EsExecutionContext();
		if (dto == null) {
			return context;
		}

		AnnotationMetadata metadata = registry.get(dto.getClass());

		context.setIndex(metadata.getIndex());

		routingBinder.bind(dto, metadata, context);

		sourceBinder.bind(dto, metadata, context);

		queryBinder.bind(dto, metadata, context);

		pageBinder.bind(dto, metadata, context);

		sortBinder.bind(dto, metadata, context);

		highlightBinder.bind(metadata, context);

		aggregationBinder.bind(metadata, context);

		return context;
	}
}
