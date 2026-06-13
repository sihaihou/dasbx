package com.reyco.dasbx.es.core.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchHitResult<T> {
	/**
	 * 文档
	 */
	private T source;
	 /**
     * _id
     */
    private String id;
    /**
     * version
     */
    private long version = -1;
    /**
     * _index
     */
    private String index;
	/**
	 * score
	 */
	private Float score;
	
	/**
	 * highlight
	 */
	private Map<String, List<String>> highlights = new HashMap<>();
	
	 /**
     * search_after / sort
     */
    private Object[] sortValues;
    
    private Double distance;

	public T getSource() {
		return source;
	}

	public void setSource(T source) {
		this.source = source;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Map<String, List<String>> getHighlights() {
		return highlights;
	}

	public void setHighlights(Map<String, List<String>> highlights) {
		this.highlights = highlights;
	}

	public Object[] getSortValues() {
		return sortValues;
	}

	public void setSortValues(Object[] sortValues) {
		this.sortValues = sortValues;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
}
