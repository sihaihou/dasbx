package com.reyco.dasbx.portal.model.domain.po;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class VideoCommentLikeUpdateStatePO extends SimpleUpdatePO {
	private Byte state;
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
}
