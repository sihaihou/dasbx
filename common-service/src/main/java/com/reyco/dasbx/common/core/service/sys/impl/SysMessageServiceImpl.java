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
import com.reyco.dasbx.common.core.dao.sys.SysMessageDao;
import com.reyco.dasbx.common.core.model.domain.sys.SysMessage;
import com.reyco.dasbx.common.core.model.dto.sys.message.SysMessageDeleteDto;
import com.reyco.dasbx.common.core.model.dto.sys.message.SysMessageSelectDto;
import com.reyco.dasbx.common.core.model.dto.sys.message.SysMessageUpdateHandleDto;
import com.reyco.dasbx.common.core.model.dto.sys.message.SysMessageUpdateReadDto;
import com.reyco.dasbx.common.core.model.po.sys.message.SysMessageDeletePO;
import com.reyco.dasbx.common.core.model.po.sys.message.SysMessageInsertPO;
import com.reyco.dasbx.common.core.model.po.sys.message.SysMessageSelectPO;
import com.reyco.dasbx.common.core.model.po.sys.message.SysMessageUpdateHandlePO;
import com.reyco.dasbx.common.core.model.po.sys.message.SysMessageUpdateReadPO;
import com.reyco.dasbx.common.core.model.vo.sys.message.SysMessageInfoVO;
import com.reyco.dasbx.common.core.service.SysAccountService;
import com.reyco.dasbx.common.core.service.sys.SysMessageService;
import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.model.constants.CachePrefixInfoConstants;
import com.reyco.dasbx.model.constants.Constants;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.model.dto.SysMessageInsertDto;

@Service
public class SysMessageServiceImpl implements SysMessageService {
	
	private static Logger logger = LoggerFactory.getLogger(SysMessageServiceImpl.class);
	
	@Autowired
	private SysMessageDao sysMessageDao;
	@Autowired
	private SysAccountService sysAccountService;
	
	@Override
	@Cacheable(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.COMMON_MESSAGE_INFO_PREFIX,key="#id")
	public SysMessageInfoVO getById(Long id) throws AuthenticationException {
		SysMessage sysMessage = sysMessageDao.getById(id);
		if(sysMessage!=null && sysMessage.getRead().intValue()==0) {
			SysMessageUpdateReadPO sysMessageUpdateReadPO = new SysMessageUpdateReadPO();
			sysMessageUpdateReadPO.setId(sysMessage.getId());
			sysMessageUpdateReadPO.setRead((byte)1);
			sysMessageUpdateReadPO.setGmtRead(Dasbx.getCurrentTime());
			sysMessageUpdateReadPO.setReadBy(TokenUtils.getToken().getId());
			sysMessageDao.updateRead(sysMessageUpdateReadPO);
			sysMessage.setRead((byte)1);
			sysMessage.setGmtRead(sysMessageUpdateReadPO.getGmtRead());
			sysMessage.setReadBy(sysMessageUpdateReadPO.getReadBy());
		}
		SysMessageInfoVO sysMessageInfo = Convert.sourceToTarget(sysMessage, SysMessageInfoVO.class);
		processSysMessageInfoVO(sysMessageInfo);
		return sysMessageInfo;
	}
	private void processSysMessageInfoVO(SysMessageInfoVO sysMessageInfo) {
		SysAccount sysAccount = sysAccountService.getById(sysMessageInfo.getUserId());
		sysMessageInfo.setUsername(sysAccount.getUsername());
		if(sysMessageInfo.getRead().intValue()==1) {
			if(sysMessageInfo.getReadBy().equals(sysAccount.getId())) {
				sysMessageInfo.setReadByDesc(sysAccount.getUsername());
			}else {
				SysAccount readAccount = sysAccountService.getById(sysMessageInfo.getReadBy());
				sysMessageInfo.setReadByDesc(readAccount.getUsername());
			}
		}
		if(sysMessageInfo.getHandle().intValue()==1) {
			if(sysMessageInfo.getHandleBy().equals(sysMessageInfo.getUserId()) || sysMessageInfo.getHandleBy().equals(sysMessageInfo.getReadBy())) {
				if(sysMessageInfo.getHandleBy().equals(sysMessageInfo.getUserId())) {
					sysMessageInfo.setHandleByDesc(sysMessageInfo.getUsername());
				}
				if(sysMessageInfo.getHandleBy().equals(sysMessageInfo.getReadBy())) {
					sysMessageInfo.setHandleByDesc(sysMessageInfo.getReadByDesc());
				}
			}else {
				SysAccount handleAccount = sysAccountService.getById(sysMessageInfo.getReadBy());
				sysMessageInfo.setHandleByDesc(handleAccount.getUsername());
			}
		}
	}
	
	@Override
	public PageInfo<SysMessageInfoVO> list(SysMessageSelectDto sysMessageSelectDto) throws AuthenticationException {
		SysMessageSelectPO sysMessageSelectPO = Convert.sourceToTarget(sysMessageSelectDto, SysMessageSelectPO.class);
		Long id = TokenUtils.getToken().getId();
		if(Constants.SUPER_ADMIN.intValue()!=id.intValue()) {
			sysMessageSelectPO.setUserId(id);
		}
		PageHelper.startPage(sysMessageSelectDto.getPageNum(), sysMessageSelectDto.getPageSize());
		List<SysMessage> sysMessages = sysMessageDao.list(sysMessageSelectPO);
		PageInfo<SysMessage> sysMessagePageInfo = new PageInfo<>(sysMessages);
		if (CollectionUtils.isEmpty(sysMessagePageInfo.getList())) {
			logger.debug("###################暂无数据");
			PageInfo<SysMessageInfoVO> sysMessageInfoVOPageInfo = new PageInfo<>();
			return sysMessageInfoVOPageInfo;
		}
		
		logger.debug("###################构建返回结果");
		List<SysMessageInfoVO> sysMessageInfoVOs = Convert.sourceListToTargetList(sysMessages, SysMessageInfoVO.class);
		// 封装分页
		PageInfo<SysMessageInfoVO> sysMessageInfoVOPageInfo = new PageInfo<SysMessageInfoVO>(sysMessageInfoVOs);
		sysMessageInfoVOPageInfo.getList().stream().forEach(sysMessageInfoVO->{
			processSysMessageInfoVO(sysMessageInfoVO);
		});;
		sysMessageInfoVOPageInfo.setTotal(sysMessagePageInfo.getTotal());
		sysMessageInfoVOPageInfo.setPages(sysMessagePageInfo.getPages());
		sysMessageInfoVOPageInfo.setPageNum(sysMessagePageInfo.getPageNum());
		return sysMessageInfoVOPageInfo;
	}
	
	@Override
	public Integer getMessageCount() throws AuthenticationException {
		Long id = TokenUtils.getToken().getId();
		Long userId = null;
		if(Constants.SUPER_ADMIN.intValue()!=id.intValue()) {
			userId = id;
		}
		return sysMessageDao.getCountByUserId(userId);
	}
	
	@Override
	public void insert(SysMessageInsertDto sysMessageInsertDto) {
		SysMessageInsertPO sysMessageInsertPO = Convert.sourceToTarget(sysMessageInsertDto, SysMessageInsertPO.class);
		sysMessageInsertPO.setCreateBy(1L);
		sysMessageInsertPO.setModifiedBy(1L);
		sysMessageInsertPO.setGmtCreate(Dasbx.getCurrentTime());
		sysMessageInsertPO.setGmtModified(Dasbx.getCurrentTime());
		sysMessageDao.insert(sysMessageInsertPO);
	}

	@Override
	@CacheEvict(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.COMMON_MESSAGE_INFO_PREFIX,key="#sysMessageUpdateReadDto.id")
	public void updateRead(SysMessageUpdateReadDto sysMessageUpdateReadDto) throws AuthenticationException {
		SysMessageUpdateReadPO sysMessageUpdateReadPO = Convert.sourceToTarget(sysMessageUpdateReadDto, SysMessageUpdateReadPO.class);
		sysMessageUpdateReadPO.setGmtRead(Dasbx.getCurrentTime());
		sysMessageUpdateReadPO.setReadBy(TokenUtils.getToken().getId());
		sysMessageDao.updateRead(sysMessageUpdateReadPO);
	}

	@Override
	@CacheEvict(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.COMMON_MESSAGE_INFO_PREFIX,key="#sysMessageUpdateHandleDto.id")
	public void updateHandle(SysMessageUpdateHandleDto sysMessageUpdateHandleDto) throws AuthenticationException {
		SysMessageUpdateHandlePO sysMessageUpdateHandlePO = Convert.sourceToTarget(sysMessageUpdateHandleDto, SysMessageUpdateHandlePO.class);
		sysMessageUpdateHandlePO.setGmtHandle(Dasbx.getCurrentTime());
		sysMessageUpdateHandlePO.setHandleBy(TokenUtils.getToken().getId());
		sysMessageDao.updateHandle(sysMessageUpdateHandlePO);
	}

	@Override
	@CacheEvict(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.COMMON_MESSAGE_INFO_PREFIX,key="#sysMessageDeleteDto.id")
	public void deleteById(SysMessageDeleteDto sysMessageDeleteDto) {
		SysMessageDeletePO sysMessageDeletePO = Convert.sourceToTarget(sysMessageDeleteDto, SysMessageDeletePO.class);
		sysMessageDao.deleteById(sysMessageDeletePO);
	}

}
