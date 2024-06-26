package com.reyco.dasbx.resource.core.interceptor;

import java.lang.reflect.Method;

import org.springframework.lang.Nullable;

public interface ResourceAttributeSource {
	
	ResourceAttribute getResourceAttribute(Method method, @Nullable Class<?> targetClass);
	
}
