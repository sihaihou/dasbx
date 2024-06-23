package com.reyco.dasbx.es.autoconfiguration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration.RestHighLevelClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.es.core.client.ElasticsearchClient;

@Configuration
@ConditionalOnProperty(name="reyco.dasbx.es.enabled",matchIfMissing=true)
@AutoConfigureAfter(value= {RestClientAutoConfiguration.class,RestHighLevelClientConfiguration.class})
public class ElasticsearchConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(ElasticsearchClient.class)
	public ElasticsearchClient<?> elasticsearchClient(RestHighLevelClient restHighLevelClient) {
		ElasticsearchClient<?> elasticsearchClient = new ElasticsearchClient();
		elasticsearchClient.setRestHighLevelClient(restHighLevelClient);
		return elasticsearchClient;
	}
	
}
