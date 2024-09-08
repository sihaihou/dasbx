package com.reyco.dasbx.portal.service;

import java.io.IOException;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.es.core.search.SearchVO;
import com.reyco.dasbx.portal.model.domain.dto.VideoInsertDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoPageDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoPlayEventDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoReviewDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoSearchDto;
import com.reyco.dasbx.portal.model.domain.vo.VideoInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoListVO;
import com.reyco.dasbx.portal.service.impl.VideoServiceImpl.PlayEvent;

public interface VideoService {
	
	int initElasticsearchVideo() throws IOException;
	
	VideoInfoVO get(Long id);
	
	PageInfo<VideoListVO> list(VideoPageDto videoPageDto);
	
	List<String> getSuggestion(String keyword) throws Exception;
	
	SearchVO<VideoListVO> search(VideoSearchDto videoSearchDto) throws IOException;
	/**
	 * 
	 */
	void add(VideoInsertDto videoInsertDto) throws AuthenticationException;
	/**
	 * 添加播放事件
	 * @param videoPlayEventDto
	 */
	void addPlayEvent(VideoPlayEventDto videoPlayEventDto);
	
	/**
	 * 处理播放事件
	 * @param playEvents
	 */
	void processPlay(List<PlayEvent> playEvents);
	/**
	 * 审核
	 * @param videoReviewDto
	 */
	void review(VideoReviewDto videoReviewDto);
	
}
