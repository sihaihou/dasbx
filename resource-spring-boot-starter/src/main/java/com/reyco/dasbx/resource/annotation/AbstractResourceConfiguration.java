package com.reyco.dasbx.resource.annotation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.reyco.dasbx.resource.constant.ResourceMode;
import com.reyco.dasbx.resource.core.handler.ResourceHandler;

public abstract class AbstractResourceConfiguration implements ImportAware {
	
	protected ResourceHandler resourceHandler;
	
	protected AnnotationAttributes enableAttributes;

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		this.enableAttributes = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableResource.class.getName(), false));
		if (this.enableAttributes == null) {
			throw new IllegalArgumentException("@EnableResource is not present on importing class " + importMetadata.getClassName());
		}
	}
	@Autowired
	void setResourceHandler(ResourceHandler resourceHandler) {
		this.resourceHandler = resourceHandler;
	}
	/**
	 * get Expression
	 * @return
	 */
	protected String getExpression() {
		ResourceMode resourceMode = getResourceMode();
		String expression = null;
		if(resourceMode==ResourceMode.EXPRESSION) {
			expression = (String)enableAttributes.get("expression");
			if(StringUtils.isBlank(expression)) {
				expression = (String)enableAttributes.get("value");
			}
			if(StringUtils.isBlank(expression)) {
				throw new IllegalArgumentException("ExpressionMode expression are not allowed to be null");
			}
		}
		return expression;
	}
	/**
	 * get patterns 
	 * @return
	 */
	protected String[] getPatterns() {
		ResourceMode resourceMode = getResourceMode();
		if(resourceMode==ResourceMode.REGULAR) {
			 String[] patterns = (String[])enableAttributes.get("patterns");
			 if(isArrayBlank(patterns)) {
				 throw new IllegalArgumentException("RegularMode patterns are not allowed to be null");
			 }
			 return patterns;
		}
		return null;
	}
	/**
	 * get ExcludedPatterns
	 * @return
	 */
	protected String[] getExcludedPatterns() {
		ResourceMode resourceMode = getResourceMode();
		if(resourceMode==ResourceMode.REGULAR) {
			return (String[])enableAttributes.get("excludedPatterns");
		}
		return null;
	}
	protected ResourceMode getResourceMode() {
		return (ResourceMode)enableAttributes.get("resourceMode");
	}
	/**
	 * get order
	 * @return
	 */
	protected int getOrder() {
		return (int)enableAttributes.get("order");
	}
	/**
	 * 是否为null
	 * @param arrays
	 * @return
	 */
	protected boolean isArrayBlank(String[] arrays) {
		if(arrays==null || arrays.length==0) {
			return true;
		}
		for (String str : arrays) {
			if(StringUtils.isNotBlank(str)) {
				return false;
			}
		}
		return true;
	}
}
