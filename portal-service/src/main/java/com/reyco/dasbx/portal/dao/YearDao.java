package com.reyco.dasbx.portal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.portal.model.domain.Year;

public interface YearDao {
	
	Year getById(Long id);
	
	List<Year> list();
	
	List<Year> listByCategoryId(@Param("categoryId") Long categoryId);
}
