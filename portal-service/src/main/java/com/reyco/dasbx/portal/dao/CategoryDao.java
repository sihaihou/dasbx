package com.reyco.dasbx.portal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.portal.model.domain.Category;

public interface CategoryDao {
	
	Category getById(Long id);
	
	List<Category> listByLimit(@Param("limit")Integer size);
	
}
