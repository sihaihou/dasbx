package com.reyco.dasbx.resource.core.interceptor;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.reyco.dasbx.resource.core.parser.DefaultResourceAnnotationParser;
import com.reyco.dasbx.resource.core.parser.ResourceAnnotationParser;

public class AnnotationResourceAttributeSource extends AbstractResourceAttributeSource {

	private final boolean publicMethodsOnly;

	private final Set<ResourceAnnotationParser> annotationParsers;

	public AnnotationResourceAttributeSource() {
		this(true);
	}

	public AnnotationResourceAttributeSource(boolean publicMethodsOnly) {
		this.publicMethodsOnly = publicMethodsOnly;
		this.annotationParsers = Collections.singleton(new DefaultResourceAnnotationParser());
	}

	public AnnotationResourceAttributeSource(ResourceAnnotationParser annotationParser) {
		this.publicMethodsOnly = true;
		Assert.notNull(annotationParser, "ResourceAnnotationParser must not be null");
		this.annotationParsers = Collections.singleton(annotationParser);
	}
	public AnnotationResourceAttributeSource(ResourceAnnotationParser... annotationParsers) {
		this.publicMethodsOnly = true;
		Assert.notEmpty(annotationParsers, "At least one ResourceAnnotationParser needs to be specified");
		this.annotationParsers = new LinkedHashSet<>(Arrays.asList(annotationParsers));
	}
	public AnnotationResourceAttributeSource(Set<ResourceAnnotationParser> annotationParsers) {
		this.publicMethodsOnly = true;
		Assert.notEmpty(annotationParsers, "At least one ResourceAnnotationParser needs to be specified");
		this.annotationParsers = annotationParsers;
	}
	@Override
	protected ResourceAttribute findResourceAttribute(Class<?> clazz) {
		return determineResourceAttribute(clazz);
	}

	@Override
	protected ResourceAttribute findResourceAttribute(Method method) {
		return determineResourceAttribute(method);
	}
	@Nullable
	protected ResourceAttribute determineResourceAttribute(AnnotatedElement element) {
		for (ResourceAnnotationParser annotationParser : this.annotationParsers) {
			ResourceAttribute attr = annotationParser.parseResourceAnnotation(element);
			if (attr != null) {
				return attr;
			}
		}
		return null;
	}
	@Override
	protected boolean allowPublicMethodsOnly() {
		return this.publicMethodsOnly;
	}
}
