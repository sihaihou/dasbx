package com.reyco.dasbx.common.core.service.sys.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.common.core.dao.sys.SysLogDao;
import com.reyco.dasbx.common.core.dao.sys.SysLoginLogDao;
import com.reyco.dasbx.common.core.model.domain.sys.SysLoginLog;
import com.reyco.dasbx.common.core.model.dto.sys.SysLogInsertDto;
import com.reyco.dasbx.common.core.model.dto.sys.SysLogPageDto;
import com.reyco.dasbx.common.core.model.dto.sys.SysLogUpdateDto;
import com.reyco.dasbx.common.core.model.po.sys.SysLogInsertPO;
import com.reyco.dasbx.common.core.model.po.sys.SysLogPO;
import com.reyco.dasbx.common.core.model.po.sys.SysLogUpdatePO;
import com.reyco.dasbx.common.core.model.vo.sys.SysLogInfoVO;
import com.reyco.dasbx.common.core.model.vo.sys.SysLogListVO;
import com.reyco.dasbx.common.core.service.sys.SysLogService;
import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.model.domain.SysLog;

@Service
public class SysLogServiceImpl implements SysLogService {
	private static Logger logger = LoggerFactory.getLogger(SysLogServiceImpl.class);
	@Autowired
	private SysLogDao sysLogDao;
	@Autowired
	private SysLoginLogDao sysLoginLogDao;
	
	@Override
	public SysLogInfoVO get(Long id) {
		SysLog sysLog = sysLogDao.getById(id);
		SysLogInfoVO sysLogInfoVO = Convert.sourceToTarget(sysLog, SysLogInfoVO.class);
		return sysLogInfoVO;
	}

	@Override
	public List<SysLogListVO> listByCode(Long code) {
		List<SysLog> sysLogs = sysLogDao.listByCode(code);
		List<SysLogListVO> sysLogListVOs= Convert.sourceListToTargetList(sysLogs, SysLogListVO.class);
		return sysLogListVOs;
	}

	@Override
	public PageInfo<SysLogListVO> search(SysLogPageDto sysLogPageDto) {
		SysLogPO sysLogPO = Convert.sourceToTarget(sysLogPageDto, SysLogPO.class);
		sysLogPO.setName(sysLogPageDto.getKeyword());
		PageHelper.startPage(sysLogPageDto.getPageNum(), sysLogPageDto.getPageSize());
		List<SysLog> sysLogs = sysLogDao.list(sysLogPO);
		PageInfo<SysLog> sysLogPageInfo = new PageInfo<>(sysLogs);
		if (CollectionUtils.isEmpty(sysLogPageInfo.getList())) {
			logger.debug("###################暂无数据");
			PageInfo<SysLogListVO> sysLogListVOPageInfo = new PageInfo<>();
			return sysLogListVOPageInfo;
		}
		
		logger.debug("###################构建返回结果");
		List<SysLogListVO> sysLogListVOs = Convert.sourceListToTargetList(sysLogs, SysLogListVO.class);
		// 封装分页
		PageInfo<SysLogListVO> sysLogListVOPageInfo = new PageInfo<>(sysLogListVOs);
		sysLogListVOPageInfo.setTotal(sysLogPageInfo.getTotal());
		sysLogListVOPageInfo.setPages(sysLogPageInfo.getPages());
		sysLogListVOPageInfo.setPageNum(sysLogPageInfo.getPageNum());
		return sysLogListVOPageInfo;
	}
	@Override
	public SysLogInfoVO insert(SysLogInsertDto sysLogInsertDto){
		SysLogInsertPO sysLogInsertPO = Convert.sourceToTarget(sysLogInsertDto, SysLogInsertPO.class);
		SysLoginLog sysLoginLog = sysLoginLogDao.getByCode(sysLogInsertPO.getCode());
		if(sysLoginLog!=null) {
			sysLogInsertPO.setUserId(sysLoginLog.getUserId());
		}else {
			sysLogInsertPO.setUserId(0L);
		}
		sysLogInsertPO.setCreateBy(1L);
		sysLogInsertPO.setModifiedBy(1L);
		sysLogInsertPO.setGmtCreate(Dasbx.getCurrentTime());
		sysLogInsertPO.setGmtModified(Dasbx.getCurrentTime());
		sysLogDao.save(sysLogInsertPO);
		SysLogInfoVO sysLogInfoVO = Convert.sourceToTarget(sysLogInsertPO, SysLogInfoVO.class);
		return sysLogInfoVO;
	}
	@Override
	public void updateByCode(SysLogUpdateDto sysLogUpdateDto) {
		SysLogUpdatePO sysLogUpdatePO = Convert.sourceToTarget(sysLogUpdateDto, SysLogUpdatePO.class);
		sysLogUpdatePO.setGmtModified(Dasbx.getCurrentTime());
		sysLogUpdatePO.setModifiedBy(1L);
		sysLogDao.updateByCode(sysLogUpdatePO);
	}
}
