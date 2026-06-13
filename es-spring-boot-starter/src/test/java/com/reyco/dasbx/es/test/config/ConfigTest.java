package com.reyco.dasbx.es.test.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.es.core.configuration.SearchConfiguration;
import com.reyco.dasbx.es.core.configuration.feature.AggregationConfiguration;
import com.reyco.dasbx.es.core.configuration.feature.FeatureConfiguration;
import com.reyco.dasbx.es.core.configuration.metadata.MetadataConfiguration;
import com.reyco.dasbx.es.core.configuration.query.GeoConfiguration;
import com.reyco.dasbx.es.core.configuration.query.RewriteConfiguration;
import com.reyco.dasbx.es.core.configuration.query.SearchAdapterConfiguration;
import com.reyco.dasbx.es.core.configuration.query.SortConfiguration;
import com.reyco.dasbx.es.core.configuration.result.PageConfiguration;
import com.reyco.dasbx.es.core.configuration.validator.ValidatorConfiguration;
import com.reyco.dasbx.es.core.metadata.FieldMetadata;
import com.reyco.dasbx.es.core.metadata.IndexMetadata;
import com.reyco.dasbx.es.core.metadata.register.MetadataRegistry;
import com.reyco.dasbx.es.support.configuration.template.TemplateConfiguration;
import com.reyco.dasbx.es.test.domain.Personage;

@Configuration
public class ConfigTest {
	
	@Bean
	public SearchConfiguration searchConfiguration() {
		return new SearchConfiguration();
	}
	@Bean
	public SearchAdapterConfiguration searchAdapterConfiguration() {
		return new SearchAdapterConfiguration();
	}
	
	@Bean
	public FeatureConfiguration featureConfiguration() {
		return new FeatureConfiguration();
	}
	
	@Bean
	public PageConfiguration pageConfiguration() {
		return new PageConfiguration();
	}

	@Bean
	public MetadataConfiguration MetadataConfiguration() {
		return new MetadataConfiguration();
	}
	
	@Bean
	public SortConfiguration sortConfiguration() {
		return new SortConfiguration();
	}
	
	@Bean
	public GeoConfiguration geoConfiguration() {
		return new GeoConfiguration();
	}

	@Bean
	public AggregationConfiguration aggregationConfiguration() {
		return new AggregationConfiguration();
	}

	@Bean
	public RewriteConfiguration RewriteConfiguration() {
		return new RewriteConfiguration();
	}
	@Bean
	public ValidatorConfiguration validatorConfiguration() {
		return new ValidatorConfiguration();
	}
	@Bean
	public TemplateConfiguration templateConfiguration() {
		return new TemplateConfiguration();
	}
	/**
	 * 注册一个IndexMetadata
	 * <pre>
	 * "name": {
	 *	  "type": "text",
	 *	  "analyzer": "text_analyzer",
	 *	  "search_analyzer": "ik_smart",
	 *	  "copy_to": "all",
	 *	  "fields": {
	 *	    "keyword": {
	 *	      "type": "keyword"
	 *	    }
	 *	  }
	 *	}
	 * @param registry
	 * @return
	 */
	//@Bean
	public CommandLineRunner metadataInit(MetadataRegistry registry) {
		return args -> {
			IndexMetadata metadata = new IndexMetadata();
			metadata.setEntityClass(Personage.class);
			metadata.addField(
					new FieldMetadata()
							.setProperty("name")
							.setField("name")
							.setKeywordField("name.keyword"));
			registry.register(metadata);
		};
	}

}
