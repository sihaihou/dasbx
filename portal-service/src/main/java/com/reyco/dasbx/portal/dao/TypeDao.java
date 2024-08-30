package com.reyco.dasbx.portal.dao;

import java.util.List;

import com.reyco.dasbx.portal.model.domain.Type;

public interface TypeDao {
	
	Type getById(Long id);
	
	List<Type> list();
	
}
