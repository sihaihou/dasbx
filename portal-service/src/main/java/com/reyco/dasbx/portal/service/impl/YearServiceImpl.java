package com.reyco.dasbx.portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.portal.dao.YearDao;
import com.reyco.dasbx.portal.model.domain.Year;
import com.reyco.dasbx.portal.model.domain.vo.YearListVO;
import com.reyco.dasbx.portal.service.YearService;

@Service
public class YearServiceImpl implements YearService {
	@Autowired
	private YearDao yearDao;
	@Override
	public List<YearListVO> list() {
		List<Year> years = yearDao.list();
		List<YearListVO> yearListVO = Convert.sourceListToTargetList(years, YearListVO.class);
		return yearListVO;
	}

}
