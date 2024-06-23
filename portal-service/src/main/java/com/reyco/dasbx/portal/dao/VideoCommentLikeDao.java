package com.reyco.dasbx.portal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.portal.model.domain.VideoCommentLike;
import com.reyco.dasbx.portal.model.domain.po.VideoCommentLikeInsertPO;
import com.reyco.dasbx.portal.model.domain.po.VideoCommentLikeUpdateStatePO;

public interface VideoCommentLikeDao {
	
	VideoCommentLike getById(Long id);
	
	VideoCommentLike getByCommentIdAndUserId(@Param("commentId")Long commentId,@Param("userId")Long userId);
	
	List<VideoCommentLike> listByCommentId(Long commentId);
	
	void insert(VideoCommentLikeInsertPO videoCommentLikeInsertPO);
	
	void updateState(VideoCommentLikeUpdateStatePO videoCommentLikeUpdateStatePO);
}
