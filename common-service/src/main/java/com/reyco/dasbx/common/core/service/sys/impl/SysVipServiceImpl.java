package com.reyco.dasbx.common.core.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.common.core.dao.sys.SysVipDao;
import com.reyco.dasbx.common.core.service.sys.SysVipService;
import com.reyco.dasbx.model.constants.CachePrefixInfoConstants;
import com.reyco.dasbx.model.domain.SysVip;

@Service
public class SysVipServiceImpl implements SysVipService {
	@Autowired
	private SysVipDao sysVipDao;
	
	@Override
	@Cacheable(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.COMMON_VIP_INFO_PREFIX,key="#id")
	public SysVip getById(Long id) {
		return sysVipDao.getById(id);
	}

	@Override
	public List<SysVip> list() {
		return sysVipDao.list();
	}

}
