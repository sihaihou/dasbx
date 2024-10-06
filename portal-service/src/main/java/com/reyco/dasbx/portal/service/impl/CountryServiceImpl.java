package com.reyco.dasbx.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.model.constants.CachePrefixInfoConstants;
import com.reyco.dasbx.portal.dao.CountryDao;
import com.reyco.dasbx.portal.model.domain.Country;
import com.reyco.dasbx.portal.model.domain.vo.CountryListVO;
import com.reyco.dasbx.portal.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService {
	@Autowired
	private CountryDao countryDao;
	
	@Override
	@Cacheable(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.PORTAL_COUNTRY_INFO_PREFIX,key="#id")
	public CountryListVO get(Long id) {
		Country country = countryDao.getById(id);
		CountryListVO countryListVO = Convert.sourceToTarget(country, CountryListVO.class);
		return countryListVO;
	}
	
	@Override
	public List<CountryListVO> list() {
		List<Country> countrys = countryDao.list();
		List<CountryListVO> countryListVO = Convert.sourceListToTargetList(countrys, CountryListVO.class);
		return countryListVO;
	}
	@Override
	public List<CountryListVO> listByCategoryId(Long categoryId) {
		if(categoryId==null || categoryId.intValue()<1) {
			return new ArrayList<>();
		}
		List<Country> countrys = countryDao.listByCategoryId(categoryId);
		List<CountryListVO> countryListVO = Convert.sourceListToTargetList(countrys, CountryListVO.class);
		return countryListVO;
	}

}
