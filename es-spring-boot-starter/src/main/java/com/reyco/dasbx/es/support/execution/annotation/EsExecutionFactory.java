package com.reyco.dasbx.es.support.execution.annotation;

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

	private static AnnotationMetadataRegistry registry = new AnnotationMetadataRegistry();
	
	private static RoutingBinder routingBinder = new RoutingBinder();
	
	private static SourceBinder sourceBinder = new SourceBinder();
	
	private static QueryBinder queryBinder = new QueryBinder();
	
	private static PageBinder pageBinder = new PageBinder();
	
	private static SortBinder sortBinder = new SortBinder();

	private static AggregationBinder aggregationBinder = new AggregationBinder();

	private static HighlightBinder highlightBinder = new HighlightBinder();
	
	
	public static EsExecutionContext create(Object dto) {
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
