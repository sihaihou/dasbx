package com.reyco.dasbx.desensitize.autoConfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.desensitize.utils.SpringContextUtils;

@Configuration
@AutoConfigureBefore(DesensitizeConfiguration.class)
public class SpringContextConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public SpringContextUtils SpringContextUtils() {
		return new SpringContextUtils();
	}
}
