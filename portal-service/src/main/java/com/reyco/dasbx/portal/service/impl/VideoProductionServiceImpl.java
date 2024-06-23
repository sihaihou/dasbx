package com.reyco.dasbx.portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.portal.dao.VideoProductionDao;
import com.reyco.dasbx.portal.model.domain.vo.VideoProductionInfoVO;
import com.reyco.dasbx.portal.service.VideoProductionService;

@Service
public class VideoProductionServiceImpl implements VideoProductionService {
	@Autowired
	private VideoProductionDao videoProductionDao;
	@Override
	public VideoProductionInfoVO getByVideoId(Long videoId) {
		return videoProductionDao.getByVideoId(videoId);
	}

}
