package com.reyco.dasbx.portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.portal.dao.CategoryDao;
import com.reyco.dasbx.portal.model.domain.Category;
import com.reyco.dasbx.portal.model.domain.vo.CategoryListVO;
import com.reyco.dasbx.portal.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
	@Autowired
	private CategoryDao categoryDao;
	@Override
	public List<CategoryListVO> listByLimit(Integer size) {
		 List<Category> listByLimit = categoryDao.listByLimit(size);
		 List<CategoryListVO> categoryListVOs = Convert.sourceListToTargetList(listByLimit, CategoryListVO.class);
		 return categoryListVOs;
	}

}
