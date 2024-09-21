package com.reyco.dasbx.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.model.constants.CachePrefixInfoConstants;
import com.reyco.dasbx.portal.dao.YearDao;
import com.reyco.dasbx.portal.model.domain.Year;
import com.reyco.dasbx.portal.model.domain.vo.YearListVO;
import com.reyco.dasbx.portal.service.YearService;

@Service
public class YearServiceImpl implements YearService {
	@Autowired
	private YearDao yearDao;

	@Override
	@Cacheable(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.PORTAL_YEAR_INFO_PREFIX	,key="#id")
	public YearListVO get(Long id) {
		Year year = yearDao.getById(id);
		YearListVO yearListVO = Convert.sourceToTarget(year, YearListVO.class);
		return yearListVO;
	}
	
	@Override
	public List<YearListVO> list() {
		List<Year> years = yearDao.list();
		List<YearListVO> yearListVO = Convert.sourceListToTargetList(years, YearListVO.class);
		return yearListVO;
	}
	@Override
	public List<YearListVO> listByCategoryId(Long categoryId) {
		if(categoryId==null || categoryId.intValue()<1) {
			return new ArrayList<>();
		}
		List<Year> years = yearDao.listByCategoryId(categoryId);
		List<YearListVO> yearListVOs = Convert.sourceListToTargetList(years, YearListVO.class);
		return yearListVOs;
	}
}
