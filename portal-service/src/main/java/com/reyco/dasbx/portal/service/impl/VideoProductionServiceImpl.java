package com.reyco.dasbx.portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.model.constants.CachePrefixInfoConstants;
import com.reyco.dasbx.portal.dao.VideoProductionDao;
import com.reyco.dasbx.portal.model.domain.vo.VideoProductionInfoVO;
import com.reyco.dasbx.portal.service.VideoProductionService;

@Service
public class VideoProductionServiceImpl implements VideoProductionService {
	@Autowired
	private VideoProductionDao videoProductionDao;
	
	@Override
	@Cacheable(cacheManager="redisCacheManager",cacheNames=CachePrefixInfoConstants.PORTAL_VIDEO_PRODUCTION_INFO_PREFIX,key="#videoId")
	public VideoProductionInfoVO getByVideoId(Long videoId) {
		return videoProductionDao.getByVideoId(videoId);
	}

}
