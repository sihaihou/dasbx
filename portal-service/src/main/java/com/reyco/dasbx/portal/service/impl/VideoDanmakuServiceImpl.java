package com.reyco.dasbx.portal.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.CusAccessObjectUtil;
import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.commons.utils.IPDataUtils;
import com.reyco.dasbx.portal.dao.VideoDanmakuDao;
import com.reyco.dasbx.portal.model.domain.VideoDanmaku;
import com.reyco.dasbx.portal.model.domain.dto.VideoDanmakuInsertDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoDanmakuListDto;
import com.reyco.dasbx.portal.model.domain.po.VideoDanmakuInsertPO;
import com.reyco.dasbx.portal.model.domain.po.VideoDanmakuListPO;
import com.reyco.dasbx.portal.model.domain.vo.VideoDanmakuInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoDanmakuListVO;
import com.reyco.dasbx.portal.service.VideoDanmakuService;

@Service
public class VideoDanmakuServiceImpl implements VideoDanmakuService{

	@Autowired
	private VideoDanmakuDao videoDanmakuDao;

	@Override
	public List<VideoDanmakuListVO> listByVideoIdAndDanmakuTime(VideoDanmakuListDto videoDanmakuListDto) {
		VideoDanmakuListPO videoDanmakuListPO = Convert.sourceToTarget(videoDanmakuListDto, VideoDanmakuListPO.class);
		List<VideoDanmaku> videoDanmakus = videoDanmakuDao.listByVideoIdAndDanmakuTime(videoDanmakuListPO);
		List<VideoDanmakuListVO> videoDanmakuListVOs = Convert.sourceListToTargetList(videoDanmakus, VideoDanmakuListVO.class);
		return videoDanmakuListVOs;
	}

	@Override
	public VideoDanmakuInfoVO save(VideoDanmakuInsertDto videoDanmakuInsertDto) {
		VideoDanmakuInsertPO videoDanmakuInsertPO = Convert.sourceToTarget(videoDanmakuInsertDto, VideoDanmakuInsertPO.class);
		videoDanmakuInsertPO.setTime(Dasbx.getCurrentTime());
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String ipAddress = CusAccessObjectUtil.getIpAddress(request);
		String cityName = IPDataUtils.getCityName(ipAddress);
		videoDanmakuInsertPO.setIp(ipAddress);
		videoDanmakuInsertPO.setProvince(cityName);
		videoDanmakuInsertPO.setCity(cityName);
		videoDanmakuInsertPO.setDistrict(cityName);
		videoDanmakuInsertPO.setAddress(cityName);
		videoDanmakuDao.insert(videoDanmakuInsertPO);
		/*try {
			WebSocketServer.sendInfo(videoDanmakuInsertDto.getVideoId(), videoDanmakuInsertDto.getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return Convert.sourceToTarget(videoDanmakuInsertPO, VideoDanmakuInfoVO.class);
	}
	
	
	
}
