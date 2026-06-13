package com.reyco.dasbx.es.core.configuration.metadata;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.es.core.mapping.DefaultIndexManager;
import com.reyco.dasbx.es.core.mapping.DefaultMappingBuilder;
import com.reyco.dasbx.es.core.mapping.IndexManager;
import com.reyco.dasbx.es.core.mapping.MappingBuilder;
import com.reyco.dasbx.es.core.metadata.register.DefaultMetadataRegistry;
import com.reyco.dasbx.es.core.metadata.register.MetadataRegistry;
import com.reyco.dasbx.es.core.metadata.resolver.DefaultFieldResolver;

@Configuration
public class MetadataConfiguration {
	
	/***************************************Metadata相关****************************************************/
	/**
	 * 元数据注册器
	 * @return
	 */
	@Bean
	public MetadataRegistry metadataRegistry() {
		return new DefaultMetadataRegistry();
	}
	/**
	 * 属性解析器
	 * @param registry
	 * @return
	 */
	@Bean
	public DefaultFieldResolver defaultFieldResolver(MetadataRegistry registry) {
		return new DefaultFieldResolver(registry);
	}
	/***************************************mapping相关****************************************************/
	/**
	 * 索引管理器
	 * @param client
	 * @param mappingBuilder
	 * @return
	 */
	@Bean
	public IndexManager indexManager(RestHighLevelClient client,MappingBuilder mappingBuilder) {
		return new DefaultIndexManager(client, mappingBuilder);
	}
	/**
	 * 索引构建器
	 * @return
	 */
	@Bean
	public MappingBuilder mappingBuilder() {
		return new DefaultMappingBuilder();
	}
	
	
}