package com.reyco.dasbx.es.core.feature.alias;

import java.util.ArrayList;
import java.util.List;

public class AliasDefinition {
	/**
	 * alias
	 */
	private String alias;

	/**
	 * 真实索引
	 */
	private List<String> indices = new ArrayList<>();
	/**
	 * index pattern
	 */
	private String pattern;
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public List<String> getIndices() {
		return indices;
	}

	public void setIndices(List<String> indices) {
		this.indices = indices;
	}
	
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}
