package com.reyco.dasbx.portal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.portal.model.domain.Video;
import com.reyco.dasbx.portal.model.domain.po.VideoDecodePO;
import com.reyco.dasbx.portal.model.domain.po.VideoInsertPO;
import com.reyco.dasbx.portal.model.domain.po.VideoPlayPO;
import com.reyco.dasbx.portal.model.domain.po.VideoReviewPO;
import com.reyco.dasbx.portal.model.domain.po.VideoSelectPO;
import com.reyco.dasbx.portal.model.domain.vo.VideoListVO;

public interface VideoDao {
	
	Video getById(Long id);
	
	List<VideoListVO> list(VideoSelectPO videoSelectPO);
	
	void insert(VideoInsertPO videoInsertPO);
	
	void updatePlay(@Param("videoPlayPOs")List<VideoPlayPO> videoPlayPOs);
	
	/**
	 * 审核
	 * @param videoReviewPO
	 */
	void review(VideoReviewPO videoReviewPO);
	/**
	 * 解码
	 * @param videoDecodePO
	 */
	void decode(VideoDecodePO videoDecodePO);
	
	
	Long getMaxId();
	
	List<Video> getListByLimit(@Param("startId")Long startId,@Param("endId")Long endId);
}
