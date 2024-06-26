package com.reyco.dasbx.resource.core.parser;

import java.lang.reflect.AnnotatedElement;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import com.reyco.dasbx.resource.annotation.Resource;
import com.reyco.dasbx.resource.core.interceptor.DefaultResourceAttribute;
import com.reyco.dasbx.resource.core.interceptor.ResourceAttribute;

public class DefaultResourceAnnotationParser implements ResourceAnnotationParser {

	@Override
	public ResourceAttribute parseResourceAnnotation(AnnotatedElement element) {
		AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(element, Resource.class, false, false);
		if (attributes != null) {
			return parseReycoResourceAttribute(attributes);
		}else {
			return null;
		}
	}
	
	private ResourceAttribute parseReycoResourceAttribute(AnnotationAttributes attributes) {
		DefaultResourceAttribute dsa = new DefaultResourceAttribute();
		String name = attributes.getString("name");
		String value = attributes.getString("value");
		String resource = attributes.getString("resource");
		dsa.setName(name);
		dsa.setValue(value);
		dsa.setResource(resource);
		return dsa;
	}
}
