package com.reyco.dasbx.portal.dao;

import com.reyco.dasbx.portal.model.domain.po.VideoProductionInsertPO;
import com.reyco.dasbx.portal.model.domain.vo.VideoProductionInfoVO;

public interface VideoProductionDao {
	
	VideoProductionInfoVO getByVideoId(Long videoId);
	
	void insert(VideoProductionInsertPO videoProductionInsertPO);
}
