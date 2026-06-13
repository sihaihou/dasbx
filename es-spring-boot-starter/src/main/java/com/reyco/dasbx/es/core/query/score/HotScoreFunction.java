package com.reyco.dasbx.es.core.query.score;

public class HotScoreFunction implements ScoreFunction {

	private Float saleWeight = 0.1F;

	private Float commentWeight = 0.2F;

	private Float favorWeight = 0.3F;

	public Float getSaleWeight() {
		return saleWeight;
	}

	public Float getCommentWeight() {
		return commentWeight;
	}

	public Float getFavorWeight() {
		return favorWeight;
	}
}
