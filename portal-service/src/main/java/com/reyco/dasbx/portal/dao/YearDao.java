package com.reyco.dasbx.portal.dao;

import java.util.List;

import com.reyco.dasbx.portal.model.domain.Year;

public interface YearDao {
	
	Year getById(Long id);
	
	List<Year> list();
	
}
