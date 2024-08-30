package com.reyco.dasbx.portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.portal.dao.CountryDao;
import com.reyco.dasbx.portal.model.domain.Country;
import com.reyco.dasbx.portal.model.domain.vo.CountryListVO;
import com.reyco.dasbx.portal.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService {
	@Autowired
	private CountryDao countryDao;
	@Override
	public List<CountryListVO> list() {
		List<Country> countrys = countryDao.list();
		List<CountryListVO> countryListVO = Convert.sourceListToTargetList(countrys, CountryListVO.class);
		return countryListVO;
	}

}
