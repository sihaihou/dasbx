package com.reyco.dasbx.oss.autoConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.oss.core.AliyunOssServiceImpl;
import com.reyco.dasbx.oss.core.LocalOssServiceImpl;
import com.reyco.dasbx.oss.properties.OssProperties;

@Configuration
@ConditionalOnProperty(name="reyco.dasbx.oss.enabled",matchIfMissing=true)
@EnableConfigurationProperties(OssProperties.class)
public class OssConfiguration {
	
	@Bean
	@ConditionalOnProperty(name = "reyco.dasbx.oss.type", havingValue = "local", matchIfMissing = true)
	public LocalOssServiceImpl localOssServiceImpl(OssProperties ossProperties) {
		LocalOssServiceImpl ossServiceImpl = new LocalOssServiceImpl(ossProperties);
		return ossServiceImpl;
	}
	
	@Bean
	@ConditionalOnProperty(name = "reyco.dasbx.oss.type", havingValue = "aliyun", matchIfMissing = true)
	public AliyunOssServiceImpl aliyunOssServiceImpl(OssProperties ossProperties) {
		AliyunOssServiceImpl ossServiceImpl = new AliyunOssServiceImpl(ossProperties);
		return ossServiceImpl;
	}
	
}
