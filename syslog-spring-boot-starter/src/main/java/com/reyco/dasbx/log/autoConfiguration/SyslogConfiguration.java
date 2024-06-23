package com.reyco.dasbx.log.autoConfiguration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.reyco.dasbx.log.autoConfiguration.SyslogConfiguration.SysLogAspectConfiguration;
import com.reyco.dasbx.log.core.SyslogAspect;
import com.reyco.dasbx.log.core.handler.CompositeSyslogHandler;
import com.reyco.dasbx.log.core.handler.SimpleSyslogHandler;
import com.reyco.dasbx.log.core.handler.SyslogHandler;
import com.reyco.dasbx.log.properties.SyslogProperties;

@Configuration
@ConditionalOnProperty(name="reyco.dasbx.syslog.enabled",matchIfMissing=true)
@ConditionalOnClass(value = {SyslogProperties.class })
@AutoConfigureBefore(SysLogAspectConfiguration.class)
public class SyslogConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(SyslogProperties.class)
	public SyslogProperties sysLogProperties() {
		SyslogProperties syslogProperties = new SyslogProperties();
		return syslogProperties;
	}
	
	@Bean
	public SyslogHandler simpleSyslogHandler() {
		return new SimpleSyslogHandler();
	}
	
	@Bean
	@Primary
	public SyslogHandler compositeSyslogPostprocessor(List<SyslogHandler> syslogPostprocessors) {
		return new CompositeSyslogHandler(syslogPostprocessors);
	}
	
	@Configuration
	@AutoConfigureAfter(SyslogConfiguration.class)
	public class SysLogAspectConfiguration{
		
		@Value("${spring.cloud.nacos.discovery.service:${spring.application.name:}}")
		private String applicationName;
		
		@Bean
		@ConditionalOnMissingBean(SyslogAspect.class)
		public SyslogAspect SysLogAspect(SyslogHandler syslogHandler,SyslogProperties syslogProperties) {
			SyslogAspect syslogAspect = new SyslogAspect();
			syslogAspect.setSyslogHandler(syslogHandler);
			syslogAspect.setSyslogProperties(syslogProperties);
			syslogAspect.setApplicationName(applicationName);
			return syslogAspect;
		}
	}
}
