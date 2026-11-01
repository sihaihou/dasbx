package com.reyco.dasbx.es.core.configuration.result;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.es.core.pipeline.result.PagePipeline;
import com.reyco.dasbx.es.core.result.strategy.OffsetPageStrategy;
import com.reyco.dasbx.es.core.result.strategy.PageStrategy;
import com.reyco.dasbx.es.core.result.strategy.PageStrategyRegistry;
import com.reyco.dasbx.es.core.result.strategy.ScrollPageStrategy;
import com.reyco.dasbx.es.core.result.strategy.SearchAfterPageStrategy;

@Configuration
public class PageConfiguration {
	
	@Bean
	public PagePipeline pagePipeline(PageStrategyRegistry registry) {
		return new PagePipeline(registry);
	}
	
	@Bean
	public PageStrategyRegistry pageStrategyRegistry(List<PageStrategy<?>> pageStrategies){
		return new PageStrategyRegistry(pageStrategies);
	}
	
	@Bean
	public OffsetPageStrategy offsetPageStrategy() {
		return new OffsetPageStrategy();
	}
	
	@Bean
	public ScrollPageStrategy scrollPageStrategy() {
		return new ScrollPageStrategy();
	}
	
	@Bean
	public SearchAfterPageStrategy searchAfterPageStrategy() {
		return new SearchAfterPageStrategy();
	}
}
