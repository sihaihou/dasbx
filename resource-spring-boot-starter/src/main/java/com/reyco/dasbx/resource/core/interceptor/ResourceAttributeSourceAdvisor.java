package com.reyco.dasbx.resource.core.interceptor;

import java.lang.reflect.Method;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

public class ResourceAttributeSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2466833545197206446L;

	private ResourceAttributeSource resourceAttributeSource;
	
	private final StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
		@Override
		public boolean matches(Method method, Class<?> targetClass) {
			ResourceAttributeSource sas = getResourceAttributeSource();
			return (sas == null || sas.getResourceAttribute(method, targetClass) != null);
		}
		public ResourceAttributeSource getResourceAttributeSource() {
			return resourceAttributeSource;
		}
	};
	public void setRouterAttributeSource(ResourceAttributeSource resourceAttributeSource) {
		this.resourceAttributeSource = resourceAttributeSource;
	}
	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

}
