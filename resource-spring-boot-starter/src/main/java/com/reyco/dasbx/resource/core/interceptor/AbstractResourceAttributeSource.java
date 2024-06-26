package com.reyco.dasbx.resource.core.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.springframework.aop.support.AopUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

public abstract class AbstractResourceAttributeSource implements ResourceAttributeSource {

	@Override
	public ResourceAttribute getResourceAttribute(Method method, Class<?> targetClass) {
		if (method.getDeclaringClass() == Object.class) {
			return null;
		}
		return computeResourceAttribute(method, targetClass);
	}

	protected ResourceAttribute computeResourceAttribute(Method method, @Nullable Class<?> targetClass) {
		if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
			return null;
		}
		Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
		ResourceAttribute txAttr = findResourceAttribute(specificMethod);
		if (txAttr != null) {
			return txAttr;
		}
		txAttr = findResourceAttribute(specificMethod.getDeclaringClass());
		if (txAttr != null && ClassUtils.isUserLevelMethod(method)) {
			return txAttr;
		}
		if (specificMethod != method) {
			txAttr = findResourceAttribute(method);
			if (txAttr != null) {
				return txAttr;
			}
			txAttr = findResourceAttribute(method.getDeclaringClass());
			if (txAttr != null && ClassUtils.isUserLevelMethod(method)) {
				return txAttr;
			}
		}
		return null;
	}

	protected abstract ResourceAttribute findResourceAttribute(Class<?> declaringClass);

	protected abstract ResourceAttribute findResourceAttribute(Method method);

	protected boolean allowPublicMethodsOnly() {
		return false;
	}
}
