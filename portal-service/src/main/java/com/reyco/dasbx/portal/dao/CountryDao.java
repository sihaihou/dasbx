package com.reyco.dasbx.portal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.portal.model.domain.Country;

public interface CountryDao {
	
	Country getById(Long id);
	
	List<Country> list();
	
	List<Country> listByCategoryId(@Param("categoryId") Long categoryId);
}
