package com.reyco.dasbx.common.core.service;

import com.reyco.dasbx.model.domain.Surname;

public interface SurnameService {
	
	Surname getById(Long id);
	
	Surname randomSurname();
	
}
