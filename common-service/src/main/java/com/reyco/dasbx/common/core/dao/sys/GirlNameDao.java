package com.reyco.dasbx.common.core.dao.sys;

import com.reyco.dasbx.model.domain.Name;

public interface GirlNameDao {
	
	Name getById(Long id);
	
	int getCount();
	
}
