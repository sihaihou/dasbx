package com.reyco.dasbx.portal.dao;

import java.util.List;

import com.reyco.dasbx.portal.model.domain.Country;

public interface CountryDao {
	
	Country getById(Long id);
	
	List<Country> list();
	
}
