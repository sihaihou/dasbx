package com.reyco.dasbx.es.core.profile;

public class PipelineProfile {
	/**
	 * QueryRewritePipeline 
	 * QueryOptimizePipeline 
	 * SearchExecutor 
	 * ResultMapper
	 */
	private String name;
	/**
	 * 毫秒
	 */
	private long took;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTook() {
		return took;
	}

	public void setTook(long took) {
		this.took = took;
	}
	@Override
	public String toString() {
	    return String.format("%-30s %6dms",name,took);
	}
}
