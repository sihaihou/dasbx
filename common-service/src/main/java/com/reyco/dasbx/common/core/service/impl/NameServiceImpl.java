package com.reyco.dasbx.common.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.common.core.dao.sys.GirlNameDao;
import com.reyco.dasbx.common.core.dao.sys.MaleNameDao;
import com.reyco.dasbx.common.core.service.NameService;
import com.reyco.dasbx.commons.utils.RandomUtils;
import com.reyco.dasbx.model.domain.Name;

@Service
public class NameServiceImpl implements NameService{
	
	@Autowired
	private GirlNameDao girlNameDao;
	@Autowired
	private MaleNameDao maleNameDao;

	@Override
	public Name getById(Long id, boolean gender) {
		return gender?girlNameDao.getById(id):maleNameDao.getById(id);
	}

	@Override
	public Name randomName() {
		int gender = RandomUtils.randomInt(9);
		return (gender&1)==1 ? randomName(false) : randomName(true);
	}

	@Override
	public Name randomName(boolean gender) {
		if(gender) {
			int girlCount = girlNameDao.getCount();
			int girlId = RandomUtils.randomInt(girlCount)+1;
			Name girlName = girlNameDao.getById(new Long(girlId));
			girlName.setGender(true);
			return girlName;
			
		}
		int maleCount = maleNameDao.getCount()+1;
		int maleId = RandomUtils.randomInt(maleCount)+1;
		Name maleName = maleNameDao.getById(new Long(maleId));
		maleName.setGender(false);
	    return maleName;
	}

	@Override
	public Name randomMaleName() {
		return randomName(false);
	}

	@Override
	public Name randomGirlName() {
		return randomName(true);
	}

}
