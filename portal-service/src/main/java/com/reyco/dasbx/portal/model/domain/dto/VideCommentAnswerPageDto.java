package com.reyco.dasbx.portal.model.domain.dto;

import com.reyco.dasbx.model.dto.SimplePageDto;

public class VideCommentAnswerPageDto extends SimplePageDto{
	private Long rootId;
	public Long getRootId() {
		return rootId;
	}

	public void setRootId(Long rootId) {
		this.rootId = rootId;
	}
}
