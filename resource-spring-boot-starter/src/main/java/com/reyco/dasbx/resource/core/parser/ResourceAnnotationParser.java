package com.reyco.dasbx.resource.core.parser;

import java.lang.reflect.AnnotatedElement;

import com.reyco.dasbx.resource.core.interceptor.ResourceAttribute;

public interface ResourceAnnotationParser {
	
	ResourceAttribute parseResourceAnnotation(AnnotatedElement element);
	
}
