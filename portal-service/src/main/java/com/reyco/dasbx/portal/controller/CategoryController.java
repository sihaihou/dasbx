package com.reyco.dasbx.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.portal.model.domain.vo.CategoryListVO;
import com.reyco.dasbx.portal.service.CategoryService;

@RestController
@RequestMapping("/portal/category")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	@GetMapping("list")
	public Object list() {
		List<CategoryListVO> portalCategoryListVOs = categoryService.list();
		return R.success(portalCategoryListVOs);
	}
	
	@GetMapping("listByLimit")
	public Object listByLimit(int size) {
		List<CategoryListVO> portalCategoryListVOs = categoryService.listByLimit(size);
		return R.success(portalCategoryListVOs);
	}
}
