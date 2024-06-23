package com.reyco.dasbx.portal.model.domain.dto;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

public class VideoCommentLikeDto extends SimpleUpdateDto{
	private Integer likeQuantity;
	public Integer getLikeQuantity() {
		return likeQuantity;
	}
	public void setLikeQuantity(Integer likeQuantity) {
		this.likeQuantity = likeQuantity;
	}
}
