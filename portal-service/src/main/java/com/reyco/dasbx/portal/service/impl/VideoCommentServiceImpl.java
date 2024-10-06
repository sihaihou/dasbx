package com.reyco.dasbx.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.commons.utils.net.CusAccessObjectUtil;
import com.reyco.dasbx.commons.utils.net.IPDataUtils;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.model.constants.CachePrefixInfoConstants;
import com.reyco.dasbx.model.vo.SysAccountToken;
import com.reyco.dasbx.portal.dao.VideoCommentDao;
import com.reyco.dasbx.portal.dao.VideoCommentLikeDao;
import com.reyco.dasbx.portal.model.domain.VideoComment;
import com.reyco.dasbx.portal.model.domain.VideoCommentLike;
import com.reyco.dasbx.portal.model.domain.dto.VideCommentAnswerPageDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoCommentInsertDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoCommentLikeDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoCommentPageDto;
import com.reyco.dasbx.portal.model.domain.po.VideoCommentInsertPO;
import com.reyco.dasbx.portal.model.domain.po.VideoCommentLikePO;
import com.reyco.dasbx.portal.model.domain.vo.VideoCommentInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoCommentListVO;
import com.reyco.dasbx.portal.service.VideoCommentLikeService;
import com.reyco.dasbx.portal.service.VideoCommentService;

@Service
public class VideoCommentServiceImpl implements VideoCommentService{
	private static Logger logger = LoggerFactory.getLogger(VideoCommentServiceImpl.class);
	
	@Autowired
	private VideoCommentDao videoCommentDao;
	
	@Autowired
	private VideoCommentLikeDao videoCommentLikeDao;
	
	@Autowired
	private VideoCommentLikeService videoCommentLikeService;
	
	@Override
	@Cacheable(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.PORTAL_VIDEO_COMMENT_INFO_PREFIX,key="#id")
	public VideoCommentInfoVO get(Long id) {
		VideoComment videoComment = videoCommentDao.getById(id);
		VideoCommentInfoVO videoCommentInfoVO = Convert.sourceToTarget(videoComment, VideoCommentInfoVO.class);
		return videoCommentInfoVO;
	}
	
	@Override
	public PageInfo<VideoCommentListVO> listByVideoIdAndParentId(VideoCommentPageDto videoCommentPageDto) {
		PageHelper.startPage(videoCommentPageDto.getPageNum(), videoCommentPageDto.getPageSize());
		List<VideoComment> videoComments = videoCommentDao.listByVideoIdAndParentId(videoCommentPageDto.getVideoId(), videoCommentPageDto.getParentId());
		PageInfo<VideoComment> videoCommentPageInfo = new PageInfo<>(videoComments);
		if (CollectionUtils.isEmpty(videoCommentPageInfo.getList())) {
			PageInfo<VideoCommentListVO> videoCommentListVOPageInfo = new PageInfo<>();
			return videoCommentListVOPageInfo;
		}
		List<VideoCommentListVO> videoCommentListVOs = Convert.sourceListToTargetList(videoComments, VideoCommentListVO.class);
		PageInfo<VideoCommentListVO> videoCommentListVOPageInfo = new PageInfo<>(videoCommentListVOs);
		videoCommentListVOPageInfo.setTotal(videoCommentPageInfo.getTotal());
		videoCommentListVOPageInfo.setPages(videoCommentPageInfo.getPages());
		videoCommentListVOPageInfo.setPageNum(videoCommentPageInfo.getPageNum());
		buildIsLike(videoCommentListVOPageInfo.getList());
		return videoCommentListVOPageInfo;
	}
	
	@Override
	public PageInfo<VideoCommentListVO> listByRootId(VideCommentAnswerPageDto videCommentAnswerPageDto) {
		PageHelper.startPage(videCommentAnswerPageDto.getPageNum(), videCommentAnswerPageDto.getPageSize());
		List<VideoComment> videoComments = videoCommentDao.listByRootId(videCommentAnswerPageDto.getRootId());
		PageInfo<VideoComment> videoCommentPageInfo = new PageInfo<>(videoComments);
		if (CollectionUtils.isEmpty(videoCommentPageInfo.getList())) {
			PageInfo<VideoCommentListVO> videoCommentListVOPageInfo = new PageInfo<>();
			return videoCommentListVOPageInfo;
		}
		List<VideoCommentListVO> videoCommentListVOs = Convert.sourceListToTargetList(videoComments, VideoCommentListVO.class);
		PageInfo<VideoCommentListVO> videoCommentListVOPageInfo = new PageInfo<>(videoCommentListVOs);
		videoCommentListVOPageInfo.setTotal(videoCommentPageInfo.getTotal());
		videoCommentListVOPageInfo.setPages(videoCommentPageInfo.getPages());
		videoCommentListVOPageInfo.setPageNum(videoCommentPageInfo.getPageNum());
		videoCommentListVOPageInfo.getList().forEach(videoCommentListVO->{
			VideoComment parentComment = videoCommentDao.getById(videoCommentListVO.getParentId());
			videoCommentListVO.setAnswerId(parentComment.getUserId());
			videoCommentListVO.setAnswerNickname(parentComment.getNickname());
		});
		buildIsLike(videoCommentListVOPageInfo.getList());
		return videoCommentListVOPageInfo;
	}
	private void buildIsLike(List<VideoCommentListVO> videoCommentListVOs) {
		try {
			SysAccountToken token = TokenUtils.getToken();
			videoCommentListVOs.stream().forEach(videoCommentListVO->{
				VideoCommentLike videoCommentLike = videoCommentLikeDao.getByCommentIdAndUserId(videoCommentListVO.getId(), token.getId());
				if(videoCommentLike!=null && videoCommentLike.getState().intValue()==1) {
					videoCommentListVO.setIsLike(true);
				}
			});
		} catch (AuthenticationException e) {
			logger.error("没有登录构建评论的isLike信息");
		}
	}
	@Override
	@CacheEvict(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.PORTAL_VIDEO_COMMENT_INFO_PREFIX,key="#videoCommentLikeDto.id")
	public VideoCommentInfoVO like(VideoCommentLikeDto videoCommentLikeDto) throws AuthenticationException {
		VideoCommentLikePO videoCommentLikePO = new VideoCommentLikePO();
		videoCommentLikePO.setId(videoCommentLikeDto.getId());
		videoCommentLikePO.setLike(videoCommentLikeDto.getLikeQuantity());
		videoCommentDao.like(videoCommentLikePO);
		videoCommentLikeService.saveOrUpdate(videoCommentLikeDto);
		return get(videoCommentLikeDto.getId());
	}
	@Override
	public VideoCommentInfoVO save(VideoCommentInsertDto videoCommentInsertDto) throws AuthenticationException {
		VideoCommentInsertPO videoCommentInsertPO = Convert.sourceToTarget(videoCommentInsertDto, VideoCommentInsertPO.class);
		if(!videoCommentInsertPO.getParentId().equals(0L)) {
			VideoComment videoComment = videoCommentDao.getById(videoCommentInsertPO.getParentId());
			if(videoComment.getParentId().equals(0L)) {
				videoCommentInsertPO.setRootId(videoComment.getId());
			}else {
				videoCommentInsertPO.setRootId(videoComment.getRootId());
			}
		}else {
			videoCommentInsertPO.setRootId(0L);
		}
		SysAccountToken sysAccountToken = TokenUtils.getToken();
		videoCommentInsertPO.setUserId(sysAccountToken.getId());
		videoCommentInsertPO.setNickname(sysAccountToken.getNickname());
		videoCommentInsertPO.setUserFaceUri(sysAccountToken.getFaceUri());
		videoCommentInsertPO.setTime(Dasbx.getCurrentTime());
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String ipAddress = CusAccessObjectUtil.getIpAddress(request);
		String cityName = IPDataUtils.getCityName(ipAddress);
		videoCommentInsertPO.setIp(ipAddress);
		videoCommentInsertPO.setProvince(cityName);
		videoCommentInsertPO.setCity(cityName);
		videoCommentInsertPO.setDistrict(cityName);
		videoCommentInsertPO.setAddress(cityName);
		videoCommentDao.insert(videoCommentInsertPO);
		List<Long> treeParentIds = getTreeParentId(new ArrayList<Long>(),videoCommentInsertPO.getParentId());
		if(treeParentIds.size()>0) {
			videoCommentDao.updateCommentQuantityByIds(treeParentIds);
		}
		return Convert.sourceToTarget(videoCommentInsertPO, VideoCommentInfoVO.class);
	}
	
	/**
	 * 
	 * @param insertId  当前新增的评论或回复对象Id
	 * @return
	 */
	private List<Long> getTreeParentId(List<Long> treeParentIds,Long insertParentId){
		if(!insertParentId.equals(0L)) {
			treeParentIds.add(insertParentId);
			VideoComment insertParentVideoComment = videoCommentDao.getById(insertParentId);
			if(insertParentVideoComment.getRootId().equals(insertParentVideoComment.getParentId())) {
				treeParentIds.add(insertParentVideoComment.getRootId());
				return treeParentIds;
			}else {
				return getTreeParentId(treeParentIds,insertParentVideoComment.getParentId());
			}
		}
		return treeParentIds;
	}
	
	
}
