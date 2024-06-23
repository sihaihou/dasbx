package com.reyco.dasbx.portal.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.portal.model.domain.dto.VideoPageDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoPlayEventDto;
import com.reyco.dasbx.portal.model.domain.vo.VideoInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoListVO;
import com.reyco.dasbx.portal.service.impl.VideoServiceImpl.PlayEvent;

public interface VideoService {
	
	VideoInfoVO get(Long id);
	
	PageInfo<VideoListVO> list(VideoPageDto videoPageDto);
	
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
}
