package com.reyco.dasbx.es.core.feature.highlight;

public class HighlightDefinition {
	/**
	 * 字段
	 */
	private String field;

	/**
	 * 前缀
	 */
	private String preTag = "<em>";

	/**
	 * 后缀
	 */
	private String postTag = "</em>";

	/**
	 * fragment size
	 */
	private Integer fragmentSize = 100;

	/**
	 * fragment num
	 */
	private Integer numOfFragments = 1;

	/**
	 * require field match
	 */
	private boolean requireFieldMatch = false;

	public HighlightDefinition() {

	}

	public HighlightDefinition(String field) {
		this.field = field;
	}
	
	public HighlightDefinition(String field, String preTag, String postTag) {
		super();
		this.field = field;
		this.preTag = preTag;
		this.postTag = postTag;
	}

	public String getField() {
		return field;
	}

	public HighlightDefinition setField(String field) {
		this.field = field;
		return this;
	}

	public String getPreTag() {
		return preTag;
	}

	public HighlightDefinition setPreTag(String preTag) {
		this.preTag = preTag;
		return this;
	}

	public String getPostTag() {
		return postTag;
	}

	public HighlightDefinition setPostTag(String postTag) {
		this.postTag = postTag;
		return this;
	}

	public Integer getFragmentSize() {
		return fragmentSize;
	}

	public HighlightDefinition setFragmentSize(Integer fragmentSize) {
		this.fragmentSize = fragmentSize;
		return this;
	}

	public Integer getNumOfFragments() {
		return numOfFragments;
	}

	public HighlightDefinition setNumOfFragments(Integer numOfFragments) {
		this.numOfFragments = numOfFragments;
		return this;
	}

	public Boolean getRequireFieldMatch() {
		return requireFieldMatch;
	}

	public HighlightDefinition setRequireFieldMatch(Boolean requireFieldMatch) {
		this.requireFieldMatch = requireFieldMatch;
		return this;
	}

}
