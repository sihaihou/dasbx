package com.reyco.dasbx.resource.core.interceptor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractGenericPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;

public class ResourceJdkRegexpAdvisor extends AbstractGenericPointcutAdvisor{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4309515527478988349L;
	
	private JdkRegexpMethodPointcut jdkRegexpMethodPointcut = new JdkRegexpMethodPointcut();
	
	public void setPatterns(String... patterns) {
		jdkRegexpMethodPointcut.setPatterns(patterns);
	}
	public void setExcludedPatterns(String... excludedPatterns) {
		jdkRegexpMethodPointcut.setExcludedPatterns(excludedPatterns);
	}
	
	@Override
	public Pointcut getPointcut() {
		return jdkRegexpMethodPointcut;
	}

}
