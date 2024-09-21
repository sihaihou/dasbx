package com.reyco.dasbx.portal.service;

import java.util.List;

import com.reyco.dasbx.portal.model.domain.vo.CategoryListVO;

public interface CategoryService {
	
	CategoryListVO get(Long id);
	
	List<CategoryListVO> list();
	
	List<CategoryListVO> listByLimit(Integer size);
}
