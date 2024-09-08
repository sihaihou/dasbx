package com.reyco.dasbx.portal.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.reyco.dasbx.commons.utils.CusAccessObjectUtil;
import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.commons.utils.IPDataUtils;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.model.vo.SysAccountToken;
import com.reyco.dasbx.portal.dao.VideoCommentLikeDao;
import com.reyco.dasbx.portal.model.domain.VideoCommentLike;
import com.reyco.dasbx.portal.model.domain.dto.VideoCommentLikeDto;
import com.reyco.dasbx.portal.model.domain.po.VideoCommentLikeInsertPO;
import com.reyco.dasbx.portal.model.domain.po.VideoCommentLikeUpdateStatePO;
import com.reyco.dasbx.portal.service.VideoCommentLikeService;

@Service
public class VideoCommentLikeServiceImpl implements VideoCommentLikeService{
	@Autowired
	private VideoCommentLikeDao videoCommentLikeDao;
	@Override
	public void saveOrUpdate(VideoCommentLikeDto videoCommentLikeDto) throws AuthenticationException {
		SysAccountToken sysAccountToken = TokenUtils.getToken();
		VideoCommentLike videoCommentLike = videoCommentLikeDao.getByCommentIdAndUserId(videoCommentLikeDto.getId(), sysAccountToken.getId());
		if(videoCommentLike==null) {
			VideoCommentLikeInsertPO videoCommentLikeInsertPO = new VideoCommentLikeInsertPO();
			videoCommentLikeInsertPO.setCommentId(videoCommentLikeDto.getId());
			videoCommentLikeInsertPO.setUserId(sysAccountToken.getId());
			videoCommentLikeInsertPO.setTime(Dasbx.getCurrentTime());
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String ipAddress = CusAccessObjectUtil.getIpAddress(request);
			String cityName = IPDataUtils.getCityName(ipAddress);
			videoCommentLikeInsertPO.setIp(ipAddress);
			videoCommentLikeInsertPO.setProvince(cityName);
			videoCommentLikeInsertPO.setCity(cityName);
			videoCommentLikeInsertPO.setDistrict(cityName);
			videoCommentLikeInsertPO.setAddress(cityName);
			videoCommentLikeDao.insert(videoCommentLikeInsertPO);
		}else {
			VideoCommentLikeUpdateStatePO videoCommentLikeUpdateStatePO = new VideoCommentLikeUpdateStatePO();
			videoCommentLikeUpdateStatePO.setId(videoCommentLike.getId());
			videoCommentLikeUpdateStatePO.setState(videoCommentLikeDto.getLikeQuantity()>0?(byte)1:(byte)0);
			videoCommentLikeDao.updateState(videoCommentLikeUpdateStatePO);
		}
	}

}
