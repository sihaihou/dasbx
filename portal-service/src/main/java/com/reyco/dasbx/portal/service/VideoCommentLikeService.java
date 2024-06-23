package com.reyco.dasbx.portal.service;

import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.portal.model.domain.dto.VideoCommentLikeDto;

public interface VideoCommentLikeService {
	
	void saveOrUpdate(VideoCommentLikeDto videoCommentLikeDto) throws AuthenticationException;
}
