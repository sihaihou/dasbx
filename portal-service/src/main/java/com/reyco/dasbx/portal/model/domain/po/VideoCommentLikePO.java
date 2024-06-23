package com.reyco.dasbx.portal.model.domain.po;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class VideoCommentLikePO extends SimpleUpdatePO{
	private Integer like;
	public Integer getLike() {
		return like;
	}

	public void setLike(Integer like) {
		this.like = like;
	}
}
