package com.reyco.dasbx.es.core.configuration.feature;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.reyco.dasbx.es.core.feature.aggregation.factory.AggregationBuilderFactory;
import com.reyco.dasbx.es.core.feature.aggregation.handler.AggregationHandler;
import com.reyco.dasbx.es.core.feature.aggregation.handler.AggregationHandlerRegistry;
import com.reyco.dasbx.es.core.feature.aggregation.handler.AvgAggregationHandler;
import com.reyco.dasbx.es.core.feature.aggregation.handler.MaxAggregationHandler;
import com.reyco.dasbx.es.core.feature.aggregation.handler.MinAggregationHandler;
import com.reyco.dasbx.es.core.feature.aggregation.handler.NestedAggregationHandler;
import com.reyco.dasbx.es.core.feature.aggregation.handler.SumAggregationHandler;
import com.reyco.dasbx.es.core.feature.aggregation.handler.TermsAggregationHandler;
import com.reyco.dasbx.es.core.feature.aggregation.parser.AggregationResultParser;
import com.reyco.dasbx.es.core.pipeline.feature.AggregationPipeline;

@Configuration
public class AggregationConfiguration {
	/**
	 * 聚合构建
	 * @param factory
	 * @return
	 */
	@Bean
	@Order(50)
	public AggregationPipeline aggregationPipeline(AggregationBuilderFactory factory) {
		return new AggregationPipeline(factory);
	}
	
	@Bean
	public AggregationBuilderFactory aggregationBuilderFactory(AggregationHandlerRegistry registry) {
		return new AggregationBuilderFactory(registry);
	}
	
	@Bean
	public AggregationResultParser aggregationResultParser(AggregationHandlerRegistry registry) {
		return new AggregationResultParser(registry);
	}
	
	@Bean
	public AggregationHandlerRegistry aggregationHandlerRegistry(List<AggregationHandler> handlers) {
		return new AggregationHandlerRegistry(handlers);
	}
	
	/**
	 * 聚合处理器
	 * @return
	 */
	@Bean
	public AvgAggregationHandler avgAggregationHandler(){
		return new AvgAggregationHandler();
	}
	@Bean
	public MaxAggregationHandler maxAggregationHandler(){
		return new MaxAggregationHandler();
	}
	@Bean
	public MinAggregationHandler minAggregationHandler(){
		return new MinAggregationHandler();
	}
	@Bean
	public SumAggregationHandler sumAggregationHandler(){
		return new SumAggregationHandler();
	}
	@Bean
	public TermsAggregationHandler termsAggregationHandler(){
		return new TermsAggregationHandler();
	}
	@Bean
	public NestedAggregationHandler nestedAggregationHandler(){
		return new NestedAggregationHandler();
	}
}
