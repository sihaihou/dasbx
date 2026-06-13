package com.reyco.dasbx.es.core.configuration.query;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.es.core.metadata.resolver.FieldResolver;
import com.reyco.dasbx.es.core.pipeline.query.QueryRewritePipeline;
import com.reyco.dasbx.es.core.query.rewirte.AggregationRewrite;
import com.reyco.dasbx.es.core.query.rewirte.QueryRewrite;
import com.reyco.dasbx.es.core.query.rewirte.QueryRewriteRegistry;
import com.reyco.dasbx.es.core.query.rewirte.SortRewrite;

@Configuration
public class RewriteConfiguration {
	
	@Bean
	public QueryRewritePipeline QueryRewritePipeline(QueryRewriteRegistry registry) {
		return new QueryRewritePipeline(registry);
	}
	@Bean
	public QueryRewriteRegistry queryRewriteRegistry(List<QueryRewrite<?>> rewrites) {
		return new QueryRewriteRegistry(rewrites);
	}
	
	@Bean
	public SortRewrite sortRewrite(FieldResolver fieldResolver) {
		return new SortRewrite(fieldResolver);
	}
	@Bean
	public AggregationRewrite aggregationRewrite(FieldResolver fieldResolver) {
		return new AggregationRewrite(fieldResolver);
	}
	
}
