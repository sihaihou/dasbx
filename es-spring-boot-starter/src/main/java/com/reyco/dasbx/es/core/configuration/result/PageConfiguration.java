package com.reyco.dasbx.es.core.configuration.result;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.es.core.pipeline.result.PagePipeline;
import com.reyco.dasbx.es.core.result.strategy.PageStrategy;
import com.reyco.dasbx.es.core.result.strategy.PageStrategyRegistry;

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
	
}
