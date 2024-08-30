package com.reyco.dasbx.oss.autoConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.oss.core.DefaultOssServiceImpl;
import com.reyco.dasbx.oss.core.OssService;
import com.reyco.dasbx.oss.properties.OssProperties;

@Configuration
@ConditionalOnProperty(name="reyco.dasbx.oss.enabled",matchIfMissing=true)
@EnableConfigurationProperties(OssProperties.class)
public class OssConfiguration {
	@Bean
	@ConditionalOnMissingBean(OssProperties.class)
	public OssProperties ossProperties() {
		return new OssProperties();
	}
	@Bean
	@ConditionalOnMissingBean(OssService.class)
	public OssService ossService(OssProperties ossProperties) {
		DefaultOssServiceImpl ossServiceImpl = new DefaultOssServiceImpl();
		ossServiceImpl.setOssProperties(ossProperties);
		return ossServiceImpl;
	}
	
}
