package com.reyco.dasbx.portal.service;

import com.reyco.dasbx.portal.model.domain.vo.VideoProductionInfoVO;

public interface VideoProductionService {
	
	VideoProductionInfoVO getByVideoId(Long videoId);
}
