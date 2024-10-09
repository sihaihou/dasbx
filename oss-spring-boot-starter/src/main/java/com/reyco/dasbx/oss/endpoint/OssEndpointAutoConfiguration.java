package com.reyco.dasbx.oss.endpoint;

import javax.xml.ws.Endpoint;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(Endpoint.class)
@ConditionalOnProperty(name="reyco.dasbx.oss.enabled",matchIfMissing=true)
public class OssEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(OssToolsIndicator.class)
	public OssToolsIndicator ossToolsIndicator() {
		OssToolsIndicator ossToolsIndicator = new OssToolsIndicator();
		return ossToolsIndicator;
	}
}
