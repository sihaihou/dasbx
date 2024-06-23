package com.reyco.dasbx.common.core.service;

import com.reyco.dasbx.model.domain.Name;
/**
 * 字service
 * @author reyco
 *
 */
public interface NameService {
	
	Name getById(Long id,boolean gender);
	
	Name randomName();
	
	Name randomName(boolean gender);
	
	Name randomGirlName();
	
	Name randomMaleName();
	
}
