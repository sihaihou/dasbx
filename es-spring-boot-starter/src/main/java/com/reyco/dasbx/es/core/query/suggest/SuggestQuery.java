package com.reyco.dasbx.es.core.query.suggest;

public class SuggestQuery {
	
	public final static String DEFAULT_SUGGEST_FIELD = "suggestion";
	
	public final static Integer DEFAULT_SUGGEST_SIZE = 10;
	/**
	 * 输入关键字
	 */
	private String keyword;
	/**
	 * suggest字段
	 */
	private String field = DEFAULT_SUGGEST_FIELD;
	/**
	 * 返回数量
	 */
	private Integer size = DEFAULT_SUGGEST_SIZE;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
}
