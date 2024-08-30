package com.reyco.dasbx.portal.model.domain.dto;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

public class VideoReviewDto extends SimpleUpdateDto {
	private Boolean review;
	public Boolean getReview() {
		return review;
	}
	public void setReview(Boolean review) {
		this.review = review;
	}
}
