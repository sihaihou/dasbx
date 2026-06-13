package com.reyco.dasbx.es.core.configuration.query;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.es.core.query.condition.adapter.BoolConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.ConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.ConditionAdapterRegistry;
import com.reyco.dasbx.es.core.query.condition.adapter.ExistsConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.FuzzyConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.IdsConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.MatchConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.MultiMatchConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.NestedConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.PrefixConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.RangeConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.RegexpConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.TermConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.TermsConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.WildcardConditionAdapter;
import com.reyco.dasbx.es.core.query.score.adapter.FieldValueFactorAdapter;
import com.reyco.dasbx.es.core.query.score.adapter.FunctionAdapter;
import com.reyco.dasbx.es.core.query.score.adapter.FunctionAdapterRegistry;
import com.reyco.dasbx.es.core.query.score.adapter.HotScoreAdapter;
import com.reyco.dasbx.es.core.query.score.adapter.RandomScoreAdapter;
import com.reyco.dasbx.es.core.query.score.adapter.ScriptScoreAdapter;
import com.reyco.dasbx.es.core.query.score.adapter.WeightFunctionAdapter;
import com.reyco.dasbx.es.core.query.score.condition.FunctionScoreConditionAdapter;

@Configuration
public class SearchAdapterConfiguration {
	
	/********************************************ConditionAdapter相关*************************************************/
	@Bean
	public ConditionAdapterRegistry conditionAdapterRegistry(List<ConditionAdapter<?>> adapters) {
		ConditionAdapterRegistry registry = new ConditionAdapterRegistry(adapters);
		return registry;
	}

	@Bean
	public MatchConditionAdapter matchConditionAdapter() {
		return new MatchConditionAdapter();
	}
	@Bean
	public MultiMatchConditionAdapter multiMatchConditionAdapter() {
		return new MultiMatchConditionAdapter();
	}
	@Bean
	public TermConditionAdapter termConditionAdapter() {
		return new TermConditionAdapter();
	}

	@Bean
	public TermsConditionAdapter termsConditionAdapter() {
		return new TermsConditionAdapter();
	}

	@Bean
	public RangeConditionAdapter rangeConditionAdapter() {
		return new RangeConditionAdapter();
	}

	@Bean
	public ExistsConditionAdapter existsConditionAdapter() {
		return new ExistsConditionAdapter();
	}

	@Bean
	public WildcardConditionAdapter wildcardConditionAdapter() {
		return new WildcardConditionAdapter();
	}

	@Bean
	public BoolConditionAdapter boolConditionAdapter() {
		return new BoolConditionAdapter();
	}

	@Bean
	public NestedConditionAdapter nestedConditionAdapter() {
		return new NestedConditionAdapter();
	}
	
	@Bean
	public FuzzyConditionAdapter fuzzyConditionAdapter() {
		return new FuzzyConditionAdapter();
	}
	@Bean
	public IdsConditionAdapter idsConditionAdapter() {
		return new IdsConditionAdapter();
	}
	@Bean
	public PrefixConditionAdapter prefixConditionAdapter() {
		return new PrefixConditionAdapter();
	}
	@Bean
	public RegexpConditionAdapter regexpConditionAdapter() {
		return new RegexpConditionAdapter();
	}
	@Bean
	public FunctionScoreConditionAdapter functionScoreConditionAdapter(FunctionAdapterRegistry registry) {
		return new FunctionScoreConditionAdapter(registry);
	}
	
	
	
	
	
	/********************************************FunctionAdapter相关*************************************************/
	@Bean
	public FunctionAdapterRegistry functionAdapterRegistry(List<FunctionAdapter<?>> adapters) {
		 FunctionAdapterRegistry registry = new FunctionAdapterRegistry(adapters);
		 return registry;
	}
	
	@Bean
	public FieldValueFactorAdapter FieldValueFactorAdapter() {
		return new FieldValueFactorAdapter();
	}
	
	@Bean
	public RandomScoreAdapter randomScoreAdapter() {
		return new RandomScoreAdapter();
	}
	
	@Bean
	public ScriptScoreAdapter scriptScoreAdapter() {
		return new ScriptScoreAdapter();
	}
	
	@Bean
	public WeightFunctionAdapter weightFunctionAdapter() {
		return new WeightFunctionAdapter();
	}
	
	@Bean
	public HotScoreAdapter hotScoreAdapter() {
		return new HotScoreAdapter();
	}
}
