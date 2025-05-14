package com.reyco.dasbx.desensitize.autoConfiguration;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.reyco.dasbx.desensitize.core.handler.DefaultExpressionDesensitizeHandler;
import com.reyco.dasbx.desensitize.core.handler.DelegateDesensitizeHandler;
import com.reyco.dasbx.desensitize.core.handler.DesensitizeHandler;
import com.reyco.dasbx.desensitize.core.handler.IndexDesensitizeHandler;

@Configuration
@ConditionalOnProperty(name=DesensitizeConfiguration.ENABLED,matchIfMissing=true)
@AutoConfigureAfter(SpringContextConfiguration.class)
public class DesensitizeConfiguration {
	
	public static final String ENABLED = "reyco.dasbx.desensitize.enabled";
	
	@Bean
	@Primary
	public DesensitizeHandler delegateDesensiteHandler(List<DesensitizeHandler> desensitizeHandlers){
		 DelegateDesensitizeHandler delegateDesensiteHandler = new DelegateDesensitizeHandler();
		 delegateDesensiteHandler.setDesensiteHandlers(desensitizeHandlers);
		 return delegateDesensiteHandler;
	}
	
	@Bean
	public DesensitizeHandler indexDesensitizeHandler(){
		return new IndexDesensitizeHandler();
	}
	@Bean
	public DesensitizeHandler expressionDesensitizeHandler(){
		return new DefaultExpressionDesensitizeHandler();
	}
}
