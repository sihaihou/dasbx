package com.reyco.dasbx.portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.model.constants.CachePrefixInfoConstants;
import com.reyco.dasbx.portal.dao.CategoryDao;
import com.reyco.dasbx.portal.model.domain.Category;
import com.reyco.dasbx.portal.model.domain.vo.CategoryListVO;
import com.reyco.dasbx.portal.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Override
	@Cacheable(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.PORTAL_CATEGORY_INFO_PREFIX,key="#id"/*,unless="#result==null"*/)
	public CategoryListVO get(Long id) {
		Category category = categoryDao.getById(id);
		CategoryListVO categoryListVO = Convert.sourceToTarget(category, CategoryListVO.class);
		return categoryListVO;
	}
	@Override
	public List<CategoryListVO> list() {
		 List<Category> listByLimit = categoryDao.listByLimit(1000);
		 List<CategoryListVO> categoryListVOs = Convert.sourceListToTargetList(listByLimit, CategoryListVO.class);
		 return categoryListVOs;
	}
	@Override
	public List<CategoryListVO> listByLimit(Integer size) {
		 List<Category> listByLimit = categoryDao.listByLimit(size);
		 List<CategoryListVO> categoryListVOs = Convert.sourceListToTargetList(listByLimit, CategoryListVO.class);
		 return categoryListVOs;
	}

}
