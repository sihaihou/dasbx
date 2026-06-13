package com.reyco.dasbx.portal.service;

import com.reyco.dasbx.commons.exception.AuthenticationException;
import com.reyco.dasbx.portal.model.domain.dto.VideoCommentLikeDto;

public interface VideoCommentLikeService {
	
	void saveOrUpdate(VideoCommentLikeDto videoCommentLikeDto) throws AuthenticationException;
}
