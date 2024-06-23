package com.reyco.dasbx.config.exception;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.config.exception.core.strategy.ArgumentExceptionStrategy;
import com.reyco.dasbx.config.exception.core.strategy.AuthenticationExceptionStrategy;
import com.reyco.dasbx.config.exception.core.strategy.BusinessExceptionStrategy;
import com.reyco.dasbx.config.exception.core.strategy.DelegationExceptionStrategy;

@Configuration
@ConditionalOnProperty(name="reyco.dasbx.exception.enabled",matchIfMissing=true)
public class ExeptionConfiguartion {
	
	/**
	 * 策略委托类---->入口类
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(DelegationExceptionStrategy.class)
	public DelegationExceptionStrategy delegationExceptionStrategy() {
		return new DelegationExceptionStrategy();
	}
	
	/**
	 * 三个策略类--->真正的异常处理类
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(ArgumentExceptionStrategy.class)
	public ArgumentExceptionStrategy argumentExceptionStrategy() {
		return new ArgumentExceptionStrategy();
	}
	@Bean
	@ConditionalOnMissingBean(AuthenticationExceptionStrategy.class)
	public AuthenticationExceptionStrategy authenticationExceptionStrategy() {
		return new AuthenticationExceptionStrategy();
	}
	@Bean
	@ConditionalOnMissingBean(BusinessExceptionStrategy.class)
	public BusinessExceptionStrategy businessExceptionStrategy() {
		return new BusinessExceptionStrategy();
	}
	
	
	
}
