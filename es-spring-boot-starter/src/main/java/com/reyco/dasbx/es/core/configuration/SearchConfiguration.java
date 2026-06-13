package com.reyco.dasbx.es.core.configuration;

import java.util.List;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration.RestHighLevelClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.reyco.dasbx.es.client.ElasticsearchClient;
import com.reyco.dasbx.es.core.engine.SearchEngine;
import com.reyco.dasbx.es.core.feature.aggregation.parser.AggregationResultParser;
import com.reyco.dasbx.es.core.feature.interceptor.SearchRequestInterceptorChain;
import com.reyco.dasbx.es.core.log.DefaultDslLogger;
import com.reyco.dasbx.es.core.log.DslLogger;
import com.reyco.dasbx.es.core.pipeline.SearchPipeline;
import com.reyco.dasbx.es.core.pipeline.feature.HighlightPipeline;
import com.reyco.dasbx.es.core.pipeline.query.QueryPipeline;
import com.reyco.dasbx.es.core.pipeline.query.SourcePipeline;
import com.reyco.dasbx.es.core.query.builder.executor.ElasticsearchSearchExecutor;
import com.reyco.dasbx.es.core.query.builder.executor.SearchExecutor;
import com.reyco.dasbx.es.core.query.builder.factory.DefaultSearchBuilderFactory;
import com.reyco.dasbx.es.core.query.builder.factory.SearchBuilderFactory;
import com.reyco.dasbx.es.core.query.condition.adapter.ConditionAdapterRegistry;
import com.reyco.dasbx.es.core.query.suggest.ElasticsearchSuggestService;
import com.reyco.dasbx.es.core.query.suggest.SuggestService;
import com.reyco.dasbx.es.core.result.mapper.DefaultResultMapper;
import com.reyco.dasbx.es.core.result.mapper.ResultMapper;
import com.reyco.dasbx.es.core.result.parser.DefaultSearchHitParser;
import com.reyco.dasbx.es.core.result.parser.SearchHitParser;
import com.reyco.dasbx.es.core.result.processor.GeoDistanceProcessor;
import com.reyco.dasbx.es.core.result.processor.HighlightProcessor;
import com.reyco.dasbx.es.core.result.processor.SearchHitProcessor;

@Configuration
@ConditionalOnProperty(name="reyco.dasbx.es.enabled",matchIfMissing=true)
@AutoConfigureAfter(value= {RestClientAutoConfiguration.class,RestHighLevelClientConfiguration.class})
public class SearchConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(ElasticsearchClient.class)
	public ElasticsearchClient<?> elasticsearchClient(RestHighLevelClient restHighLevelClient) {
		ElasticsearchClient<?> elasticsearchClient = new ElasticsearchClient();
		elasticsearchClient.setRestHighLevelClient(restHighLevelClient);
		return elasticsearchClient;
	}
	
	@Bean
	public QueryPipeline queryPipeline(ConditionAdapterRegistry registry) {
		return new QueryPipeline(registry);
	}
	
	@Bean
	public SourcePipeline sourcePipeline() {
		return new SourcePipeline();
	}
	/**
	 * 高亮
	 * @return
	 */
	@Bean
	@Order(60)
	public HighlightPipeline highlightPipeline(){
	    return new HighlightPipeline();
	}
	
	@Bean
	public DslLogger logger() {
		return new DefaultDslLogger();
	}
	
	/**
	 * Executor
	 */
	@Bean
	public SearchExecutor searchExecutor(RestHighLevelClient client,SearchRequestInterceptorChain chain,DslLogger logger) {
		return new ElasticsearchSearchExecutor(client,chain, logger);
	}
	
	/**
	 * Engine
	 */
	@Bean
	public SearchEngine searchEngine(List<SearchPipeline> pipelines, SearchExecutor executor) {
		return new SearchEngine(pipelines,executor);
	}
	
	/**
	 * SearchHitParser
	 */
	@Bean
	public SearchHitParser searchHitParser(List<SearchHitProcessor> processors) {
		return new DefaultSearchHitParser(processors);
	}
	/**
	 * ResultMapper
	 */
	@Bean
	public ResultMapper resultMapper(AggregationResultParser aggregationParser,SearchHitParser searchHitParser) {
		return new DefaultResultMapper(aggregationParser,searchHitParser);
	}
	
	/**
	 * SearchBuilderFactory
	 */
	@Bean
	public SearchBuilderFactory searchBuilderFactory(SearchEngine engine, ResultMapper mapper) {
		return new DefaultSearchBuilderFactory(engine, mapper);
	}
	
	
	@Bean
	public GeoDistanceProcessor geoDistanceProcessor() {
		return new GeoDistanceProcessor();
	}
	@Bean
	public HighlightProcessor highlightProcessor() {
		return new HighlightProcessor();
	}
	
	/**
	 * Suggest
	 * @param client
	 * @return
	 */
	@Bean
	public SuggestService suggestService(RestHighLevelClient client) {
		return new ElasticsearchSuggestService(client);
	}

}
