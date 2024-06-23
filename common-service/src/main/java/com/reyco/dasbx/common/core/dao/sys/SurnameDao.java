package com.reyco.dasbx.common.core.dao.sys;

import com.reyco.dasbx.model.domain.Surname;

public interface SurnameDao {
	
	Surname getById(Long id);
	
	int getCount();
	
}
