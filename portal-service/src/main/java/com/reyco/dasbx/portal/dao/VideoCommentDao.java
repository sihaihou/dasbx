package com.reyco.dasbx.portal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.portal.model.domain.VideoComment;
import com.reyco.dasbx.portal.model.domain.po.VideoCommentInsertPO;
import com.reyco.dasbx.portal.model.domain.po.VideoCommentLikePO;

public interface VideoCommentDao {
	
	VideoComment getById(Long id);
	
	List<VideoComment> listByVideoIdAndParentId(@Param("videoId") Long videoId,@Param("parentId") Long parentId);
	
	List<VideoComment> listByRootId(Long rootId);
	
	Integer countByRootId(Long rootId);
	
	VideoComment getOneByRootId(Long rootId);
	
	void insert(VideoCommentInsertPO videoCommentInsertPO);
	
	void like(VideoCommentLikePO videoCommentLikePO);
	
	void updateCommentQuantityByIds(@Param("commentIds")List<Long> commentIds);
	
}
