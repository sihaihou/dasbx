package com.reyco.dasbx.common.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.common.core.dao.sys.SurnameDao;
import com.reyco.dasbx.common.core.service.SurnameService;
import com.reyco.dasbx.commons.utils.random.RandomUtils;
import com.reyco.dasbx.model.constants.CachePrefixInfoConstants;
import com.reyco.dasbx.model.domain.Surname;

@Service
public class SurnameServiceImpl implements SurnameService {
	@Autowired
	private SurnameDao surnameDao;
	
	@Override
	@Cacheable(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.COMMON_SURNAME_INFO_PREFIX,key="#id")
	public Surname getById(Long id) {
		return surnameDao.getById(id);
	}

	@Override
	public Surname randomSurname() {
		int count = surnameDao.getCount();
		int id = RandomUtils.randomInt(count)+1;
		return getById(new Long(id));
	}

}
