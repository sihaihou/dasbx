package com.reyco.dasbx.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.portal.model.domain.dto.VideCommentAnswerPageDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoCommentInsertDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoCommentLikeDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoCommentPageDto;
import com.reyco.dasbx.portal.model.domain.vo.VideoCommentInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoCommentListVO;
import com.reyco.dasbx.portal.service.VideoCommentService;

@RestController
@RequestMapping("/portal/comment")
public class VideoCommentController {

	@Autowired
	private VideoCommentService videoCommentService;
	
	@GetMapping("listByVideoIdAndParentId")
	public Object listByVideoIdAndParentId(VideoCommentPageDto videoCommentPageDto) {
		PageInfo<VideoCommentListVO> pageInfo = videoCommentService.listByVideoIdAndParentId(videoCommentPageDto);
		return R.success(pageInfo);
	}
	@GetMapping("listByRootId")
	public Object listByRootId(VideCommentAnswerPageDto videCommentAnswerPageDto) {
		PageInfo<VideoCommentListVO> pageInfo = videoCommentService.listByRootId( videCommentAnswerPageDto);
		return R.success(pageInfo);
	}
	
	@PatchMapping("like")
	public Object like(@RequestBody VideoCommentLikeDto videoCommentLikeDto) throws AuthenticationException {
		VideoCommentInfoVO videoCommentInfoVO = videoCommentService.like(videoCommentLikeDto);
		return R.success(videoCommentInfoVO);
	}
	
	@PostMapping
	public Object save(@RequestBody VideoCommentInsertDto videoCommentInsertDto) throws AuthenticationException {
		VideoCommentInfoVO videoCommentInfoVO = videoCommentService.save(videoCommentInsertDto);
		return R.success(videoCommentInfoVO);
	}
}
