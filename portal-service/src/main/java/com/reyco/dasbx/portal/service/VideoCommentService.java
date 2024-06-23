package com.reyco.dasbx.portal.service;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.portal.model.domain.dto.VideCommentAnswerPageDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoCommentInsertDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoCommentLikeDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoCommentPageDto;
import com.reyco.dasbx.portal.model.domain.vo.VideoCommentInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoCommentListVO;

public interface VideoCommentService {
	
	VideoCommentInfoVO get(Long id);
	
	PageInfo<VideoCommentListVO> listByVideoIdAndParentId(VideoCommentPageDto videoCommentPageDto);
	
	PageInfo<VideoCommentListVO> listByRootId(VideCommentAnswerPageDto videCommentAnswerPageDto);

	VideoCommentInfoVO like(VideoCommentLikeDto videoCommentLikeDto) throws AuthenticationException;

	VideoCommentInfoVO save(VideoCommentInsertDto videoCommentInsertDto) throws AuthenticationException ;
}
