package com.reyco.dasbx.common.core.service.sys.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.common.core.dao.sys.SysLoginLogDao;
import com.reyco.dasbx.common.core.model.domain.sys.SysLoginLog;
import com.reyco.dasbx.common.core.model.dto.sys.SysLoginLogInsertDto;
import com.reyco.dasbx.common.core.model.dto.sys.SysLoginLogLoginUpdateDto;
import com.reyco.dasbx.common.core.model.dto.sys.SysLoginLogLogoutUpdateDto;
import com.reyco.dasbx.common.core.model.dto.sys.SysLoginLogPageDto;
import com.reyco.dasbx.common.core.model.po.sys.SysLoginLogInsertPO;
import com.reyco.dasbx.common.core.model.po.sys.SysLoginLogLoginUpdatePO;
import com.reyco.dasbx.common.core.model.po.sys.SysLoginLogLogoutUpdatePO;
import com.reyco.dasbx.common.core.model.po.sys.SysLoginLogPO;
import com.reyco.dasbx.common.core.model.vo.sys.SysLoginLogInfoVO;
import com.reyco.dasbx.common.core.model.vo.sys.SysLoginLogListVO;
import com.reyco.dasbx.common.core.service.sys.SysLoginLogService;
import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.model.constants.CachePrefixInfoConstants;

@Service
public class SysLoginLogServiceImpl implements SysLoginLogService {
	private static Logger logger = LoggerFactory.getLogger(SysLoginLogServiceImpl.class);
	@Autowired
	private SysLoginLogDao sysLoginLogDao;
	
	@Override
	@Cacheable(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.COMMON_LOGINLOG_INFO_PREFIX,key="#id")
	public SysLoginLogInfoVO get(Long id) {
		SysLoginLog sysLoginLog = sysLoginLogDao.getById(id);
		SysLoginLogInfoVO sysLoginLogInfoVO = Convert.sourceToTarget(sysLoginLog, SysLoginLogInfoVO.class);
		return sysLoginLogInfoVO;
	}

	@Override
	public List<SysLoginLogListVO> listByUserId(Long userId) {
		List<SysLoginLog> sysLoginLogs = sysLoginLogDao.listByUserId(userId);
		List<SysLoginLogListVO> sysLoginLogListVOs = Convert.sourceListToTargetList(sysLoginLogs, SysLoginLogListVO.class);
		return sysLoginLogListVOs;
	}

	@Override
	public PageInfo<SysLoginLogListVO> search(SysLoginLogPageDto sysLoginLogPageDto) {
		PageHelper.startPage(sysLoginLogPageDto.getPageNum(), sysLoginLogPageDto.getPageSize());
		SysLoginLogPO sysLoginLogPO = Convert.sourceToTarget(sysLoginLogPageDto, SysLoginLogPO.class);
		sysLoginLogPO.setUsername(sysLoginLogPageDto.getKeyword());
		List<SysLoginLog> sysLoginLog = sysLoginLogDao.list(sysLoginLogPO);
		PageInfo<SysLoginLog> sysLoginLogPageInfo = new PageInfo<>(sysLoginLog);
		if (CollectionUtils.isEmpty(sysLoginLogPageInfo.getList())) {
			logger.debug("###################暂无数据");
			PageInfo<SysLoginLogListVO> sysLoginLogListVOPageInfo = new PageInfo<>();
			return sysLoginLogListVOPageInfo;
		}
		
		logger.debug("###################构建返回结果");
		List<SysLoginLogListVO> sysLoginLogListVOs = Convert.sourceListToTargetList(sysLoginLog, SysLoginLogListVO.class);
		// 封装分页
		PageInfo<SysLoginLogListVO> sysLoginLogListVOPageInfo = new PageInfo<>(sysLoginLogListVOs);
		sysLoginLogListVOPageInfo.setTotal(sysLoginLogPageInfo.getTotal());
		sysLoginLogListVOPageInfo.setPages(sysLoginLogPageInfo.getPages());
		return sysLoginLogListVOPageInfo;
	}
	@Override
	public SysLoginLogInfoVO insert(SysLoginLogInsertDto sysLoginLogInsertDto) {
		SysLoginLogInsertPO sysLoginLogInsertPO = Convert.sourceToTarget(sysLoginLogInsertDto, SysLoginLogInsertPO.class);
		sysLoginLogInsertPO.setCreateBy(sysLoginLogInsertDto.getUserId());
		sysLoginLogInsertPO.setGmtCreate(sysLoginLogInsertDto.getGmtLogin());
		sysLoginLogInsertPO.setModifiedBy(sysLoginLogInsertDto.getUserId());
		sysLoginLogInsertPO.setGmtModified(sysLoginLogInsertDto.getGmtLogin());
		sysLoginLogDao.save(sysLoginLogInsertPO);
		SysLoginLogInfoVO sysLoginLogInfoVO = Convert.sourceToTarget(sysLoginLogInsertPO, SysLoginLogInfoVO.class);
		return sysLoginLogInfoVO;
	}
	@Override
	@CacheEvict(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.COMMON_LOGINLOG_INFO_PREFIX,key="#sysLoginLogLoginUpdateDto.id")
	public void updateLogin(SysLoginLogLoginUpdateDto sysLoginLogLoginUpdateDto) {
		SysLoginLogLoginUpdatePO sysLoginLogLoginUpdatePO = Convert.sourceToTarget(sysLoginLogLoginUpdateDto, SysLoginLogLoginUpdatePO.class);
		SysLoginLog sysLoginLog = sysLoginLogDao.getByCode(sysLoginLogLoginUpdateDto.getCode());
		if(sysLoginLog==null) {
			return;
		}
		sysLoginLogLoginUpdatePO.setId(sysLoginLog.getId());
		sysLoginLogDao.updateLogin(sysLoginLogLoginUpdatePO);
	}
	@Override
	@CacheEvict(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.COMMON_LOGINLOG_INFO_PREFIX,key="#sysLoginLogUpdateDto.id")
	public void updateLogout(SysLoginLogLogoutUpdateDto sysLoginLogUpdateDto) {
		SysLoginLogLogoutUpdatePO sysLoginLogUpdatePO = Convert.sourceToTarget(sysLoginLogUpdateDto, SysLoginLogLogoutUpdatePO.class);
		SysLoginLog sysLoginLog = sysLoginLogDao.getByCode(sysLoginLogUpdateDto.getCode());
		if(sysLoginLog==null) {
			return;
		}
		sysLoginLogUpdatePO.setId(sysLoginLog.getId());
		sysLoginLogDao.updateLogout(sysLoginLogUpdatePO);
	}
	
}
