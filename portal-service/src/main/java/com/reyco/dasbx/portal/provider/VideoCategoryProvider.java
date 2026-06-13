package com.reyco.dasbx.portal.provider;

import org.springframework.beans.factory.annotation.Autowired;

import com.reyco.dasbx.es.support.result.facet.FacetLabelProvider;
import com.reyco.dasbx.portal.model.domain.vo.CategoryListVO;
import com.reyco.dasbx.portal.service.CategoryService;

public class VideoCategoryProvider implements FacetLabelProvider{
	
	private CategoryService categoryService;
	
	@Autowired
	public VideoCategoryProvider(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@Override
	public String getLabel(Object key) {
		Long  categoryId = Long.parseLong(key.toString());
		CategoryListVO categoryListVO = categoryService.get(categoryId);
		return categoryListVO.getName();
	}

}
