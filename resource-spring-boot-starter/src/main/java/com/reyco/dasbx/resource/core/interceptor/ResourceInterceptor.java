package com.reyco.dasbx.resource.core.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;

public class ResourceInterceptor extends ResourceAspectSupport implements MethodInterceptor,InitializingBean {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
		return invokeWithinRouter(invocation.getMethod(), targetClass, invocation.getArguments(), invocation::proceed);
	}
	
}
