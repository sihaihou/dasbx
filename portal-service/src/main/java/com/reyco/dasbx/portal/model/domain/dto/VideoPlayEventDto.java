package com.reyco.dasbx.portal.model.domain.dto;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

public class VideoPlayEventDto extends SimpleUpdateDto {
	private Long id;
	private Long userId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
