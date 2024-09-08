package com.reyco.dasbx.portal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.portal.model.domain.Type;

public interface TypeDao {
	
	Type getById(Long id);
	
	List<Type> list();
	
	List<Type> listByCategoryId(@Param("categoryId") Long categoryId);
}
