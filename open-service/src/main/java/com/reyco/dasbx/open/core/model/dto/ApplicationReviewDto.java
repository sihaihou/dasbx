package com.reyco.dasbx.open.core.model.dto;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

public class ApplicationReviewDto extends SimpleUpdateDto {
	private Long status;

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
}
