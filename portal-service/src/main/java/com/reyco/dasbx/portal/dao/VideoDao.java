package com.reyco.dasbx.portal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.portal.model.domain.Video;
import com.reyco.dasbx.portal.model.domain.po.VideoPlayPO;
import com.reyco.dasbx.portal.model.domain.po.VideoSelectPO;
import com.reyco.dasbx.portal.model.domain.vo.VideoListVO;

public interface VideoDao {
	
	Video getById(Long id);
	
	List<VideoListVO> list(VideoSelectPO videoSelectPO);
	
	void updatePlay(@Param("videoPlayPOs")List<VideoPlayPO> videoPlayPOs);
	
}
