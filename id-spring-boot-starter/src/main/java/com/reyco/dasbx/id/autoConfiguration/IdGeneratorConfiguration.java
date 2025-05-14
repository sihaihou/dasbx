package com.reyco.dasbx.id.autoConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.reyco.dasbx.id.core.IdGenerator;
import com.reyco.dasbx.id.core.SnowflakeIdGenerator;
import com.reyco.dasbx.id.core.UUIdGenerator;
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
	
	@Primary
	@Bean(IdGenerator.DEFAULT_IDGENERATOR)
	@ConditionalOnBean(SnowFlakeProperties.class)
	public SnowflakeIdGenerator snowflakeIdGenerator(SnowFlakeProperties snowFlakeProperties) {
		SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(snowFlakeProperties);
		return snowflakeIdGenerator;
	}

	@Bean(IdGenerator.UUID_IDGENERATOR)
	public UUIdGenerator uuIdGenerator() {
		UUIdGenerator uuIdGenerator = new UUIdGenerator();
		return uuIdGenerator;
	}
}
