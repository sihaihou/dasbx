package com.reyco.dasbx.constant.autoConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.constant.core.ConstantManager;
import com.reyco.dasbx.constant.core.DefaultConstantManager;
import com.reyco.dasbx.constant.properties.ConstantProperties;
import com.reyco.dasbx.constant.utils.ConstantUtils;

@Configuration
@ConditionalOnProperty(name="reyco.dasbx.constants.enabled",matchIfMissing=true)
@EnableConfigurationProperties(ConstantProperties.class)
public class ConstantConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(ConstantProperties.class)
	public ConstantProperties constantProperties() {
		return new ConstantProperties();
	}
	
	@Bean
	@ConditionalOnBean(ConstantProperties.class)
	@ConditionalOnMissingBean(ConstantManager.class)
	public ConstantManager constantManager(ConstantProperties constantProperties) {
		return new DefaultConstantManager(constantProperties);
	}
	
	@Bean
	@ConditionalOnBean(ConstantManager.class)
	@ConditionalOnMissingBean(ConstantUtils.class)
	public ConstantUtils constantUtils(ConstantManager constantManager) {
		return new ConstantUtils(constantManager);
	}
}
