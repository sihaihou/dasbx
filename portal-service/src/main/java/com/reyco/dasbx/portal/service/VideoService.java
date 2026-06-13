package com.reyco.dasbx.portal.service;

import java.io.IOException;
import java.util.List;

import com.reyco.dasbx.commons.exception.AuthenticationException;
import com.reyco.dasbx.es.support.result.Result;
import com.reyco.dasbx.portal.model.domain.dto.VideoInsertDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoListSearchDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoPlayEventDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoReviewDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoSearchDto;
import com.reyco.dasbx.portal.model.domain.vo.VideoInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoListDetailVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoListVO;
import com.reyco.dasbx.portal.service.impl.VideoServiceImpl.PlayEvent;
import com.reyco.dasbx.sync.exception.SyncException;

public interface VideoService {
	
	int initElasticsearchVideo() throws SyncException;
	
	VideoInfoVO get(Long id);
	
	List<String> getSuggestion(String keyword) throws Exception;
	
	Result<VideoListVO> search(VideoSearchDto videoSearchDto) throws IOException;
	
	Result<VideoListDetailVO> searchBack(VideoListSearchDto videoListSearchDto) throws IOException;
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
