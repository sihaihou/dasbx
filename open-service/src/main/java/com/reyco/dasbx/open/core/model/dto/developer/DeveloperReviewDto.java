package com.reyco.dasbx.open.core.model.dto.developer;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

public class DeveloperReviewDto extends SimpleUpdateDto{
	private Byte state;

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}
}
