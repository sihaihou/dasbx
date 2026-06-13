package com.reyco.dasbx.es.core.configuration.query;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.es.core.pipeline.query.QueryOptimizePipeline;
import com.reyco.dasbx.es.core.query.optimize.OptimizeRule;
import com.reyco.dasbx.es.core.query.optimize.OptimizeRuleRegistry;
import com.reyco.dasbx.es.core.query.optimize.bool.BoolFlattenOptimizeRule;
import com.reyco.dasbx.es.core.query.optimize.bool.EmptyBoolOptimizeRule;
import com.reyco.dasbx.es.core.query.optimize.bool.SingleBoolOptimizeRule;
import com.reyco.dasbx.es.core.query.optimize.condition.DuplicateConditionOptimizeRule;
import com.reyco.dasbx.es.core.query.optimize.nested.NestedMergeOptimizeRule;

@Configuration
public class OptimizeConfiguration {

	@Bean
	public QueryOptimizePipeline queryOptimizePipeline(OptimizeRuleRegistry registry) {
		return new QueryOptimizePipeline(registry);
	}
	
	@Bean
	public OptimizeRuleRegistry OptimizeRuleRegistry(List<OptimizeRule> optimizers) {
		return new OptimizeRuleRegistry(optimizers);
	}
	
	@Bean
	public BoolFlattenOptimizeRule boolFlattenOptimizeRule() {
		return new BoolFlattenOptimizeRule();
	}
	@Bean
	public EmptyBoolOptimizeRule emptyBoolOptimizeRule() {
		return new EmptyBoolOptimizeRule();
	}
	@Bean
	public SingleBoolOptimizeRule singleBoolOptimizeRule() {
		return new SingleBoolOptimizeRule();
	}
	@Bean
	public DuplicateConditionOptimizeRule duplicateConditionOptimizeRule() {
		return new DuplicateConditionOptimizeRule();
	}
	@Bean
	public NestedMergeOptimizeRule nestedMergeOptimizeRule() {
		return new NestedMergeOptimizeRule();
	}
}
