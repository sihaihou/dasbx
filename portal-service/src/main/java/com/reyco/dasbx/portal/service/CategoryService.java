package com.reyco.dasbx.portal.service;

import java.util.List;

import com.reyco.dasbx.portal.model.domain.vo.CategoryListVO;

public interface CategoryService {
	
	List<CategoryListVO> listByLimit(Integer size);
}
