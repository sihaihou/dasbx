package com.reyco.dasbx.portal.dao;

import java.util.List;

import com.reyco.dasbx.portal.model.domain.VideoDanmaku;
import com.reyco.dasbx.portal.model.domain.po.VideoDanmakuInsertPO;
import com.reyco.dasbx.portal.model.domain.po.VideoDanmakuListPO;

public interface VideoDanmakuDao {
	
	VideoDanmaku getById(Long id);
	
	List<VideoDanmaku> listByVideoIdAndDanmakuTime(VideoDanmakuListPO VideoDanmakuListPO);
	
	
	void insert(VideoDanmakuInsertPO videoDanmakuInsertPO);
	
	
}
