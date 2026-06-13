package com.reyco.dasbx.es.core.configuration.feature;

import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.es.core.feature.alias.AliasManager;
import com.reyco.dasbx.es.core.feature.alias.AliasProperties;
import com.reyco.dasbx.es.core.feature.alias.DefaultAliasManager;
import com.reyco.dasbx.es.core.feature.interceptor.AliasInterceptor;
import com.reyco.dasbx.es.core.feature.interceptor.RoutingInterceptor;
import com.reyco.dasbx.es.core.feature.interceptor.SearchRequestInterceptor;
import com.reyco.dasbx.es.core.feature.interceptor.SearchRequestInterceptorChain;
import com.reyco.dasbx.es.core.pipeline.feature.AliasPipeline;

@Configuration
@EnableConfigurationProperties(AliasProperties.class)
public class FeatureConfiguration {
	
	@Bean
	public AliasPipeline aliasPipeline(AliasManager aliasManager) {
		return new AliasPipeline(aliasManager);
	}
	
	@Bean
	public AliasManager aliasManager(AliasProperties aliasProperties) {
		return new DefaultAliasManager(aliasProperties);
	}
	
	/***********************************拦截器相关配置***********************************************/
	@Bean
	public SearchRequestInterceptorChain searchRequestInterceptorChain(List<SearchRequestInterceptor> interceptors) {
		SearchRequestInterceptorChain interceptorChain = new SearchRequestInterceptorChain(interceptors);
		return interceptorChain;
	}
	@Bean
	public AliasInterceptor aliasInterceptor() {
		AliasInterceptor aliasInterceptor = new AliasInterceptor();
		return aliasInterceptor;
	}
	@Bean
	public RoutingInterceptor routingInterceptor() {
		RoutingInterceptor routingInterceptor = new RoutingInterceptor();
		return routingInterceptor;
	}
	
}
