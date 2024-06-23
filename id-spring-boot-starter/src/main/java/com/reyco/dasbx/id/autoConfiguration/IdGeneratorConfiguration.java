package com.reyco.dasbx.id.autoConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.id.core.IdGenerator;
import com.reyco.dasbx.id.core.SnowflakeIdGenerator;
import com.reyco.dasbx.id.properties.SnowFlakeProperties;

@Configuration
@ConditionalOnProperty(name="reyco.dasbx.id.enabled",matchIfMissing=true)
@EnableConfigurationProperties(SnowFlakeProperties.class)
public class IdGeneratorConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(SnowFlakeProperties.class)
	public SnowFlakeProperties snowFlakeProperties() {
		SnowFlakeProperties snowFlakeProperties = new SnowFlakeProperties();
		return snowFlakeProperties;
	}
	@Bean
	@ConditionalOnBean(SnowFlakeProperties.class)
	@ConditionalOnMissingBean(IdGenerator.class)
	public IdGenerator<Long> idGenerator(SnowFlakeProperties snowFlakeProperties) {
		SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(snowFlakeProperties);
		return snowflakeIdGenerator;
	}
}
