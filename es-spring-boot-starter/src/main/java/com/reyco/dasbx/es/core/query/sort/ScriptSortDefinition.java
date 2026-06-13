package com.reyco.dasbx.es.core.query.sort;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.search.sort.ScriptSortBuilder.ScriptSortType;
import org.elasticsearch.search.sort.SortOrder;

public class ScriptSortDefinition implements SortDefinition {
	/**
	 * painless script
	 */
	private String script;

	/**
	 * number/string
	 */
	private ScriptSortType type = ScriptSortType.NUMBER;

	/**
	 * params
	 */
	private Map<String, Object> params = new HashMap<>();

	/**
	 * asc/desc
	 */
	private SortOrder sortOrder = SortOrder.DESC;
	
	private int order;

	public String getScript() {
		return script;
	}

	public ScriptSortDefinition setScript(String script) {
		this.script = script;
		return this;
	}

	public ScriptSortType getType() {
		return type;
	}

	public ScriptSortDefinition setType(ScriptSortType type) {
		this.type = type;
		return this;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public ScriptSortDefinition setParams(Map<String, Object> params) {
		this.params = params;
		return this;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public ScriptSortDefinition setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
		return this;
	}
	public ScriptSortDefinition setOrder(int order) {
		this.order = order;
		return this;
	}
	@Override
	public int getOrder() {
		return order;
	}
}
