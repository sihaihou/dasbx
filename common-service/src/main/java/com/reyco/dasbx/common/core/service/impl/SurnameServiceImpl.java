package com.reyco.dasbx.common.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.common.core.dao.sys.SurnameDao;
import com.reyco.dasbx.common.core.service.SurnameService;
import com.reyco.dasbx.commons.utils.RandomUtils;
import com.reyco.dasbx.model.domain.Surname;

@Service
public class SurnameServiceImpl implements SurnameService {
	@Autowired
	private SurnameDao surnameDao;
	@Override
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
