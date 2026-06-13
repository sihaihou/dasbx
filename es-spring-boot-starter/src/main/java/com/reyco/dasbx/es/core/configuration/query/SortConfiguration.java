package com.reyco.dasbx.es.core.configuration.query;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.es.core.metadata.register.MetadataRegistry;
import com.reyco.dasbx.es.core.pipeline.query.SortPipeline;
import com.reyco.dasbx.es.core.query.geo.sort.GeoDistanceSortStrategy;
import com.reyco.dasbx.es.core.query.sort.SortDefinition;
import com.reyco.dasbx.es.core.query.sort.strategy.FieldSortStrategy;
import com.reyco.dasbx.es.core.query.sort.strategy.ScoreSortStrategy;
import com.reyco.dasbx.es.core.query.sort.strategy.ScriptSortStrategy;
import com.reyco.dasbx.es.core.query.sort.strategy.SortStrategy;
import com.reyco.dasbx.es.core.query.sort.strategy.SortStrategyRegistry;

@Configuration
public class SortConfiguration {

	@Bean
	public SortPipeline sortPipeline(SortStrategyRegistry registry) {
		return new SortPipeline(registry);
	}

	@Bean
	public SortStrategyRegistry sortStrategyRegistry(List<SortStrategy<? extends SortDefinition>> strategies) {
		SortStrategyRegistry registry = new SortStrategyRegistry(strategies);
		return registry;
	}

	/**
	 * 排序
	 * @param registry
	 * @return
	 */
	@Bean
	public FieldSortStrategy fieldSortStrategy(MetadataRegistry registry) {
		return new FieldSortStrategy(registry);
	}

	@Bean
	public ScoreSortStrategy scoreSortStrategy() {
		return new ScoreSortStrategy();
	}

	@Bean
	public ScriptSortStrategy scriptSortStrategy() {
		return new ScriptSortStrategy();
	}

	@Bean
	public GeoDistanceSortStrategy geoDistanceSortStrategy() {
		return new GeoDistanceSortStrategy();
	}
}
