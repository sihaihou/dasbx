package com.reyco.dasbx.es.core.configuration.validator;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.es.core.configuration.metadata.MetadataConfiguration;
import com.reyco.dasbx.es.core.metadata.register.MetadataRegistry;
import com.reyco.dasbx.es.core.pipeline.validator.QueryValidatorPipeline;
import com.reyco.dasbx.es.core.validator.AggregationValidator;
import com.reyco.dasbx.es.core.validator.PageValidator;
import com.reyco.dasbx.es.core.validator.QueryValidator;
import com.reyco.dasbx.es.core.validator.SearchAfterValidator;
import com.reyco.dasbx.es.core.validator.SortValidator;

@Configuration
@AutoConfigureAfter(MetadataConfiguration.class)
public class ValidatorConfiguration {
	
	@Bean
	public QueryValidatorPipeline queryValidatorPipeline(List<QueryValidator> validators){
		return new QueryValidatorPipeline(validators);
	}
	@Bean
	public PageValidator pageValidator() {
		return new PageValidator();
	}
	@Bean
	public SearchAfterValidator searchAfterValidator() {
		return new SearchAfterValidator();
	}
	@Bean
	public SortValidator sortValidator(MetadataRegistry registry) {
		return new SortValidator(registry);
	}
	@Bean
	public AggregationValidator aggregationValidator(MetadataRegistry registry) {
		return new AggregationValidator(registry);
	}
	
}
