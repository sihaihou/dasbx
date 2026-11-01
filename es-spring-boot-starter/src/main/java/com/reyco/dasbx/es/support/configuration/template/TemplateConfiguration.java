package com.reyco.dasbx.es.support.configuration.template;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.es.core.query.builder.factory.SearchBuilderFactory;
import com.reyco.dasbx.es.core.query.suggest.SuggestService;
import com.reyco.dasbx.es.support.annotation.aggregation.AggregationBinder;
import com.reyco.dasbx.es.support.annotation.highlight.HighlightBinder;
import com.reyco.dasbx.es.support.annotation.metadata.parser.AnnotationMetadataParser;
import com.reyco.dasbx.es.support.annotation.metadata.parser.AnnotationMetadataRegistry;
import com.reyco.dasbx.es.support.annotation.page.PageBinder;
import com.reyco.dasbx.es.support.annotation.query.QueryBinder;
import com.reyco.dasbx.es.support.annotation.query.QueryStrategyRegistry;
import com.reyco.dasbx.es.support.annotation.routing.RoutingBinder;
import com.reyco.dasbx.es.support.annotation.sort.SortBinder;
import com.reyco.dasbx.es.support.annotation.source.SourceBinder;
import com.reyco.dasbx.es.support.execution.annotation.EsExecutionFactory;
import com.reyco.dasbx.es.support.result.ResultProcessor;
import com.reyco.dasbx.es.support.result.facet.FacetConverter;
import com.reyco.dasbx.es.support.result.record.GeoDistanceSearchHitResultProcessor;
import com.reyco.dasbx.es.support.result.record.HighlightSearchHitResultProcessor;
import com.reyco.dasbx.es.support.result.record.RecordProcessor;
import com.reyco.dasbx.es.support.result.record.SearchHitProcessorRegistry;
import com.reyco.dasbx.es.support.result.record.SearchHitResultProcessor;
import com.reyco.dasbx.es.support.template.SearchTemplate;

@Configuration
public class TemplateConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public AnnotationMetadataParser annotationMetadataParser() {
		return new AnnotationMetadataParser();
	}

	@Bean
	@ConditionalOnMissingBean
	public AnnotationMetadataRegistry annotationMetadataRegistry(AnnotationMetadataParser parser) {
		return new AnnotationMetadataRegistry(parser);
	}

	@Bean
	@ConditionalOnMissingBean
	public QueryStrategyRegistry queryStrategyRegistry() {
		return new QueryStrategyRegistry();
	}

	@Bean
	@ConditionalOnMissingBean
	public RoutingBinder routingBinder() {
		return new RoutingBinder();
	}

	@Bean
	@ConditionalOnMissingBean
	public SourceBinder sourceBinder() {
		return new SourceBinder();
	}

	@Bean
	@ConditionalOnMissingBean
	public QueryBinder queryBinder(QueryStrategyRegistry registry) {
		return new QueryBinder(registry);
	}

	@Bean
	@ConditionalOnMissingBean
	public PageBinder pageBinder() {
		return new PageBinder();
	}

	@Bean
	@ConditionalOnMissingBean
	public SortBinder sortBinder() {
		return new SortBinder();
	}

	@Bean
	@ConditionalOnMissingBean
	public HighlightBinder highlightBinder() {
		return new HighlightBinder();
	}

	@Bean
	@ConditionalOnMissingBean
	public AggregationBinder aggregationBinder() {
		return new AggregationBinder();
	}

	@Bean
	@ConditionalOnMissingBean
	public EsExecutionFactory esExecutionFactory(AnnotationMetadataRegistry registry, RoutingBinder routingBinder,
			SourceBinder sourceBinder, QueryBinder queryBinder, PageBinder pageBinder, SortBinder sortBinder,
			HighlightBinder highlightBinder, AggregationBinder aggregationBinder) {

		return new EsExecutionFactory(registry, routingBinder, sourceBinder, queryBinder, pageBinder, sortBinder,
				highlightBinder, aggregationBinder);
	}

	@Bean
	@ConditionalOnMissingBean
	public SearchTemplate searchTemplate(SearchBuilderFactory factory, ResultProcessor resultProcessor,
			SuggestService suggestService, EsExecutionFactory executionFactory) {

		return new SearchTemplate(factory, resultProcessor, suggestService, executionFactory);
	}

	@Bean
	public ResultProcessor resultProcessor(RecordProcessor recordProcessor, FacetConverter facetConverter) {
		return new ResultProcessor(recordProcessor, facetConverter);
	}

	@Bean
	public RecordProcessor recordProcessor(SearchHitProcessorRegistry registr) {
		return new RecordProcessor(registr);
	}

	@Bean
	public FacetConverter facetConverter() {
		return new FacetConverter();
	}

	@Bean
	public SearchHitProcessorRegistry searchHitProcessorRegistry(
			List<SearchHitResultProcessor> searchHitResultProcessor) {
		return new SearchHitProcessorRegistry(searchHitResultProcessor);
	}

	@Bean
	public HighlightSearchHitResultProcessor highlightSearchHitResultProcessor() {
		return new HighlightSearchHitResultProcessor();
	}

	@Bean
	public GeoDistanceSearchHitResultProcessor geoDistanceSearchHitResultProcessor() {
		return new GeoDistanceSearchHitResultProcessor();
	}

}
