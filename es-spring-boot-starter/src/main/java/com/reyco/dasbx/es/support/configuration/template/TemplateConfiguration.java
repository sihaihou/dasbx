package com.reyco.dasbx.es.support.configuration.template;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.es.core.query.builder.factory.SearchBuilderFactory;
import com.reyco.dasbx.es.core.query.suggest.SuggestService;
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
	public SearchTemplate searchTemplate(SearchBuilderFactory factory, ResultProcessor resultProcessor, SuggestService suggestService) {
		return new SearchTemplate(factory, resultProcessor, suggestService);
	}

	@Bean
	public ResultProcessor resultProcessor(RecordProcessor recordProcessor,FacetConverter facetConverter) {
		return new ResultProcessor(recordProcessor,facetConverter);
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
	public SearchHitProcessorRegistry searchHitProcessorRegistry(List<SearchHitResultProcessor> searchHitResultProcessor) {
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
